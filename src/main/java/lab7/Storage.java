package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.Arrays;

public class Storage {
    private static final String server = "tcp://localhost:8088";
    private static final long NOTIFY_PERIOD = 5000;
    private static final String NOTIFY = "notify";
    private static final String RESULT = "cache";
    private static final String GET = "get";
    private static final String PUT = "put";

    public static void main(String[] argv){
        int start = Integer.parseInt(argv[0]);
        ArrayList<String> cache = new ArrayList<>(Arrays.asList(argv).subList(1, argv.length));
        int end = start + cache.size() - 1;
        ZContext context = new ZContext(1);
        ZMQ.Socket dealer = context.createSocket(SocketType.DEALER);
        dealer.connect(server);
        long time = System.currentTimeMillis();
        while (!Thread.currentThread().isInterrupted()){
            if (System.currentTimeMillis() - time >= NOTIFY_PERIOD){
                dealer.send(String.format("%s %d %d", NOTIFY, start, end));
                time = System.currentTimeMillis();
            }
            String msg = dealer.recvStr().toLowerCase();
            if (msg.contains(GET)) {
                int index = Integer.parseInt(msg.split(" ")[1]);
                dealer.send(String.format("%s %s",RESULT, cache.get(index - start)));
            }
            if (msg.contains(PUT)){
                String[] split = msg.split(" ");
                int index = Integer.parseInt(split[1]);
                String value = split[2];
                cache.set(index - start, value);
            }
        }
        context.destroySocket(dealer);
        context.destroy();
    }
}
