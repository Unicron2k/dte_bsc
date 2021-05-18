package Modul6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VLASearch {

    private static int indexNo;
    private static int[] array = new int[1000000];

    private static ExecutorService threads = Executors.newFixedThreadPool(10);

    public static void main(String args[]) {

        int num=0;
        boolean millionSet=false;

        for (int i = 0; i < 1000000; i++) {
            num = (int) (Math.random() * 1000001);
            if(num==1000000){
                millionSet=true;
            }
            array[i] = num;
        }
        //If no million was generated, we set one manually at a random position
        if(!millionSet) {
            array[(int) (Math.random() * 1000000)] = 1000000;
        }

        try {
            threads.execute(new ArraySearchTask(0, 100000, 1000000));
            threads.execute(new ArraySearchTask(100001, 200000, 1000000));
            threads.execute(new ArraySearchTask(200001, 300000, 1000000));
            threads.execute(new ArraySearchTask(300001, 400000, 1000000));
            threads.execute(new ArraySearchTask(400001, 500000, 1000000));
            threads.execute(new ArraySearchTask(500001, 600000, 1000000));
            threads.execute(new ArraySearchTask(600001, 700000, 1000000));
            threads.execute(new ArraySearchTask(700001, 800000, 1000000));
            threads.execute(new ArraySearchTask(800001, 900000, 1000000));
            threads.execute(new ArraySearchTask(900001, 999999, 1000000));
        }
        //LazyCatch... If a thread throws an exception, it's most likely due to it being terminated(or maybe array index out of bounds).
        catch (Exception ex){
            throw ex;
        }

        while(true) {
            if (threads.isShutdown()) {
                System.out.println("Found '1000000' at index " + indexNo);
                break;
            }
        }

    }

    public static class ArraySearchTask implements Runnable {

        private int start = 0, end = 0, number = 0;

        ArraySearchTask(int start, int end, int number) {
            this.start = start;
            this.end = end;
            this.number = number;
        }

        @Override
        public void run() {
            for (int i = start; i <= end; i++) {
                if (array[i] == number) {
                    indexNo = i;
                    threads.shutdown();
                }
            }
        }
    }
}