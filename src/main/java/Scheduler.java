import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int peak;
    private int peakHour;
    private int maxNoServers;
    private Strategy strategy;

    Scheduler(int maxServers,String fileName) {
        maxNoServers = maxServers;
        peak=0;
        peakHour=0;
        servers = new ArrayList<>();
        for (int i = 0; i < maxNoServers; i++) {
            Server s = new Server(i+1,fileName);
            servers.add(s);
            servers.get(i).run();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ConcreteStrategyQueue();
        if (policy == SelectionPolicy.SHORTEST_TIME)
            strategy = new ConcreteStrategyTime();
    }

    public boolean peakClients(){
        int sum=0;
        for(Server s:servers){
            sum+=s.getSize();
        }
        if(peak<sum){
            peak=sum;
            return true;
        }
        return false;
    }

    public boolean emptyServers(){
        for(Server s:servers)
            if(s.getSize()!=0)
                return false;
        return true;
    }
    public float getAverageServiceTime(){
        float sum=0;
        int i=0;
        for(Server s:servers){
            if(s.getSize()==0)
                continue;
            sum+=s.getAverageService();
            i++;
        }
        if(sum==0)
            return 0;
        else
            return sum/i;
    }
    public void dispatchTask(Task t) { strategy.addTask(servers, t); }
    public List<Server> getServers(){
        return servers;
    }
    public void setPeakHour(int nr){ peakHour=nr;}
    public int getPeakHour(){ return peakHour;}
    public int getPeak(){ return peak;}
}
