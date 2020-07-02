import java.io.PrintWriter;
import java.io.File;
import java.io.FileOutputStream;

public class Prime{
    BitArr bitArr; // bit table for eratosthenes
    byte[] bitMask = {(byte) 128, (byte) 64, (byte) 32, (byte) 16, (byte) 8, (byte) 4, (byte) 2, (byte) 1};
    long maxPrime; //Storste verdi for primtall, n
    final static int STARTPRIME = 3;
    final static int MAXSMALLPRIME = 10000;
    int[] smallPrimes = null;
    long lastSqrt = -1;


    Prime(long maxPrime){
        this.maxPrime = maxPrime;
        System.out.println("maxPrime" + maxPrime);
        bitArr = new BitArr((maxPrime/16)+1);
    }

    public long getMaxPrime(){
        return maxPrime;
    }

    // finds primes with eratosthenes
    public void findPrimes(){
        crossOut(1); //Vi vet at 1 ikke er primtall...
        long product = STARTPRIME*STARTPRIME;
        for (long k = STARTPRIME; product <= maxPrime; k +=2, product = k*k){
            if(isPrime(k)){
                long crossOutNumber = product;
                long k2 = k*2;

                while (crossOutNumber <= maxPrime){
                    crossOut(crossOutNumber);
                    crossOutNumber += k2;
                }
            }
        }
    }

    // Checks for a number has factors or not. If not so, it is a prime
    public boolean hasFactors (Int3 number){
        if (number.dividable(2)){
            return true;
        }
        for (int i = 0; i < smallPrimes.length; i++){
            if (number.dividable(smallPrimes[i])){
               if (number.equal(new Int3(smallPrimes[i]))){
                   return false;
               } else{
                   return true;
               }
           }
        }
        long n = getLastSmall();
        if (lastSqrt == -1){
            Int3 tempNumber = number.clone();
            if (tempNumber.isZero()){
                tempNumber.addSmall(1);
            }
            if (tempNumber.greater(Long.MAX_VALUE)){
                lastSqrt = Long.MAX_VALUE;
            } else{
                lastSqrt = tempNumber.toLong();
            }
        }
        long sqrtNumber = number.sqrt(lastSqrt);
        lastSqrt = sqrtNumber;

        while (n <= (sqrtNumber+1)){
            if (isPrime(n) && (number.dividable(n))){
                return true;
            }
            n +=2;
        }
        return false;
    }

    public long getLastSmall(){
        if (smallPrimes == null || smallPrimes.length <= 0){
            return STARTPRIME;
        }
        return smallPrimes[smallPrimes.length-1];
    }

    // This function must be called before initSmallPrimes, to fill smallPrimes list
    public void initSmallPrimes(){
        int nSmallPrimes = 0;
        long max = MAXSMALLPRIME;
        if (max > maxPrime){
            max = maxPrime;
        }
        for (int n = STARTPRIME;n <= max; n+=2){
            if (isPrime(n)){
                nSmallPrimes++;
            }
        }
        smallPrimes = new int[nSmallPrimes];
        for (int n = STARTPRIME, i = 0; n <= max && i < nSmallPrimes; n+=2){
            if (isPrime(n)){
                smallPrimes[i] = n;
                i++;
            }
        }
    }

    // prints some of the biggest primes
    public void print(){
        System.out.println("Primes:");

        long n = maxPrime;
        int counter = 0;
        while (n <= maxPrime && counter < 5){
            if (isPrime(n)){
                System.out.print(n + ", ");
                counter++;
            }
            n -=2;
        }
    }

    public void printAll(){
        System.out.println("Primes:");

        long n = STARTPRIME;
        while (n < maxPrime){
            if (isPrime(n)){
                System.out.print(n + ", ");
            }
            n +=2;
        }
        System.out.println();
    }

    public void write(String filename){
        long n = 3;
        PrintWriter writer = null;
        try{
            writer = new PrintWriter(new FileOutputStream(filename), true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        writer.write("Primes:");
        writer.println();

        while (n < maxPrime){
            if (isPrime(n)){
                writer.println(n);
            }
            n +=2;
        }
        writer.println();
        writer.close();
    }

    // Returns the biggest prime from bitArr
    public long getBiggestPrime(){
        long n = maxPrime;

        for (long i = n; i > 0; i-=2){
            if (isPrime(i)){
                return i;
            }
        }
        return -1;
    }

    public long getPrevPrime(long n){
        if (n % 2 == 0){
            n--;
        }
        for (long i = n-2; i > 0; i-=2){
            if (isPrime(i)){
                return i;
            }
        }
        return -1;
    }

    public long getNextPrime(long n){
        if (n % 2 == 0){
            n++;
        }
        for (long i = n+2; i <= maxPrime; i+=2){
            if (isPrime(i)){
                return i;
            }
        }
        return -1;
    }

    public boolean isPrime(long n){
        long index = n / 16;
        int position = (int)((n/2) - (index*8));
        return ((bitArr.get(index) & bitMask[position]) != bitMask[position]);
    }

    protected void crossOut(long n){
        long index = n / 16;
        int position = (int)((n/2) - (index*8));

        bitArr.set(index, (byte)(bitArr.get(index) | bitMask[position]));
    }
}
