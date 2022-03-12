public class Task implements Comparable<Task>{
    private int id;
    private int tArrival;
    private int tService;

    Task(int id, int arr, int ser){
        this.id=id;
        tArrival=arr;
        tService=ser;
    }

    public int getTArrival(){
        return tArrival;
    }

    public int getId(){
        return id;
    }

    public int getTService(){
        return tService;
    }

    public void reduceService(){
        tService--;
    }

    @Override
    public int compareTo(Task t) {
        if(this.tArrival<t.getTArrival())
            return -1;
        if(this.tArrival>t.getTArrival())
            return 1;
        return 0;
    }
}
