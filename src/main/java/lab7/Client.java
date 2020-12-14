package lab7;

import org.zeromq.*;

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
            //client.send(command, 0);
            frame.send(client, 0);
            System.out.println(ZMsg.recvMsg(client).getLast().getData(), ZMQ.CHARSET);
        }
        context.destroySocket(client);
        context.destroy();
    }
}
