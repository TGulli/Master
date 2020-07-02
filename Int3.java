class Int3 {
    int [] rep = new int [3];// rep[3] is  the 6 lowest order digits
    int max = 100000000,
    mask = max-1;;

    Int3 () {}

    Int3 (String str) {
        if (str.length() > 24){
            System.out.println("ERROR: Max number: 99999999 99999999 99999999");
            return;
        }
        while (str.length() < 24){
            str = "0" + str;
        }
        rep[0] = Integer.parseInt(str.substring(0,8));
        rep[1] = Integer.parseInt(str.substring(8,16));
        rep[2] = Integer.parseInt(str.substring(16,24));
    }

    Int3 (int a, int b, int c) {
        rep[0] = a;
        rep[1] = b;
        rep[2] = c;
    }

    Int3 (int[] number) {
        rep = number;
    }

    Int3 (long init) {
        long temp = init;
        int t;

        for (int i = 2; i >=0; i--){
            rep[i] = (int) (temp % max);
            temp = temp/max;
        }
    }

    public void mul(Int3 n){
        int[] rep2 = n.getValues();
        long temp, rest;
        int[] res = new int[3];

        temp = (long)rep[2] * (long)rep2[2];
        res[2] = (int)(temp%max);
        rest = temp/max;

        temp = ((long)rep[1] * (long)rep2[2]) + rest;
        rest = temp/max;
        res[1] = (int)(temp%max);

        temp = (long)rep[0] * (long)rep2[2] + rest;
        res[0] = (int)(temp%max);

        temp = (long)rep[2] * (long)rep2[1] + res[1];
        res[1] = (int)(temp%max);
        rest = temp/max;

        temp = ((long)rep[1] * (long)rep2[1]) + res[0] + rest;
        res[0] = (int)(temp%max);

        temp = (long)rep[2] * (long)rep2[0] + res[0];
        res[0] = (int)(temp%max);

        rep[0] = res[0];
        rep[1] = res[1];
        rep[2] = res[2];
    }

    public long sqrt(long g){
        Int3 x = clone();
        x.div(g);
        long val = (x.toLong() + g)/2;
        while (val-x.toLong() > 1){
            x = clone();
            x.div(val);
            val = (x.toLong() + val)/2;
        }
        return val;
    }

    public long sqrt(Int3 g2){
        long g;
        if (g2.greater(new Int3(Long.MAX_VALUE))){
            g = Long.MAX_VALUE;
        } else{
            g = g2.toLong();
        }
        Int3 x = clone();
        x.div(g);
        long val = (x.toLong() + g)/2;

        while (val-x.toLong() > 1){
            x = clone();
            x.div(val);
            val = (x.toLong() + val)/2;
        }
        return val;
    }

    // Assume that addend is never bigger than 99999999
    public void addSmall (int addend) {
        rep[2] += addend;
        if (rep[2] < max){
            return;
        }
        rep[2] -= max;
        rep[1]++;
        if (rep[1] > max){
            rep[0]++;
            rep[1] -= max;
        }
        if (rep[0] >= max){
            System.out.printf("ERROR: %s + %d will overflow.%n", toString(), addend);
        }
    } // end add

    public void add(long addend){
        add(new Int3(addend));
    }

    public void add (Int3 addend) {
        int[] addendArr = addend.getValues();
        int sum[] = new int[rep.length];
        int rest = 0;

        sum[2] = rep[2] + addendArr[2] + rest;
        rest = 0;
        while (sum[2] > mask){
            sum[2]-=max;
            rest++;
        }
        rep[2] = sum[2];

        sum[1] = rep[1] + addendArr[1] + rest;
        rest = 0;
        while (sum[1] > mask){
            sum[1]-=max;
            rest++;
        }
        rep[1] = sum[1];

        sum[0] = rep[0] + addendArr[0] + rest;
        rest = 0;
        if (sum[0] > mask){
            System.out.printf("ERROR: %s + %s will overflow.%n", toString(), addend.toString());
            return;
        }
        rep[0] = sum[0];

    } // end add

    public void sub(int n){
        rep[2]-=n;
        if (rep[2] < 0){
            rep[2]+= max;
            if (rep[1] > 0){
                rep[1]--;
            } else if (rep[2] > 0){
                rep[1] = mask;
                rep[2]--;
            } else{
                System.out.println("Int3 class can not handle negative numbers.");
                System.out.printf("%s - %d will be negative..%n", toString(), n);
            }
        }
    }

    public void sub(Int3 n) {
        int[] repN = n.getValues();

        if (repN[0] > rep[0] || (repN[1] > rep[1] && repN[0] > rep[0]) ||
                    (repN[2] > rep[2] && (repN[1] > rep[1] && repN[0] > rep[0]))){

            System.out.println("Int3 class can not handle negative numbers.");
            System.out.printf("%s - %s will be negative..%n", toString(), n.toString());
            return;
        }

        rep[0] -= repN[0];
        rep[1] -= repN[1];
        rep[2] -= repN[2];

        if (rep[2] < 0){
            rep[2]+=max;
            rep[1]--;
        }
        if (rep[1] < 0){
            rep[1]+=max;
            rep[0]--;
        }
    }

    public void div(long divisor ) {
        long mente =0;
        long dividend = mente*max + rep[0];
        rep[0] = (int)(dividend/divisor);
        mente  = (dividend%divisor);

        dividend = mente*max + rep[1];
        rep[1] = (int)(dividend/divisor);
        mente  = (dividend%divisor);

        dividend = mente*max + rep[2];
        rep[2] = (int)(dividend/divisor);
    }

    public long modulo(long x) {
        return (((((((long)rep[0])%x)*max) + (long)rep[1])%x)*max + (long)rep[2])%x;
    }

    public boolean dividable (long x){
        return  modulo(x) == 0;
    }

    public boolean isZero (){
        return (rep[0] == 0 && rep[1] == 0 && rep[2] == 0);
    }

    public boolean isOne (){
        return (rep[0] == 0 && rep[1] == 0 && rep[2] == 1);
    }

    public boolean greater (Int3 n){
        int[] repN = n.getValues();
        if (rep[0] > repN[0]){
            return true;
        } else if (rep[0] < repN[0]){
            return false;
        } else if (rep[1] > repN[1]){
            return true;
        } else if (rep[1] < repN[1]){
            return false;
        } else if (rep[2] > repN[2]){
            return true;
        } else if (rep[2] < repN[2]){
            return false;
        }
        return false;
    }

    // Faster greater for small values
    public boolean greater (long n){
        if (max < n){
            return (rep[2] > n || rep[1] > 1 || rep[0] > 1);
        }
        return greater(new Int3(n));
    }

    public boolean less (Int3 n){
        int[] repN = n.getValues();
        if (rep[0] < repN[0]){
            return true;
        } else if (rep[0] > repN[0]){
            return false;
        } else if (rep[1] < repN[1]){
            return true;
        } else if (rep[1] > repN[1]){
            return false;
        } else if (rep[2] < repN[2]){
            return true;
        } else if (rep[2] > repN[2]){
            return false;
        }
        return false;
    }

    // Faster greater for small values
    public boolean less (long n){
        if (max < n){
            return (rep[2] < n && rep[1] < 1 && rep[0] < 1);
        }
        return less(new Int3(n));
    }

    public boolean equal (Int3 n){
        int[] repN = n.getValues();
        if (rep[0] != repN[0]){
            return false;
        } else if (rep[1] != repN[1]){
            return false;
        } else if (rep[2] != repN[2]){
            return false;
        }
        return true;
    }

    public boolean equal (long n){
        if (n < max){
            return (n == rep[2]) && (rep[1] == 0) && (rep[0] == 0);
        } else{
            return equal(new Int3(n));
        }
    }

    public int[] getValues() {
        return rep;
    }

    public Int3 clone() {
        return (new Int3(rep[0], rep[1], rep[2]));
    }

    public int toInt() {
        if (rep[0] == 0 && rep[1] == 0){
            return rep[2];
        } else if (rep[0] == 0 && (rep[1] < 21 || (rep[1] == 21 && rep[2] <= 47483647))) {
            return rep[2] + rep[1]*max;
        }
        System.out.println("Int3 number is to big to represent as one int: " + toString());
        return -1;
    }

    public long toLong() {
        long ret = -1;
        if (rep[0] == 0){
            return (long)rep[2] + ((long)rep[1]*(long)max);
        } else if((rep[0] < 922) ||
                        (rep[0] == 922 && rep[1] < 33720368) ||
                        (rep[0] == 922 && rep[1] == 33720368 && rep[2] <= 54775807)){
            return (long)rep[2] + (long)rep[1]*(long)max + (long)rep[0]*(long)max*(long)max;
        }

        System.out.println("Int3 number is to big to represent as one long: " + toString());
        return -1;
    }

    public String toString(){
        String ret ="";
        for (int i = 0; i < 3 ; i++){
            ret += " " + format(rep[i]);
        }
        return ret;
    }// end print

    public String format(int rep) {
        return String.format("%08d", rep);
    }

    // Returns number of bits used to represent the number
    public int nBitUsed(){
        int counter = (32 - Integer.numberOfLeadingZeros(rep[0]));
        counter += (32 - Integer.numberOfLeadingZeros(rep[1]));
        counter += (32 - Integer.numberOfLeadingZeros(rep[2]));
        return counter;
    }
} // end class Int4
