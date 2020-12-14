package lab7;

import org.zeromq.ZFrame;

public class CacheStatus {
    public long time;
    public int start;
    public int end;
    public String id;
    public ZFrame frame;
    private static long TIMEOUT = 5000;
    public CacheStatus(int start, int end, String id, ZFrame frame){
        this.start = start;
        this.end = end;
        this.frame = frame;
        this.time = System.currentTimeMillis();
    }

    boolean isFresh(){
        return System.currentTimeMillis() - time <= TIMEOUT;
    }

}
