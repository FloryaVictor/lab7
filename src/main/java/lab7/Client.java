package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.Scanner;


public class Client {
    public static String server = "tcp://localhost:8086";

    public static void main(String[] argv){
        ZContext context = new ZContext(1);
        ZMQ.Socket client = context.createSocket(SocketType.REQ);
        client.connect(server);
        Scanner in = new Scanner(System.in);
        while (!Thread.currentThread().isInterrupted()){
            String command = in.nextLine();
            if (command == )
        }
    }
}
