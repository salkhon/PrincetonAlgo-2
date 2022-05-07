package week2;

// a priority queue with the ability to refer to keys with an index.
// exactly as needed for Prims eager MST algorithm
public class IndexedDynamicMinPQ<K extends Comparable<K>> {
    private K[] indexKeys;
    private int[] indexHeapPositions;
    private int[] indexBinaryHeap;

    private int size;

    @SuppressWarnings("unchecked")
    public IndexedDynamicMinPQ(int n) {
        this.indexKeys = (K[]) new Comparable[n];
        this.indexBinaryHeap = new int[n]; // start from 1, such that child of k is 2k and 2k + 1
        this.indexHeapPositions = new int[n];
        this.size = 0;
    }

    public int delMinKeyIndex() {
        int min = -1;
        if (this.size > 0) {
            min = this.indexBinaryHeap[1];
            this.indexKeys[min] = null;
            this.indexHeapPositions[min] = -1;
            this.indexBinaryHeap[1] = this.indexBinaryHeap[this.size--];
            this.indexHeapPositions[this.indexBinaryHeap[1]] = 1;
            // size will control the heap boundaries
            sinkDown(1);
        }
        return min;
    }

    public int minKeyIndex() {
        return this.size > 0 ?
                this.indexBinaryHeap[1] : -1;
    }

    public void insert(int index, K key) {
        if (index < this.indexKeys.length) {
            this.indexKeys[index] = key;
            this.indexBinaryHeap[++this.size] = index;
            this.indexHeapPositions[index] = this.size;
            swimUp(this.size);
        }
    }

    public boolean contains(int indexKey) {
        return this.indexKeys[indexKey] != null;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public void decreaseKeyPriority(int index, K key) {
        if (index < this.indexKeys.length) {
            this.indexKeys[index] = key;
            swimUp(this.indexHeapPositions[index]);
        }
    }

    private boolean less(int indexKey1, int indexKey2) {
        return this.indexKeys[indexKey1].compareTo(this.indexKeys[indexKey2]) < 0;
    }

    private void swimUp(int heapPosition) {
        int currentHP = heapPosition;
        for (int parentHP = currentHP / 2;
             parentHP > 0 && less(this.indexBinaryHeap[currentHP], this.indexBinaryHeap[parentHP]);
             currentHP = parentHP, parentHP = currentHP / 2) {
            swapHeapPosition(currentHP, parentHP);
        }
    }

    private void sinkDown(int heapPosition) {
        int currentHP = heapPosition;
        for (int childLHP = currentHP * 2, childRHP = childLHP + 1, leastChildHP = childLHP;
             childRHP <= this.size || childLHP <= this.size;
             currentHP = leastChildHP, childLHP = currentHP * 2, childRHP = childLHP + 1) {
            leastChildHP = childLHP;
            if (childRHP <= this.size &&
                    less(this.indexBinaryHeap[childRHP], this.indexBinaryHeap[childLHP])) {
                leastChildHP = childRHP;
            }
            swapHeapPosition(currentHP, leastChildHP);
        }

    }

    private void swapHeapPosition(int hp1, int hp2) {
        this.indexBinaryHeap[hp1] ^= this.indexBinaryHeap[hp2];
        this.indexBinaryHeap[hp2] ^= this.indexBinaryHeap[hp1];
        this.indexBinaryHeap[hp1] ^= this.indexBinaryHeap[hp2];

        this.indexHeapPositions[this.indexBinaryHeap[hp1]] = hp1;
        this.indexHeapPositions[this.indexBinaryHeap[hp2]] = hp2;
    }
}
