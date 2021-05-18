package Modul6;

import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;

    public class PrimeChecker {

        private int numberOfThreads=10;
        private ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);
        private boolean isPrime;
        private long number = 9223372036854775783L;
        private long[] startValues = new long[numberOfThreads];
        private long[] endValues = new long[numberOfThreads];

        PrimeChecker() {

            System.out.println("Testing primality of " + number + ", multithreaded:");
            long time = System.currentTimeMillis();
            try{
                init();
                runThreads();
                while(!pool.isTerminated());
            }
            catch(Exception ex){
                System.out.println("An exception occured: " + ex.getMessage());
            }
            time = System.currentTimeMillis()-time;


            if (isPrime) {
                System.out.printf("%d is a prime.%n", number);
            } else {
                System.out.printf("%d is not prime.%n", number);
            }
            System.out.printf("Execution time multithreaded: %dms%n%n", time);

            System.out.println("Testing primality of " + number + ", singlethreaded:");
            time = System.currentTimeMillis();
            isPrime = singleThreadedPrimeCheck();
            time = System.currentTimeMillis()-time;

            if (isPrime) {
                System.out.printf("%d is a prime.%n", number);
            } else {
                System.out.printf("%d is not prime.%n", number);
            }
            System.out.printf("Execution time singlethreaded: %dms%n%n", time);

        }

        public static void main(String[] args) {
            PrimeChecker primeChecker = new PrimeChecker();
            //kill it fast, so that we dont have to wait for the executor-service to time out...
            System.exit(0);
        }
        private void init() {
            long step = (long)Math.sqrt(number)/numberOfThreads+1;
            for(int i=1; i<numberOfThreads; i++){
                endValues[i-1]=step*i + 3;
                startValues[i]=endValues[i-1]+1;
            }
            startValues[0]=3;
            endValues[numberOfThreads-1]=step*numberOfThreads;
        }
        private void runThreads(){
            for (int i = 0; i < numberOfThreads; i++)
                pool.execute(new PrimeTask(number, startValues[i], endValues [i]));
        }
        private void notPrime() {
            pool.shutdown();
            isPrime = false;
        }
        private boolean singleThreadedPrimeCheck() {
            if (number == 2 || number == 3) {
                return true;
            }
            if (number % 2 == 0) {
                return false;
            }
            for (long i = 3; i <= Math.sqrt(number); i = i+2) {
                if (number % i == 0) {
                    return false;
                }
            }
            return true;
        }
        private class PrimeTask implements Runnable {
            private long number;
            private long startValue;
            private long endValue;

            PrimeTask(long number, long startValue, long endValue) {
                this.number = number;
                this.startValue = startValue;
                this.endValue = endValue;
            }
            @Override
            public void run() {
                if (number % 2 == 0) {
                    notPrime();
                }
                for (long i = startValue; i <= endValue; i+=2) {
                    if (number % i == 0) {
                        notPrime();
                    }
                }
                if (endValue == endValues[numberOfThreads - 1]){
                    //if we get here without any other threads requesting to shut down pool,
                    pool.shutdown();
                    isPrime = true;
                }
            }
        }
    }