package lab7;

import org.zeromq.ZFrame;

public class CacheStatus {
    public long time;
    public int start;
    public int end;
    ZFrame frame;
    public CacheStatus(int start, int end, ZFrame frame){
        this.start = start;
        this.end = end;
        this.frame = frame;
    }
}