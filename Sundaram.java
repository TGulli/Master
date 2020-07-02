import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.lang.Math;

class MainSundaram{
    public static void main(String[] args) {
        long maxPrime = (long)Math.pow(2,37)-1;
        Sundaram s = new Sundaram(maxPrime);

        double time = System.nanoTime();
        s.findPrimes();
        time = (System.nanoTime() - time) / 1000000; //milliseconds

        System.out.println("Time used to find primes: " + time + " ms.");
        s.print();
    }
}

public class Sundaram{
    BitArr bitArr; // normal bit table for eratosthenes
    byte[] bitMask = {(byte) 128, (byte) 64, (byte) 32, (byte) 16, (byte) 8, (byte) 4, (byte) 2, (byte) 1};
    long maxPrime; //Storste verdi for primtall, n
    final static int STARTPRIME = 3;

    Sundaram(long maxPrime){
        this.maxPrime = maxPrime;
        bitArr = new BitArr((long)(maxPrime/2/8)+1);
    }

    public void findPrimes(){
        long n = (long) (maxPrime/2)+1;
        long q = 4;
        long i, j;
        for (i = 1; q < n; i++){
            for (j = i; q < n; j++){
                crossOut(q);
                q = i + j + (2*i*j);
            }
            j = i;
            q = i + j + (2*i*j);
        }
    }

    // prints some of the biggest primes
    public void print(){
        long n = (long) (maxPrime/2)+1;
        System.out.println("Primes:");

        for (long i = n-100; i < n; i++){
            if (!crossedOut(i)){
                long p = (2*i)+1;
                System.out.print(p + ", ");
            }
        }
        System.out.println();
    }

    protected void crossOut(long n){
        long index = n / 8;
        int position = (int)(n - (index*8));

        bitArr.set(index, (byte)(bitArr.get(index) | bitMask[position]));
    }

    protected boolean crossedOut(long n){
        long index = n / 8;
        int position = (int)(n - (index*8));
        return !((bitArr.get(index) & bitMask[position]) != bitMask[position]);
    }
}
