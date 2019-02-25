package client;

import common.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Initial point of execution.
 * Do not change this class.
 */

public class Main {

    private static final boolean GLOBAL_VERBOSE_FLAG = false;

    private static final String[] argNames = {"AICHostIP", "AICHostPort", "AICToken", "AICRetryDelay"};
    private static final String[] argDefaults = {"localhost", "7099", "00000000000000000000000000000000", "1000"};

    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        arrayList.add(1);
        arrayList.add(2);
        arrayList.add(3);
        System.out.println("first array:" + arrayList);
        chaneVal(arrayList);
        System.out.println("final array:" + arrayList);

        try {
            run(getArgs());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void chaneVal(ArrayList<Integer> arrayList){

        for (Integer val: arrayList){
            int i=0;
            for (Integer val2: arrayList){
                if (val==val2){
                    arrayList.set(i,i+2);
                }
                i++;
            }
        }
    }

    private static void run(String[] args) throws FileNotFoundException {
        if(GLOBAL_VERBOSE_FLAG || Arrays.asList(args).contains("--verbose")) {
            Log.DEV_MODE = true;
            Log.LOG_LEVEL = Log.VERBOSE;

            Log.outputFile = new FileOutputStream("client.log", false);
        }

        try {
            Controller c = new Controller(args[0], Integer.parseInt(args[1]), args[2], Long.parseLong(args[3]));
            c.start();
        } catch (Exception e) {
            System.err.println("Client terminated with error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String[] getArgs() {
        String[] args = new String[argNames.length];
        for (int i = 0; i < argNames.length; i++) {
            args[i] = System.getenv(argNames[i]);
            if (args[i] == null)
                args[i] = argDefaults[i];
            Log.i("PARAM", argNames[i] + "=" + args[i]);
        }
        return args;
    }

}