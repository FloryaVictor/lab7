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
            ZFrame frame = new ZFrame(command);
            frame.send(client, 0);
            System.out.println(Arrays.toString(ZMsg.recvMsg(client).getLast().getData()));
        }
        context.destroySocket(client);
        context.destroy();
    }
}
