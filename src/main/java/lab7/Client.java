package lab7;

import org.zeromq.*;

import java.util.Arrays;
import java.util.Scanner;


public class Client {
    public static String server = "tcp://localhost:8086";

    public static void main(String[] argv) throws InterruptedException {
        ZContext context = new ZContext(1);
        ZMQ.Socket client = context.createSocket(SocketType.REQ);
        client.connect(server);
        Scanner in = new Scanner(System.in);
        while (true){
            String command = in.nextLine();
            if (command.equals("quit")){
                break;
            }
            client.send(command);
            System.out.println(client.recvStr());
        }
        context.destroySocket(client);
        context.destroy();
    }
}
