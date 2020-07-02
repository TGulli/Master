import java.io.File;
import java.io.PrintWriter;
import java.io.FileWriter;

public class GapFinderEratosthenes{
    Prime p;
    GapSave gs;
    double totalTime;
    final static String FILENAMEGAP = "gapE.txt";

    GapFinderEratosthenes(Prime p){
        this.p = p;

        File a = new File(FILENAMEGAP);
        a.delete();
    }

    public void findGaps(Int3 start, int initGapSize, int nGaps){
        gs = new GapSave(nGaps);

        if (start.less(3)){
            start = new Int3(3);
        } else if (start.dividable(2)){
            start.addSmall(1);
        }
        Int3 maxPrime = new Int3(p.getMaxPrime());
        maxPrime.mul(maxPrime);
        totalTime = System.nanoTime();

        boolean retNextTime = false;
        boolean firstRun = true;
        while(nGaps > 0 && start.less(maxPrime)){
            Int3 end = start.clone();
            end.add(10000000000L);
            if (end.greater(maxPrime)){
                end = maxPrime.clone();
                if(retNextTime){
                    return;
                }
                retNextTime = true;
            }

            EratosthenesGap erat = new EratosthenesGapParallel(p);
            erat.findPrimes(start, end, !firstRun);

            if (gs == null || gs.getBiggestGap() == 0){
                nGaps -= localFindGaps(start, initGapSize, nGaps, erat);
            } else{
                nGaps -= localFindGaps(start, gs.getBiggestGap(), nGaps, erat);
            }
            firstRun = false;
            start = erat.getLastPrime();
            System.out.println("Find primes to " + start);
            start.add(2);
        }
        totalTime = (System.nanoTime() - totalTime) / 1000000; //milliseconds
        System.out.printf("Time total: %.4f ms.%n", totalTime);

    }

    public int localFindGaps(Int3 start, int initGapSize, int nGaps, EratosthenesGap erat){
        double time = System.nanoTime();
        int gapsFound = 0;

        //Gapsize muse be an even number, and minimum 2
        if (initGapSize % 2 != 0){
            initGapSize++;
        } else if (initGapSize < 2){
            initGapSize = 2;
        }
        if (start.dividable(2) || start.isZero()){
            start.addSmall(1);
        }

        Int3 startPrime;
        if (erat.isPrime(start)){
            startPrime = start.clone();
        } else{
            startPrime = erat.findNextPrime(start);
        }
        int biggestGap = initGapSize;

        while (gapsFound < nGaps){
            Int3 p = startPrime.clone();
            p.addSmall(biggestGap);

            if (p.greater(erat.maxPrime)){
                break;
            }
            if (erat.isPrime(p)){
                startPrime = p;
                continue;
            }

            Int3 p2 = erat.findPrevPrime(p);

            if (p2 != null && !p2.equal(startPrime)){
                startPrime = p2;
                continue;
            }
            p = erat.findNextPrime(p);

            if (p == null){
                break;
            }

            // New gap found
            Int3 temp3 = p.clone();
            temp3.sub(startPrime);
            biggestGap = temp3.toInt();
            System.out.printf("New gap found with size %4d, from prime %s%n", biggestGap, startPrime.toString());
            gs.save(new Gap(startPrime, biggestGap, totalTime));
            writeLastToFile();
            startPrime = p.clone();
            gapsFound++;
        }
        return gapsFound;
    }

    private void writeLastToFile(){
        PrintWriter writer;
        try{
            writer = new PrintWriter(new FileWriter(FILENAMEGAP, true), true);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        writer.println(gs.read(gs.getLength()-1).toString());
        writer.close();
    }
}
