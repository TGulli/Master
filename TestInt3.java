import java.math.BigInteger;
import java.util.Arrays;

class TestInt3{
    static BigInteger[] bigList = new BigInteger[50000000];
    static Int3[] int3List = new Int3[50000000];


    public static void main(String[] args) {
        long from = (long)Math.pow(2,32);
        BigInteger fromBig = BigInteger.TWO.pow(64);

        testMul(from, 25);
        testSqrt(fromBig, 25);
        testAddSmall(fromBig, 25);
        testAddBig(fromBig, 25);
        testSubSmall(fromBig, 25);
        testSubBig(fromBig, 25);
        testDiv(fromBig, 25);
        testModulo(fromBig, 25);
        testGreater(fromBig, 25);
        testEqual(fromBig, 25);
    }
    public static void initBigList(long from){

        for(int i = 0; i < bigList.length; i++){
            bigList[i] = BigInteger.valueOf(from);
            from++;
        }
    }

    public static void initIntList(long from){
        for(int i = 0; i < bigList.length; i++){
            int3List[i] = new Int3(from);
            from++;
        }
    }

    public static void initBigList(BigInteger from){
        BigInteger counter = from;

        for(int i = 0; i < bigList.length; i++){
            bigList[i] = counter;
            counter = counter.add(BigInteger.ONE);
        }
    }

    public static void initIntList(BigInteger from){
        Int3 counter = new Int3(from.toString());

        for(int i = 0; i < bigList.length; i++){
            int3List[i] = counter.clone();
            counter.addSmall(1);
        }
    }

    public static void testMul(long from, int nRuns){
        System.out.println("----- BigInteger -----");
        double[] time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initBigList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < bigList.length; i++){
                bigList[i] = bigList[i].multiply(bigList[i]);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }
        System.out.println("Time used to multiply: " + time[nRuns/2] + " ms.");
        System.out.println("result " + bigList[bigList.length-1]);


        System.out.println("----- Int3 -----");
        time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initIntList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < int3List.length; i++){
                int3List[i].mul(int3List[i]);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }

        System.out.println("Time used to multiply: " + time[nRuns/2] + " ms.");
        System.out.println("result " + int3List[int3List.length-1]);
    }

    public static void testSqrt(BigInteger from, int nRuns){
        System.out.println("----- BigInteger -----");
        double[] time = new double[nRuns];
        long result = 0;

        for (int n = 0; n < nRuns; n++){
            initBigList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < bigList.length; i++){
                result = bigList[i].sqrt().longValue();
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }
        System.out.println("Time used to take square root: " + time[nRuns/2] + " ms.");
        System.out.println("result " + result);

        System.out.println("----- Int3 -----");
        time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initIntList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < int3List.length; i++){
                result = int3List[i].sqrt(Long.MAX_VALUE);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }

        System.out.println("Time used to to take square root: " + time[nRuns/2] + " ms.");
        System.out.println("result " + result);
    }

    public static void testAddSmall(BigInteger from, int nRuns){
        System.out.println("----- BigInteger -----");
        double[] time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initBigList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < bigList.length; i++){
                bigList[i] = bigList[i].add(BigInteger.TWO);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }
        Arrays.sort(time);
        System.out.println("Time used to add 2: " + time[nRuns/2] + " ms.");
        System.out.println("result " + bigList[bigList.length-1]);


        System.out.println("----- Int3 -----");
        time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initIntList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < int3List.length; i++){
                int3List[i].addSmall(2);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }
        Arrays.sort(time);

        System.out.println("Time used to add 2: " + time[nRuns/2] + " ms.");
        System.out.println("result " + int3List[int3List.length-1]);
    }


    public static void testAddBig(BigInteger from, int nRuns){
        System.out.println("----- BigInteger -----");
        double[] time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initBigList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < bigList.length; i++){
                bigList[i] = bigList[i].add(bigList[i]);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }
        System.out.println("Time used to add big: " + time[nRuns/2] + " ms.");
        System.out.println("result " + bigList[bigList.length-1]);

        System.out.println("----- Int3 -----");
        time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initIntList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < int3List.length; i++){
                int3List[i].add(int3List[i]);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }

        System.out.println("Time used to add big: " + time[nRuns/2] + " ms.");
        System.out.println("result " + int3List[int3List.length-1]);
    }

    public static void testSubSmall(BigInteger from, int nRuns){
        System.out.println("----- BigInteger -----");
        double[] time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initBigList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < bigList.length; i++){
                bigList[i] = bigList[i].subtract(BigInteger.TWO);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }
        System.out.println("Time used to sub 2: " + time[nRuns/2] + " ms.");
        System.out.println("result " + bigList[bigList.length-1]);


        System.out.println("----- Int3 -----");
        time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initIntList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < int3List.length; i++){
                int3List[i].sub(2);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }

        System.out.println("Time used to sub 2: " + time[nRuns/2] + " ms.");
        System.out.println("result " + int3List[int3List.length-1]);
    }


    public static void testSubBig(BigInteger from, int nRuns){
        System.out.println("----- BigInteger -----");
        double[] time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initBigList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < bigList.length; i++){
                bigList[i] = bigList[i].subtract(bigList[i]);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }
        System.out.println("Time used to sub big: " + time[nRuns/2] + " ms.");
        System.out.println("result " + bigList[bigList.length-1]);


        System.out.println("----- Int3 -----");
        time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initIntList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < int3List.length; i++){
                int3List[i].sub(int3List[i]);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }

        System.out.println("Time used to sub big: " + time[nRuns/2] + " ms.");
        System.out.println("result " + int3List[int3List.length-1]);
    }

    public static void testDiv(BigInteger from, int nRuns){
        System.out.println("----- BigInteger -----");
        double[] time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initBigList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < bigList.length; i++){
                bigList[i] = bigList[i].divide(BigInteger.valueOf(i+1));
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }
        System.out.println("Time used to div: " + time[nRuns/2] + " ms.");
        System.out.println("result " + bigList[bigList.length-1]);


        System.out.println("----- Int3 -----");
        time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initIntList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < int3List.length; i++){
                int3List[i].div(i+1);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }

        System.out.println("Time used to div: " + time[nRuns/2] + " ms.");
        System.out.println("result " + int3List[int3List.length-1]);
    }

    public static void testModulo(BigInteger from, int nRuns){
        System.out.println("----- BigInteger -----");
        double[] time = new double[nRuns];
        long result = 0;

        for (int n = 0; n < nRuns; n++){
            initBigList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < bigList.length; i++){
                result = bigList[i].mod(BigInteger.valueOf(i+1)).longValue();
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }
        System.out.println("Time used to mod: " + time[nRuns/2] + " ms.");
        System.out.println("result " + result);


        System.out.println("----- Int3 -----");
        time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initIntList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < int3List.length; i++){
                result = int3List[i].modulo(i+1);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }

        System.out.println("Time used to mod: " + time[nRuns/2] + " ms.");
        System.out.println("result " + result);
    }

    public static void testGreater(BigInteger from, int nRuns){
        System.out.println("----- BigInteger -----");
        double[] time = new double[nRuns];
        boolean result = false;

        for (int n = 0; n < nRuns; n++){
            initBigList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < bigList.length; i++){
                result = (bigList[i].compareTo(bigList[i]) == 1);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }
        System.out.println("Time used to test greater: " + time[nRuns/2] + " ms.");
        System.out.println("result " + result);


        System.out.println("----- Int3 -----");
        time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initIntList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < int3List.length; i++){
                result = int3List[i].greater(int3List[i]);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }

        System.out.println("Time used to test greater: " + time[nRuns/2] + " ms.");
        System.out.println("result " + result);
    }

    public static void testEqual(BigInteger from, int nRuns){
        System.out.println("----- BigInteger -----");
        double[] time = new double[nRuns];
        boolean result = false;

        for (int n = 0; n < nRuns; n++){
            initBigList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < bigList.length; i++){
                result = (bigList[i].compareTo(bigList[i]) == 0);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }
        System.out.println("Time used to test equal: " + time[nRuns/2] + " ms.");
        System.out.println("result " + result);


        System.out.println("----- Int3 -----");
        time = new double[nRuns];

        for (int n = 0; n < nRuns; n++){
            initIntList(from);
            time[n] = System.nanoTime();
            for(int i = 0; i < int3List.length; i++){
                result = int3List[i].equal(int3List[i]);
            }
            time[n] = (System.nanoTime() - time[n]) / 1000000; //milliseconds
        }

        System.out.println("Time used to test equal: " + time[nRuns/2] + " ms.");
        System.out.println("result " + result);
    }
}
