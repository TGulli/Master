import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;

class EratosthenesGap{
    BitArr bitArr; // bit table for eratosthenes
    byte[] bitMask = {(byte) 128, (byte) 64, (byte) 32, (byte) 16, (byte) 8, (byte) 4, (byte) 2, (byte) 1};
    long smallMaxPrime;
    Int3 minPrime, maxPrime;
    Prime p;
    GapSave gs;

    EratosthenesGap(Prime p){
        this.p = p;
        this.smallMaxPrime = p.getMaxPrime();
    }

    protected long calculateBitArrSize(Int3 minPrime, Int3 maxPrime){
        Int3 tempMaxPrime = maxPrime.clone();
        tempMaxPrime.sub(minPrime);

        tempMaxPrime.div(16);
        tempMaxPrime.addSmall(1);
        return tempMaxPrime.toLong();
    }

    // finds primes with eratosthenes
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
        long bitArrSize = calculateBitArrSize(minPrime, maxPrime);
        System.out.println("bitArrSize " + bitArrSize);
        bitArr = new BitArr(bitArrSize);
        long arrSize = (bitArr.getLength())*16; // size in bits
        long sqrtMaxPrime = maxPrime.sqrt(maxPrime);

        if (!minIsPrime){
            //Cross out minPrime if minPrime is not a prime
            for (long k = 3;k <= sqrtMaxPrime; k +=2){
                if (p.isPrime(k) && minPrime.dividable(k)){
                    crossOut(k);
                    break;
                }
            }
        }

        for (long k = 3;k <= sqrtMaxPrime; k +=2){
            if(p.isPrime(k)){
                long k2 = k*2;
                long index;

                if (minPrime.greater(k)){
                    Int3 temp3 = minPrime.clone();
                    temp3.sub(new Int3(k));
                    long mod = temp3.modulo(k2);
                    index = k2 - mod;
                } else{
                    Int3 kk = new Int3(k);
                    kk.mul(kk);
                    kk.sub(minPrime);
                    index = kk.toLong();
                }

                while (index < arrSize){
                    crossOut(index);
                    index += k2;
                }
            }
        }
    }

    // prints some of the biggest primes
    public void printPrimes(){
        System.out.println("Primes:");

        Int3 n = maxPrime;
        int counter = 0;

        while ((!n.greater(maxPrime)) && counter < 5){
            if (isPrime(n)){
                System.out.print(n + ", ");
                counter++;
            }
            n.sub(2);
        }
        System.out.println();
    }

    public void printAllPrimes(){
        System.out.println("Primes:");

        Int3 n = minPrime.clone();
        System.out.println("Start: " + n);

        while (!n.greater(maxPrime)){
            if (isPrime(n)){
                System.out.print(n + ", ");
            }
            n.addSmall(2);
        }
        System.out.println();
    }

    public void writePrimes(String filename){
        Int3 n = minPrime.clone();
        PrintWriter writer = null;
        try{
            writer = new PrintWriter(new FileOutputStream(filename), true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        writer.write("Primes:");
        writer.println();

        while (maxPrime.greater(n)){
            if (isPrime(n)){
                writer.println(n);
            }
            n.addSmall(2);
        }
        writer.println();
        writer.close();
    }

    public Int3 findNextPrime(Int3 p){
        Int3 p2 = p.clone();
        p2.addSmall(2);

        while (maxPrime.greater(p2)){
            if (isPrime(p2)){
                return p2;
            }
            p2.addSmall(2);
        }
        return null;
    }

    public Int3 findPrevPrime(Int3 p){
        Int3 p2 = p.clone();
        p2.sub(2);
        while (!p2.less(minPrime)){
            if (isPrime(p2)){
                return p2;
            }
            p2.sub(2);
        }
        return null;
    }

    public boolean isPrime(Int3 n){
        Int3 nTemp = n.clone();
        nTemp.sub(minPrime);
        long diff = nTemp.toLong();

        long index = diff / 16;
        int position = (int)((diff/2) - (index*8));
        return ((bitArr.get(index) & bitMask[position]) != bitMask[position]);
    }

    //Cross out at bit place b
    protected void crossOut(long b){
        long index = b / 16;
        int position = (int)((b/2) - (index*8));
        bitArr.set(index, (byte)(bitArr.get(index) | bitMask[position]));
    }

    public Int3 getLastPrime(){
        Int3 p2 = maxPrime.clone();
        while (!p2.less(minPrime)){
            if (isPrime(p2)){
                return p2;
            }
            p2.sub(2);
        }
        return null;
    }
}
