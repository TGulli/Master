//Class that can have a big array of bytes

public class BitArr{
    final int MAXARRAYSIZE = Integer.MAX_VALUE-5; // Maximum length of a array
    byte[][] bitArr;
    long size;

    // The constructor finds out how many arrays of bytes, and how big they have
    // to be, depending on the size.
    BitArr(long size){
        this.size = size;
        long localSize = size;
        int maxOuterArraySize = 1;


        while (localSize > MAXARRAYSIZE){
            localSize -= MAXARRAYSIZE;
            maxOuterArraySize++;
        }

        localSize = size;
        bitArr = new byte[maxOuterArraySize][];

        for (int i = 0; i < MAXARRAYSIZE; i++){
            if (localSize < MAXARRAYSIZE){

                bitArr[i] = new byte[(int)localSize];
                break;
            }
            bitArr[i] = new byte[(int)MAXARRAYSIZE];
            localSize -= MAXARRAYSIZE;
        }
    }

    // Returns the length of the array
    public long getLength(){
        return size;
    }

    // Stores a byte at a index
    public void set(long index, byte value){
        long localIndex = index;

        for (int i = 0; i < MAXARRAYSIZE; i++){
            if (localIndex < MAXARRAYSIZE){

                bitArr[i][(int)localIndex] = value;
                break;
            }
            localIndex -= MAXARRAYSIZE;
        }
    }

    // Returns the byte on a given index
    public byte get(long index){
        if (index < 0){
            System.out.println("Negative index: " + index);
            System.exit(0);
        }
        long localIndex = index;

        for (int i = 0; i < MAXARRAYSIZE; i++){
            if (localIndex < MAXARRAYSIZE){
                return bitArr[i][(int)localIndex];
            }
            localIndex -= MAXARRAYSIZE;
        }
        return 0;
    }
}
