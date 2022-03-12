import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private int id;
    private String fileName;
    private boolean closed;
    private float averageService;
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingTime;

    public Server(int id,String fileName){
        this.id=id;
        this.fileName=fileName;
        closed=true;
        averageService=0;
        tasks=new LinkedBlockingQueue<>();
        waitingTime=new AtomicInteger();
    }

    public void addTask(Task t){
        tasks.add(t);
        waitingTime.addAndGet(t.getTService());
    }

    @Override
    public void run(){
        try {
            FileWriter writer=new FileWriter(fileName,true);
            writer.write("Server "+id+":");
            if(tasks.size()!=0){
                if(!closed){
                    tasks.peek().reduceService();
                    waitingTime.decrementAndGet();
                }
                averageServiceTime();
                closed=false;
                if(tasks.peek().getTService()==0)
                    tasks.poll();
                Object[] temp=  tasks.toArray();
                if(tasks.size()==0) {
                    writer.write("closed");
                    closed=true;
                }
                else
                for(int i=0;i<tasks.size();i++)
                    writer.write("("+((Task)temp[i]).getId()+","+((Task)temp[i]).getTArrival()+","+((Task)temp[i]).getTService()+");");
                writer.write("\n");
            }
            else
                writer.write("closed\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void averageServiceTime(){
        if(tasks.size()!=0){
            int sum=0;
            Object[] temp=  tasks.toArray();
            for(int i=0;i<getSize();i++){
                if(!closed && i==0)
                    continue;
                sum+=((Task)temp[i]).getTService();
            }
                averageService=(float)sum/getSize();
        }
    }

    public int getWaitingTime(){
        return waitingTime.get();
    }
    public int getSize(){
        return tasks.size();
    }
    public float getAverageService(){return averageService;}
}
