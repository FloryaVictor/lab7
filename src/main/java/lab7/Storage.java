package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class Storage {
    private static final String server = "tcp://localhost:8088";
    private static final long NOTIFY_PERIOD = 1000;
    private static final String NOTIFY = "notify";
    private static final String RESULT = "cache";
    private static final String GET = "get";
    private static final String PUT = "put";
    private static final int TIMEOUT = 3000;
    private static final String id = UUID.randomUUID().toString();

    public static void main(String[] argv){
        int start = Integer.parseInt(argv[0]);
        ArrayList<String> cache = new ArrayList<>(Arrays.asList(argv).subList(1, argv.length));
        int end = start + cache.size() - 1;
        ZContext context = new ZContext(1);
        ZMQ.Socket dealer = context.createSocket(SocketType.DEALER);
        dealer.connect(server);
        ZMQ.Poller poller = context.createPoller(1);
        poller.register(dealer);
        long time = System.currentTimeMillis();
        while (poller.poll(TIMEOUT) != -1){
            if (System.currentTimeMillis() - time >= NOTIFY_PERIOD){
                System.out.println("notify");
                dealer.send(String.format("%s %d %d", NOTIFY, start, end));
                time = System.currentTimeMillis();
            }
            if (poller.pollin(0)) {
                ZMsg zmsg = ZMsg.recvMsg(dealer);
                String msg = zmsg.getLast().toString().toLowerCase();
                if (msg.contains(GET)) {
                    System.out.println(msg);
                    try {
                        int index = Integer.parseInt(msg.split(" ")[1]);
                        zmsg.getLast().reset(String.format("%s %s", RESULT, cache.get(index - start)));
                    } catch (NumberFormatException | IndexOutOfBoundsException e) {
                        zmsg.getLast().reset("error");
                    }
                    zmsg.send(dealer);
                }
                if (msg.contains(PUT)) {
                    try {
                        String[] split = msg.split(" ");
                        int index = Integer.parseInt(split[1]);
                        String value = split[2];
                        cache.set(index - start, value);
                    } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
                    }
                }
            }
        }
        context.destroySocket(dealer);
        context.destroy();
    }
}
