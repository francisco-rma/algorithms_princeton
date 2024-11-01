/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

public class PercolationOld {
    private int[] ids;
    private byte[] metadata;
    private int[] sizes;
    private boolean[] opened;
    private int openCount;

    private int size;
    private int virtualTop;
    private int virtualBottom;
    private boolean btt = false;


    // creates n-by-n grid, with all sites initially blocked
    public PercolationOld(int n) {
        if (n <= 0) throw new IllegalArgumentException();

        openCount = 0;
        size = n;
        virtualTop = size * size;
        virtualBottom = size * size + 1;

        ids = new int[(size * size) + 2];
        sizes = new int[size * size + 2];
        opened = new boolean[(size * size) + 2];

        for (int i = 0; i < size * size + 2; i++) {
            ids[i] = i;
            sizes[i] = 1;
            if (i == virtualBottom || i == virtualTop) opened[i] = true;
        }
    }

    private int root(int i) {
        while (i != ids[i]) {
            i = ids[i];
        }

        return i;
    }

    private void union(int p, int q) {
        if (p >= size * size + 2 || q >= size * size + 2) {
            System.out.println("Unable to connect requested numbers");
            return;
        }

        int i = root(p);
        int j = root(q);

        if (i == j) return;

        boolean isPercolation = (i == virtualBottom || i == virtualTop) && (j == virtualBottom
                || j == virtualTop);

        if (sizes[i] <= sizes[j]) {
            ids[i] = j;
            sizes[j] += sizes[i];

            if (isPercolation) {
                btt = i == virtualBottom;
            }
        }
        else {
            ids[j] = i;
            sizes[i] += sizes[j];

            if (isPercolation) {
                btt = j == virtualBottom;
            }
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) throw new IllegalArgumentException();

        int idx = (row - 1) * size + col - 1;

        if (!isOpen(row, col)) {
            opened[idx] = true;
            openCount += 1;
        }

        if (row - 1 == 0) {
            union(idx, virtualTop);
        }

        if (row - 1 == size - 1) {
            union(idx, virtualBottom);
        }

        if (row - 1 > 0 && isOpen(row - 1, col)) {
            union(idx, idx - size);
        }
        if (row - 1 < size - 1 && isOpen(row + 1, col)) {
            union(idx, idx + size);
        }

        if (col - 1 > 0 && isOpen(row, col - 1)) {
            union(idx, idx - 1);
        }

        if (col - 1 < size - 1 && isOpen(row, col + 1)) {
            union(idx, idx + 1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) throw new IllegalArgumentException();
        return opened[size * (row - 1) + col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) throw new IllegalArgumentException();
        int idx = size * (row - 1) + col - 1;
        boolean topContact = false;
        boolean bottomContact = false;

        while (idx != ids[idx] && !topContact && !bottomContact) {
            idx = ids[idx];
        }

        while (idx != ids[idx] && !topContact && !bottomContact) {
            idx = ids[idx];
        }

        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return root(virtualTop) == root(virtualBottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 20;

        PercolationOld wu = new PercolationOld(n);
        while (!wu.percolates()) {
            int idx = StdRandom.uniformInt(n * n);


            while (!wu.opened[idx]) {
                idx = StdRandom.uniformInt(n * n);
            }

            int i = idx / wu.size;
            int j = idx % wu.size;
            wu.open(i, j);
        }
    }
}
