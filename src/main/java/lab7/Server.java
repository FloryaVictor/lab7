package lab7;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

public class Server {
    private static final String clientSever = "tcp://localhost:8086";
    private static final String storageSever = "tcp://localhost:8088";
    private static final int CLIENT_SOCKET_NUMBER = 0;
    private static final int STORAGE_SOCKET_NUMBER = 1;

    public static void main(String[] argv){
        ZContext context = new ZContext(1);
        ZMQ.Socket clientSocket = context.createSocket(SocketType.ROUTER);
        ZMQ.Socket storageSocket = context.createSocket(SocketType.ROUTER);
        clientSocket.bind(clientSever);
        storageSocket.bind(storageSever);
        ZMQ.Poller poller = 
        while (!Thread.currentThread().isInterrupted()){

        }
        context.destroySocket(clientSocket);
        context.destroySocket(storageSocket);
        context.destroy();
    }
}
