package lab7;

import org.zeromq.*;
import java.util.Scanner;


public class Client {
    public static String server = "tcp://localhost:8086";

    public static void main(String[] argv){
        ZContext context = new ZContext(1);
        ZMQ.Socket client = context.createSocket(SocketType.REQ);
        client.setReceiveTimeOut(3000);
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
