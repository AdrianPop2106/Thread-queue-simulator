import java.util.List;

public class ConcreteStrategyTime implements Strategy{

    @Override
    public void addTask(List<Server> servers, Task t) {
        short minIndex=0,wait=(short)servers.get(0).getWaitingTime();
        for(int i=1;i<servers.size();i++)
            if(servers.get(i).getWaitingTime()<wait){
                minIndex=(short)i;
                wait=(short)servers.get(minIndex).getWaitingTime();
            }
        servers.get(minIndex).addTask(t);
    }
}
