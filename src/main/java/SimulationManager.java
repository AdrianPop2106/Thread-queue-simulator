import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients;
    public float averageServiceTime;

    String fileName;
    public SelectionPolicy selectionPolicy=SelectionPolicy.SHORTEST_QUEUE;

    private Scheduler scheduler;
    private List<Task> generatedTasks;

    public SimulationManager(int limit,int maxArrival,int minArrival,int maxProcess,int minProcess,int servers,int clients,String fileName){
        timeLimit=limit;
        this.fileName=fileName;
        averageServiceTime=0;
        maxProcessingTime=maxProcess;
        minProcessingTime=minProcess;
        numberOfServers=servers;
        numberOfClients=clients;
        scheduler=new Scheduler(numberOfServers,fileName);
        scheduler.changeStrategy(selectionPolicy);
        generatedTasks=new ArrayList<>();
        generateNRandomTasks(minArrival,maxArrival);
        new File(fileName);
    }

    private void generateNRandomTasks(int minArrival,int maxArrival){
        for(int i=0;i<numberOfClients;i++){
            Random rand=new Random();
            int proc=rand.nextInt(maxProcessingTime-minProcessingTime+1)+minProcessingTime;
            int arr=rand.nextInt(maxArrival-minArrival+1)+minArrival;
            generatedTasks.add(new Task(i+1,arr,proc));
        }
        Collections.sort(generatedTasks);
    }

    @Override
    public void run() {
        try {
        new FileWriter(fileName, false).close();
        int currentTime=0;
        while(currentTime<timeLimit){
            FileWriter writer=new FileWriter(fileName,true);
            showing(currentTime,writer);
            if(generatedTasks.isEmpty() && scheduler.emptyServers())
                break;
            writer.close();
            currentTime++;
        }
            FileWriter fin=new FileWriter(fileName,true);
            fin.write("Peak Hour:"+scheduler.getPeakHour()+" ("+scheduler.getPeak()+" Clients)\n");
            fin.write("Average Service Time:"+averageServiceTime+"\n");
            fin.close();
        } catch (IOException | InterruptedException e) { e.printStackTrace();}
    }

    private void showing(int currentTime, FileWriter writer) throws IOException, InterruptedException {
        int i=0,waitTimes=0;
        writer.write("Time "+currentTime+"\nWaiting:");
        while(i<generatedTasks.size() && !generatedTasks.isEmpty()){
            if(generatedTasks.get(i).getTArrival()==currentTime) {
                scheduler.dispatchTask(generatedTasks.get(i));
                generatedTasks.remove(i);
                continue;
            }
            writer.write("("+generatedTasks.get(i).getId()+","+generatedTasks.get(i).getTArrival()+","+generatedTasks.get(i).getTService()+");");
            waitTimes+=generatedTasks.get(i).getTArrival();
            i++;
        }
        writer.write("\n");
        averages(writer,waitTimes);
        writer.close();
        for(i=0;i<numberOfServers;i++){
            Thread temp=new Thread(scheduler.getServers().get(i));
            temp.start();
            temp.join();
        }
        if(scheduler.peakClients())
            scheduler.setPeakHour(currentTime);
        Thread.sleep(500);
    }

    private void averages(FileWriter writer,int waitTimes) throws IOException {
        if(generatedTasks.size()!=0)
            writer.write("Average Waiting Time:"+(float)waitTimes/generatedTasks.size()+"\n");
        else
            writer.write("Average Waiting Time:"+0+"\n");
        if(scheduler.getAverageServiceTime()!=0){
            if(averageServiceTime==0)
                averageServiceTime= scheduler.getAverageServiceTime();
            else
                averageServiceTime= (averageServiceTime+scheduler.getAverageServiceTime())/2;
        }
    }


}
