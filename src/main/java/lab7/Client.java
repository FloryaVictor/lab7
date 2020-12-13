package lab7;

import org.zeromq.ZContext;

import java.net.Socket;

public class Client {
    public static String server = "tcp://localhost:8086";

    public static void main(String[] argv){
        ZContext context = new ZContext(1);
        Socket client = context.createSocket()
    }
}
