package lab7;

import org.zeromq.*;

import java.util.ArrayList;

public class Server {
    private static final String clientSever = "tcp://localhost:8086";
    private static final String storageSever = "tcp://localhost:8088";
    private static final int CLIENT_SOCKET_NUMBER = 0;
    private static final int STORAGE_SOCKET_NUMBER = 1;
    private static final ArrayList<CacheStatus> caches = new ArrayList<>();

    public static void main(String[] argv){
        ZContext context = new ZContext(1);
        ZMQ.Socket clientSocket = context.createSocket(SocketType.ROUTER);
        ZMQ.Socket storageSocket = context.createSocket(SocketType.ROUTER);
        clientSocket.bind(clientSever);
        storageSocket.bind(storageSever);
        ZMQ.Poller poller = context.createPoller(2);
        poller.register(clientSocket, ZMQ.Poller.POLLIN);
        poller.register(storageSocket, ZMQ.Poller.POLLIN);
        while (!Thread.currentThread().isInterrupted()){
            if (poller.pollin(0)){
                ZMsg msg = ZMsg.recvMsg(clientSocket);
                String msgString = msg.getLast().toString();
                if (msgString.contains("put")){
                    try {
                        String[] split = msgString.split(" ");
                        int key = Integer.parseInt(split[1]);
                        String value = split[2];
                        for (CacheStatus cs : caches) {
                            if (cs.start <= key && cs.end >= key){
                                cs.frame.send(clientSocket, ZFrame.REUSE | ZFrame)
                            }
                        }
                    }catch (Exception ignored) {
                    }
                }
            }
            if (poller.pollin(1)){
                ZMsg msg = ZMsg.recvMsg(clientSocket);

            }
        }
        context.destroySocket(clientSocket);
        context.destroySocket(storageSocket);
        context.destroy();
    }
}
