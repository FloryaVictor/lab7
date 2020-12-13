package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.Arrays;

public class Storage {
    private static String server = "tcp://localhost:8088";

    public static void main(String[] argv){
        int start = Integer.parseInt(argv[0]);
        ArrayList<String> cache = new ArrayList<>(Arrays.asList(argv).subList(1, argv.length));
        int end = start + cache.size() - 1;
        ZContext context = new ZContext(1);
        ZMQ.Socket socket = context.createSocket(SocketType.DEALER);
        socket.connect(server);
        
        while (!Thread.currentThread().isInterrupted()){

        }
    }
}
