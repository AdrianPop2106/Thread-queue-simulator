import java.util.List;

public class ConcreteStrategyQueue implements Strategy{

    @Override
    public void addTask(List<Server> servers, Task t) {
        short minIndex=0,length=(short)servers.get(0).getSize();
        for(int i=1;i<servers.size();i++)
            if(servers.get(i).getSize()<length){
                minIndex=(short)i;
                length=(short)servers.get(minIndex).getSize();
            }
        servers.get(minIndex).addTask(t);
    }
}
