
public class PrimeCrossParallel extends PrimeCross{
    Thread[] threads;
    static int nThreads;
    long globalStart = 0;
    boolean factorFound = false;

    PrimeCrossParallel(long maxPrime){
        super(maxPrime);
        this.nThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("Nthread: " + nThreads);
    }

    PrimeCrossParallel(long maxPrime, int nThreads){
        super(maxPrime);
        this.nThreads = nThreads;
        System.out.println("Nthread: " + nThreads);
    }

    private void parallel357(){
      int nThreadsMade = 0;
      long start = 1;
      long stepSize = bitArr.getLength()/nThreads;
      long stop = stepSize;

      for(int i = 0; i < nThreads; i++){
          if (i+1 == nThreads){
              stop = bitArr.getLength();
          }
          threads[i] = new Thread(new ParallelCross357(start, stop));
          threads[i].start();
          start = stop;
          stop += stepSize;
          nThreadsMade++;
      }

      joinThreads(nThreadsMade);
      }

    // finds primes with eratosthenes
    @Override
    public void findPrimes(){
        threads = new Thread[nThreads];
        parallel357();

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

    private class ParallelCross357 implements Runnable{
        long start, stop;

        ParallelCross357(long start, long stop){
            this.start = start;
            this.stop = stop;
        }

        public void run(){
            //0x49 = 01001001, 0x24= 00100100, 0x92 = 10010010.
            byte[] crossOut3 = new byte[] {(byte)0x49, (byte)0x24, (byte)0x92};
            // 0x21 = 00100001, 0x8 = 00001000, 0x42 = 01000010, 0x10 = 00010000, 0x84 = 10000100
            byte[] crossOut5 = new byte[] {(byte)0x21, (byte)0x8, (byte)0x42, (byte)0x10, (byte)0x84};
            // 00010000 00100000 01000000 10000001 00000010 00000100 00001000
            byte[] crossOut7 = new byte[] {(byte)0x10, (byte)0x20, (byte)0x40, (byte)0x81, (byte)0x2, (byte)0x4, (byte)0x8};

            for(long i = start; i < stop; i++){
                bitArr.set(i, (byte) (crossOut3[(int)(i%3)] | crossOut5[(int)(i%5)] | crossOut7[(int)(i%7)]));
            }
        }
    }
}
