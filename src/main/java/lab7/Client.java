package lab7;

import org.zeromq.*;
import java.util.Scanner;


public class Client {
    public static String server = "tcp://localhost:8086";
    public static int RECEIVE_TIMEOUT = 3000;
    public static void main(String[] argv){
        ZContext context = new ZContext(1);
        ZMQ.Socket client = context.createSocket(SocketType.REQ);
        client.setReceiveTimeOut(RECEIVE_TIMEOUT);
        client.connect(server);
        Scanner in = new Scanner(System.in);
        while (true){
            String command = in.nextLine();
            if (command.equals("quit")){
                break;
            }
            client.send(command);
            String rep = client.recvStr();
            if (rep != null) {
                System.out.println(rep);
            }else {
                System.out.println("no answer");
                context.destroySocket(client);
                client = context.createSocket(SocketType.REQ);
                client.connect(server);
            }
        }
        context.destroySocket(client);
        context.destroy();
    }
}
