import java.io.File;
import java.lang.Math;

public class PrimeCross{
    BitArr bitArr; // bit table for eratosthenes
    byte[] bitMask = {(byte) 128, (byte) 64, (byte) 32, (byte) 16, (byte) 8, (byte) 4, (byte) 2, (byte) 1};
    long maxPrime; //Storste verdi for primtall, n
    final static int STARTPRIME = 9;
    int[] smallPrimes = null;
    long lastSqrt = -1;


    PrimeCross(long maxPrime){
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
        cross357();

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

    public boolean hasFactorsTestLong(long number){
       if (number % 2 == 0){
           return true;
       }
       for (int i = 0; i < smallPrimes.length; i++){
           if (number % smallPrimes[i] == 0){
              if (number  == smallPrimes[i]){
                  return false;
              } else{
                  return true;
              }
          }
       }
       long n = getLastSmall();
       if (lastSqrt == -1){
           long tempNumber = number;
           if (tempNumber == 0){
               tempNumber++;
           }
           if (tempNumber > Long.MAX_VALUE){
               lastSqrt = Long.MAX_VALUE;
           } else{
               lastSqrt = tempNumber;
           }
       }
       long sqrtNumber = (long)Math.sqrt(number);
       lastSqrt = sqrtNumber;

       while (n <= (sqrtNumber+1)){
           if (isPrime(n) && (number % n == 0)){
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
        for (long i = n+2; i > maxPrime; i+=2){
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

    protected void cross357(){
        //0x49 = 01001001, 0x24= 00100100, 0x92 = 10010010.
        byte[] crossOut3 = new byte[] {(byte)0x49, (byte)0x24, (byte)0x92};
        // 0x21 = 00100001, 0x8 = 00001000, 0x42 = 01000010, 0x10 = 00010000, 0x84 = 10000100
        byte[] crossOut5 = new byte[] {(byte)0x21, (byte)0x8, (byte)0x42, (byte)0x10, (byte)0x84};
        // 00010000 00100000 01000000 10000001 00000010 00000100 00001000
        byte[] crossOut7 = new byte[] {(byte)0x10, (byte)0x20, (byte)0x40, (byte)0x81, (byte)0x2, (byte)0x4, (byte)0x8};

        for(long i = 0; i < bitArr.getLength(); i++){
            bitArr.set(i, (byte) (crossOut3[(int)(i%3)] | crossOut5[(int)(i%5)] | crossOut7[(int)(i%7)]));
        }
        // 10001010 = 0x8A
        bitArr.set(0, (byte) 0x8A);
    }
}
