package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;

import java.util.Scanner;


public class Client {
    public static String server = "tcp://localhost:8086";

    public static void main(String[] argv) throws InterruptedException {
        ZContext context = new ZContext(1);
        ZMQ.Socket client = context.createSocket(SocketType.REQ);
        client.connect(server);
        Scanner in = new Scanner(System.in);
        while (!Thread.currentThread().isInterrupted()){
            String command = in.nextLine();
            if (command.equals("quit")){
                break;
            }
            client.send(command, 0);
            System.out.println(client.recvStr(0));
        }
        context.destroySocket(client);
        context.destroy();
    }
}
