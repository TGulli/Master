import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.lang.Math;

public class Main{
    public static void main(String[] args) {
        // Finds prime numbers with eratosthenes up to maxPrime
        long maxPrime = (long)Math.pow(2,20) - 1;

        // PrimeCross pp = new PrimeCross(maxPrime); //With cross 3, 5, 7
        Prime pp = new PrimeParallel(maxPrime);

        double time = System.nanoTime();
        pp.findPrimes();
        time = (System.nanoTime() - time) / 1000000; //milliseconds

        System.out.println("Time used to find primes: " + time + " ms.");


        pp.print(); // Prints some of the last primes
        // pp.write("eratosthenes.txt");

        System.out.println("\n\n---------------------------------------");
        System.out.println("Gaps:\n");

        findGapsWithEratosthenes(pp);
        // findPrimesDivision(pp);
        // findGapDivision(pp);
    }

    public static void findGapsWithEratosthenes(Prime pp){
        GapFinderEratosthenes ge = new GapFinderEratosthenes(pp);
        // ge.findGaps(new Int3(3), 2, 10);
        // ge.findGaps(new Int3(1844,67440737,9551629), 2, 27); // 64 bit
        ge.findGaps(new Int3(118059,16207174,11303423), 2, 100); // 70 bit
    }

    public static void findPrimesDivision(Prime pp){
        PrimeDivision b = new PrimeDivision(pp);
        double time = System.nanoTime();
        b.findPrimes(new Int3(0,0,1024), 100, 2);
        time = (System.nanoTime() - time) / 1000000; //milliseconds
        b.printPrimes();
        System.out.printf("Time to find primes with division: %.4f ms.%n", time);
    }

    public static void findGapDivision(Prime pp){
        GapFinder g = new GapFinder(pp);
        double time = System.nanoTime();
        g.findGaps(new Int3(0,0,0), 2, 5);
        time = (System.nanoTime() - time) / 1000000; //milliseconds
        System.out.printf("Gap time: %.4f ms.%n", time);
    }
}
