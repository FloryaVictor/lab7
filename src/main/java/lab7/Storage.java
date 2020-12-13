package lab7;

import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.Arrays;

public class Storage {
    public static void main(String[] argv){
        int start = Integer.parseInt(argv[0]);
        ArrayList<String> cache = new ArrayList<>(Arrays.asList(argv).subList(1, argv.length));
        int end = start + cache.size() + 1;
    }
}
