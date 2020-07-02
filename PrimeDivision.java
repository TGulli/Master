import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileOutputStream;

/*
* This class is for calculating primes with divison method
*/

class PrimeDivision{
    Prime primeClass;
    Int3 maxPrime;
    long smallMaxPrime, startPrime;
    Int3[] primesFound;
    final static String FILENAME = "BigPrime.txt";
    PrintWriter gapWriter;

    // takes a object of Prime and maxPrime, so it can calcualte bigger primes
    PrimeDivision(Prime p){
        this.primeClass = p;
        this.maxPrime = new Int3(p.maxPrime);
        this.maxPrime.mul(this.maxPrime);
        this.primeClass.initSmallPrimes();
        this.smallMaxPrime = p.maxPrime;
        this.startPrime = p.STARTPRIME;
    }

    public boolean hasFactors(Int3 number){
        return primeClass.hasFactors(number);
    }

    public boolean isPrime(long number){
        return primeClass.isPrime(number);
    }

    public Int3 findNextPrime(Int3 p){
        Int3 p2 = p.clone();
        p2.addSmall(2);

        while (p2.less(maxPrime)){
            if (!hasFactors(p2)){
                return p2;
            }
            p2.addSmall(2);
        }
        return null;
    }

    public Int3 findPrevPrime(Int3 startPrime){
        Int3 p = startPrime.clone();
        p.sub(2);
        while (true){
            if (!hasFactors(p)){
                return p;
            }
            p.sub(2);
        }
        // return null;
    }

    public void findPrimes(int nPrimesToFind, int step){
        findPrimes(null, nPrimesToFind, step);
    }

    // find bigger primes with factorizing
    public void findPrimes(Int3 start, int nPrimesToFind, int step){
        double time = System.nanoTime();
        if (start == null){
            start = new Int3(primeClass.getBiggestPrime()); // current biggest prime from eratosthenes
        } else if (start.dividable(2)){ // Start must be an odd number
            start.addSmall(1);
        }
        if (step % 2 != 0){
            System.out.println("WARNING: Step must be a even number.");
            System.out.println("Changed step to: " + step);
            step++;
        }

        if (step != 2){
            nPrimesToFind /= 2;
        }
        primesFound = new Int3[nPrimesToFind];

        int nPrimesFound = 0;
        Int3 currentPrime = start.clone();

        while((!currentPrime.greater(maxPrime))){

            currentPrime.add(step);
            if (!hasFactors(currentPrime)){ // prime found
                primesFound[nPrimesFound] = currentPrime.clone();
                writeOnePrime(primesFound[nPrimesFound], time);
                nPrimesFound++;
                if (nPrimesFound >= nPrimesToFind){
                    break;
                }
            }
        }
    }

    // Prints all the primes with the distance for the primes that BigPrime has found
    public void printPrimes(){
        System.out.println("\nPrimes: \n");
        for (int i = 0; i < primesFound.length; i++){
            System.out.println("Prime: " + primesFound[i]);
        }
    }

    public void writeOnePrime(Int3 prime, double time){
        time = (System.nanoTime() - time) / 1000000; //milliseconds

        if (gapWriter == null){
            deleteFile();
        }
        try{
            gapWriter = new PrintWriter(new FileWriter(FILENAME, true), true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        gapWriter.printf("Prime: %s used time %.4f ms.%n", prime.toString(), time);
        gapWriter.close();
    }

    private void deleteFile(){
        File a = new File(FILENAME);
        a.delete();
    }

    public void writePrimesToFile(String filename, double time){
        if (primesFound.length < 1){
            return;
        }
        PrintWriter writer = null;
        try{
            writer = new PrintWriter(new FileOutputStream(filename), true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        writer.println("\nBig primes: \n");
        for (int i = 0; i < primesFound.length; i++){
            writer.println("Prime: " + primesFound[i]);
        }

        writer.printf("%n%nTime used: %.4f ms.%n", time);
        writer.close();
    }
}
