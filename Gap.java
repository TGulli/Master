class Gap{
    Int3 first;
    int gapSize;
    double time;

    Gap(Int3 first, int gapSize, double time){
        this.first = first;
        this.gapSize = gapSize;
        this.time = (System.nanoTime() - time) / 1000000;
    }

    public String toString(){
        return ("Found new gap with size " + gapSize + ", after prime " + first.toString() + ". Used " + time + " ms.");
    }
}

class GapSave{
    Gap[] gaps;
    int saveAtIndex = 0;
    int biggestGapSize = 0;

    GapSave(int nToSave){
        gaps = new Gap[nToSave];
    }

    public void save(Gap g){
        gaps[saveAtIndex] = g;
        saveAtIndex++;
        if (g.gapSize > biggestGapSize){
            biggestGapSize = g.gapSize;
        }
    }

    public void resetSaveIndex(){
        saveAtIndex = 0;
    }

    public Gap read(int i){
        return gaps[i];
    }

    public int getLength(){
        return saveAtIndex;
    }

    public int getBiggestGap(){
        return biggestGapSize;
    }

}
