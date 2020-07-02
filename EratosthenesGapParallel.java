
public class EratosthenesGapParallel extends EratosthenesGap{
    Thread[] threads;
    static int nThreads;
    long sqrtMaxPrime;

    EratosthenesGapParallel(Prime p){
        super(p);
        nThreads = Runtime.getRuntime().availableProcessors();
        threads = new Thread[nThreads];
    }

    // finds primes with eratosthenes
    @Override
    public void findPrimes(Int3 minPrime, Int3 maxPrime, boolean minIsPrime){
        if (minPrime.dividable(2)){
            minPrime.addSmall(1);
        }
        if (maxPrime.dividable(2)){
            maxPrime.sub(1);
        }
        Int3 tempSmallMaxPrime = new Int3(smallMaxPrime);
        tempSmallMaxPrime.mul(tempSmallMaxPrime);
        if (maxPrime.greater(tempSmallMaxPrime)){
            maxPrime = tempSmallMaxPrime;
        }

        this.minPrime = minPrime;
        this.maxPrime = maxPrime;
        sqrtMaxPrime = maxPrime.sqrt(maxPrime);

        long bitArrSize = calculateBitArrSize(minPrime, maxPrime);
        bitArr = new BitArr(bitArrSize);

        long startIndex = 0;
        long endIndex = (bitArr.getLength()*16)/nThreads;
        int nThreadsMade = 0;

        long lastPrime = sqrtMaxPrime;

        if (!minIsPrime){
            //Cross out minPrime if minPrime is not a prime
            for (long k = 3;k <= sqrtMaxPrime; k +=2){
                if (p.isPrime(k) && minPrime.dividable(k)){
                    crossOut(k);
                    break;
                }
            }
        }

        long start = 0;
        long step = bitArr.getLength()*16/nThreads;
        long end = step;

        nThreadsMade = 0;
        for(int i = 0; i < nThreads; i++){
            if (i == (nThreads-1)){
                end = bitArr.getLength()*16;
            }
            threads[i] = new Thread(new ParallelCrossOut(start, end));
            threads[i].start();
            nThreadsMade++;
            start = end;
            end += step;
        }
        joinThreads(nThreadsMade);
    }

    private class ParallelCrossOut implements Runnable{
        long start, end;

        ParallelCrossOut(long start, long end){
            this.start = start;
            this.end = end;
        }

        public void run(){
            long arrSize = (bitArr.getLength())*16; // size in bits
            Int3 myPrime = minPrime.clone();
            myPrime.add(start);

            for (long k = 3;k <= sqrtMaxPrime; k +=2){

                if(p.isPrime(k)){
                    long k2 = k*2;
                    long index;

                    if (myPrime.greater(k)){
                        Int3 temp3 = myPrime.clone();
                        temp3.sub(new Int3(k));
                        long mod = temp3.modulo(k2);
                        index = k2 - mod + start;

                   } else{
                       Int3 kk = new Int3(k);
                       kk.mul(kk);
                       kk.sub(myPrime);
                       index = kk.toLong() + start;
                   }

                    while (index < end){
                        crossOut(index);
                        index += k2;
                    }
                }
            }
        }
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
}
