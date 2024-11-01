/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;


public class Percolation {
    private static final int BOTTOMCONTACTMASK = 1;
    private static final int TOPCONTACTMASK = 2;
    private static final int OPENMASK = 4;
    private static final int PERCOLATIONMASK = 7;

    private int size;
    private boolean percolates = false;

    private class WeightedQuickUnion {
        private int[] ids;
        private byte[] metadata;
        private int[] rank;
        private int openCount;

        public WeightedQuickUnion() {
            openCount = 0;
            ids = new int[size * size];
            rank = new int[size * size];
            metadata = new byte[size * size];
            for (int i = 0; i < size * size; i++) {
                int row = i / size;
                ids[i] = i;
                if (row == 0) metadata[i] = (byte) (metadata[i] | TOPCONTACTMASK);
                if (row == size - 1) metadata[i] = (byte) (metadata[i] | BOTTOMCONTACTMASK);
            }
        }

        public int find(int i) {
            while (i != ids[i]) {
                i = ids[i];
            }
            return i;
        }

        public void union(int p, int q) {
            if (p >= size * size + 2 || q >= size * size + 2) {
                System.out.println("Unable to connect requested numbers");
                return;
            }

            int i = find(p);
            int j = find(q);

            if (i == j) return;

            int newMetadata = metadata[i] | metadata[j];

            if (rank[i] < rank[j]) {
                ids[i] = j;
                metadata[j] = (byte) newMetadata;
            }

            else if (rank[i] > rank[j]) {
                ids[j] = i;
                metadata[i] = (byte) newMetadata;
            }
            else {
                ids[i] = j;
                metadata[j] = (byte) newMetadata;
                rank[j] += 1;
            }
        }

    }

    private WeightedQuickUnion engine;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();

        size = n;
        engine = new WeightedQuickUnion();
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) throw new IllegalArgumentException();

        int idx = (row - 1) * size + col - 1;

        if (!isOpen(row, col)) {
            engine.metadata[idx] = (byte) (engine.metadata[idx] ^ (1 << 2));
            engine.openCount += 1;
        }

        if (row - 1 > 0 && isOpen(row - 1, col)) {
            engine.union(idx, idx - size);
        }
        if (row - 1 < size - 1 && isOpen(row + 1, col)) {
            engine.union(idx, idx + size);
        }

        if (col - 1 > 0 && isOpen(row, col - 1)) {
            engine.union(idx, idx - 1);
        }

        if (col - 1 < size - 1 && isOpen(row, col + 1)) {
            engine.union(idx, idx + 1);
        }

        int root = engine.find(idx);

        if ((engine.metadata[root] & PERCOLATIONMASK) == PERCOLATIONMASK) {
            percolates = true;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) throw new IllegalArgumentException();
        int data = engine.metadata[size * (row - 1) + col - 1] & OPENMASK;
        return data == OPENMASK;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) throw new IllegalArgumentException();
        int idx = size * (row - 1) + col - 1;
        int root = engine.find(idx);
        int openFullMask = TOPCONTACTMASK | OPENMASK;
        return (engine.metadata[root] & openFullMask) == openFullMask;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return engine.openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return percolates;
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 20;

        Percolation wu = new Percolation(n);

        while (!wu.percolates()) {
            int idx = StdRandom.uniformInt(n * n);
            int i = (idx / n) + 1;
            int j = (idx % n) + 1;

            while (wu.isOpen(i, j)) {
                idx = StdRandom.uniformInt(n * n);
                i = (idx / n) + 1;
                j = (idx % n) + 1;
            }


            wu.open(i, j);
        }
    }
}

