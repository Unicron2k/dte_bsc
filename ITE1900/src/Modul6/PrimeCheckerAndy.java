package Modul6;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrimeCheckerAndy {
    private ExecutorService pool;
    private boolean isPrime;
    private long number;
    private int numberOfThreads;
    private long[] startValues;
    private long[] endValues;

    private PrimeCheckerAndy(long number, int numberOfThreads) {
        this.number = number;
        this.numberOfThreads = numberOfThreads;
        init();

        /* Output */
        long startTime = System.currentTimeMillis();
        runThreads();
        while (!pool.isTerminated()); // Wait for threads to finish
        long time = System.currentTimeMillis() - startTime;
        System.out.printf("%s is%sa prime number. \nExecution time: %s ms. \n\n", number, isPrime ? " " : " not ", time);
    }

    public static void main(String[] args){
        PrimeCheckerAndy primeChecker = new PrimeCheckerAndy(9223372036854775783L, 10);
    }

    private void init(){
        startValues = new long[numberOfThreads];
        endValues = new long[numberOfThreads];

        pool = Executors.newFixedThreadPool(numberOfThreads);
        long iterator = (long)(Math.sqrt(number) / numberOfThreads);

        // Partition startValues[] and endValues[] for the threads to compute
        for (int i = 0; i < numberOfThreads; i++) {
            if (i == 0) {
                startValues[i] = 3;
                endValues[i] = startValues[i] + iterator;
            } else {
                startValues[i] = endValues[i - 1] + 1;
                endValues[i] = startValues[i] + iterator;
            }
            System.out.printf("[%s] start: %s, end: %s\n", i, startValues[i], endValues[i]);
        }

    }
    private void runThreads(){
        for (int i = 0; i < numberOfThreads; i++)
            pool.execute(new PrimeTask(number, startValues[i], endValues [i]));
    }
    private void notPrime(){
        pool.shutdown();
        isPrime = false;
    }
    class PrimeTask implements Runnable {
        private long number;
        private long startValue; // Where to start checking if number is prime
        private long endValue; // Where to stop checking if number is prime

        PrimeTask(long number, long startValue, long endValue){
            this.number = number;
            this.startValue = startValue;
            this.endValue = endValue;
        }

        @Override
        public void run() {
            if (number % 2 == 0)
                notPrime();

            for (long i = startValue; i <= endValue; i = i+2)
                if (number % i == 0)
                    notPrime();

            if (endValue == endValues[numberOfThreads - 1]) { // If current thread is last thread, shutdown and isPrime is set to true
                pool.shutdown();
                isPrime = true;
            }
        }
    }
}
