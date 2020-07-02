import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;


class GapFinder extends PrimeDivision{
    GapSave gs;
    final static String FILENAMEGAP = "PrimeDivisionGaps.txt";

    GapFinder(Prime p){
        super(p);
        File a = new File(FILENAMEGAP);
        a.delete();
    }

    public void findGaps(Int3 start, int initGapSize, int nGaps){
        double time = System.nanoTime();
        gs = new GapSave(nGaps);
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
        if (!hasFactors(start)){
            startPrime = start.clone();
        } else{
            startPrime = findNextPrime(start);
        }
        int biggestGap = initGapSize;

        try{
            while (gapsFound < nGaps){
                Int3 p = startPrime.clone();
                p.addSmall(biggestGap);

                if (!hasFactors(p)){
                    startPrime = p;
                    continue;
                }
                Int3 p2 = findPrevPrime(p);
                if (!p2.equal(startPrime)){
                    startPrime = p2;
                    continue;
                }
                p = findNextPrime(p);

                if (p == null){
                    break;
                }

                // New gap found
                Int3 temp3 = p.clone();
                temp3.sub(startPrime);
                biggestGap = temp3.toInt();
                System.out.printf("New gap found with size %4d, from prime %s%n", biggestGap, startPrime.toString());
                gs.save(new Gap(startPrime, biggestGap, time));
                writeToFile();
                startPrime = p.clone();
                gapsFound++;
            }
        } catch(ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR: " + e);
            System.out.println("Hint: Trying to find more gaps from bigger primes which is not found?");
            return;
        }
    }

    protected void writeToFile(){
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
