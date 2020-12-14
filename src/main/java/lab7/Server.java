package lab7;

import org.zeromq.*;

import java.util.ArrayList;

public class Server {
    private static final String clientSever = "tcp://localhost:8086";
    private static final String storageSever = "tcp://localhost:8088";
    private static final int CLIENT_SOCKET_NUMBER = 0;
    private static final int STORAGE_SOCKET_NUMBER = 1;
    private static final long TIMEOUT = 5000;
    private static final ArrayList<CacheStatus> caches = new ArrayList<>();

    public static void main(String[] argv){
        ZContext context = new ZContext(1);
        ZMQ.Socket clientSocket = context.createSocket(SocketType.ROUTER);
        clientSocket.bind(clientSever);
        ZMQ.Socket storageSocket = context.createSocket(SocketType.ROUTER);
        storageSocket.bind(storageSever);
        ZMQ.Poller poller = context.createPoller(2);
        poller.register(clientSocket, ZMQ.Poller.POLLIN);
        poller.register(storageSocket, ZMQ.Poller.POLLIN);
        while (poller.poll(TIMEOUT) != -1){
            if (poller.pollin(CLIENT_SOCKET_NUMBER)){
                ZMsg zmsg = ZMsg.recvMsg(clientSocket);
                String msg = zmsg.getLast().toString().toLowerCase();
                if (msg.contains("get")){
                    System.out.println(msg);
                    try {
                        String[] split = msg.split(" ");
                        int key = Integer.parseInt(split[1]);
                        boolean found = false;
                        for (CacheStatus cs : caches) {
                            if (cs.start <= key && cs.end >= key && cs.isFresh()){
                                cs.frame.send(storageSocket, ZFrame.REUSE | ZFrame.MORE);
                                zmsg.send(storageSocket, false);
                                found = true;
                                System.out.println("found");
                            }
                        }
                        if (!found){
                            zmsg.getLast().reset("not found");
                            zmsg.send(clientSocket);
                        }
                    }catch (Exception ignored) {
                        zmsg.getLast().reset("error");
                        zmsg.send(clientSocket);
                    }

                }
                if (msg.contains("put")){
                    try {
                        String[] split = msg.split(" ");
                        int key = Integer.parseInt(split[1]);
                        String value = split[2];
                        for (CacheStatus cs : caches) {
                            if (cs.start <= key && cs.end >= key){
                                cs.frame.send(storageSocket, ZFrame.REUSE | ZFrame.MORE);
                                zmsg.send(storageSocket, false);
                            }
                        }
                    }catch (Exception ignored) {
                        zmsg.getLast().reset("error");
                        zmsg.send(clientSocket);
                    }
                }
            }
            if (poller.pollin(STORAGE_SOCKET_NUMBER)){
                ZMsg zmsg = ZMsg.recvMsg(storageSocket);
                zmsg.unwrap();
                String msg = zmsg.getLast().toString().toLowerCase();
                if (msg.contains("notify")){
                    try {
                        String[] split = msg.split(" ");
                        String id = split[1];
                        int start = Integer.parseInt(split[2]);
                        int end = Integer.parseInt(split[3]);
                        boolean found = false;
                        int i = 0;
                        for(CacheStatus cs : caches){
                            if (cs.id.equals(id)){
                                found = true;
                                break;
                            }
                            i++;
                        }
                        if (!found){
                            caches.add(new CacheStatus(start, end, id, zmsg.getFirst()));
                        }else {
                            caches.get(i).start = start;
                            caches.get(i).end = end;
                            caches.get(i).time = System.currentTimeMillis();
                        }
                    }catch (Exception ignored){}
                }else {
                    zmsg.send(clientSocket);
                }
            }
        }
        context.destroySocket(clientSocket);
        context.destroySocket(storageSocket);
        context.destroy();
    }
}
