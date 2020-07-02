public class PrimeParallel extends Prime{
    Thread[] threads;
    static int nThreads;

    PrimeParallel(long maxPrime){
        super(maxPrime);
        this.nThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("Nthread: " + nThreads);
    }

    PrimeParallel(long maxPrime, int nThreads){
        super(maxPrime);
        this.nThreads = nThreads;
        System.out.println("Nthread: " + nThreads);
    }

    // finds primes with eratosthenes
    @Override
    public void findPrimes(){
        threads = new Thread[nThreads];
        int nThreadsMade = 0;
        long start = 0;
        long temp = bitArr.getLength()/nThreads;
        long end = temp;
        System.out.println("Maxprime " + maxPrime);

        for(int i = 0; i < nThreads; i++){
            if (i == (nThreads-1)){
                end = bitArr.getLength();
            }
            threads[i] = new Thread(new ParallelCrossOut(start, end));
            threads[i].start();
            nThreadsMade++;
            start = end;
            end += temp;
        }
        joinThreads(nThreadsMade);
    }

    //Joins the threads
    private void joinThreads(int nThreadsMade){
        for (int i = 0; i < nThreadsMade; i++){
            try{
                threads[i].join();
            } catch (InterruptedException e){
                return;
            }
        }
    }

    private class ParallelCrossOut implements Runnable{
        long start, end;

        ParallelCrossOut(long start, long end){
            this.start = (start*16)+1;
            this.end = (end*16)+1;
        }

        public void run(){
            long sqrtMaxPrime = (long)Math.sqrt(maxPrime);
            for (long k = STARTPRIME; k <= sqrtMaxPrime; k +=2){
                if(isPrime(k)){
                    long product = k*k;
                    long crossOut = product;
                    long k2 = k*2;

                    if (start > product){
                        crossOut = start + (k2 - ((start - product)%k2));
                    }

                    while (crossOut < end){
                        crossOut(crossOut);
                        crossOut +=k2;
                    }
                }
            }
        }
    }
}
