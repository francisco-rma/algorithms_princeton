/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class Percolation {
    private int[] ids;
    private int[] sizes;
    private boolean[] vacancies;

    private int size;
    private int virtualTop;
    private int virtualBottom;

    private List<Integer> attempts = new ArrayList<Integer>();

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        size = n;

        virtualTop = size * size;
        virtualBottom = size * size + 1;

        ids = new int[(size * size) + 2];
        sizes = new int[size * size + 2];
        vacancies = new boolean[(size * size) + 2];

        for (int i = 0; i < size * size + 2; i++) {
            ids[i] = i;
            sizes[i] = 1;

            boolean vacant = true;

            if (i == virtualBottom || i == virtualTop) vacant = false;
            vacancies[i] = vacant;
        }
    }

    private int root(int i) {
        while (i != ids[i]) {
            i = ids[i];
        }

        return i;
    }

    private boolean connected(int p, int q) {
        if (p >= size * size + 2 || q >= size * size + 2) {
            return false;
        }

        return (!vacancies[p] && !vacancies[q]) && (root(p) == root(q));
    }

    private void union(int p, int q) {
        if (p >= size * size + 2 || q >= size * size + 2) {
            System.out.println("Unable to connect requested numbers");
            return;
        }

        int i = root(p);
        int j = root(q);

        if (i == j) return;

        if (sizes[i] <= sizes[j]) {
            ids[i] = j;
            sizes[j] += sizes[i];
        }
        else {
            ids[j] = i;
            sizes[i] += sizes[j];

        }
    }

    private void display() {
        for (int i = 0; i < size * size; i++) {
            System.out.printf("Index: %d%n", i);

            System.out.printf("Neighbours: %n");
            List<Integer> neighbours = listNeighbours(i);
            for (int j = 0; j < neighbours.size(); j++) {
                System.out.printf("%d, ", neighbours.get(j));
            }
            System.out.printf("%nValue: %d%n", ids[i]);
            System.out.printf("Open: %s%n", !vacancies[i]);
            System.out.printf("%n");
        }
    }

    private List<Integer> listNeighbours(int idx) {
        List<Integer> neighbours = new ArrayList<Integer>();
        int i = idx / size;
        int j = idx % size;

        if (i > 0) neighbours.add(idx - size);
        if (i < size - 1) neighbours.add(idx + size);
        if (j > 0) neighbours.add(idx - 1);
        if (j < size - 1) neighbours.add(idx + 1);
        return neighbours;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int idx = row * size + col;
        vacancies[idx] = false;

        if (row == 0) {
            union(idx, virtualTop);
        }

        if (row == size - 1) {
            union(idx, virtualBottom);
        }

        List<Integer> neighbours = listNeighbours(idx);

        for (int i = 0; i < neighbours.size(); i++) {
            int value = neighbours.get(i);
            if (!vacancies[value]) {
                union(idx, value);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        return !vacancies[size * row + col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return connected(size * row + col, virtualTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < vacancies.length; i++) {
            if (!vacancies[i]) count += 1;
        }
        return count;
    }

    // does the system percolate?
    public boolean percolates() {
        int topRoot = root(virtualTop);
        int bottomRoot = root(virtualBottom);
        boolean check = connected(virtualTop, virtualBottom);
        return check;
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 20;

        Percolation wu = new Percolation(n);
        while (!wu.percolates()) {
            int idx = StdRandom.uniformInt(n * n);


            while (!wu.vacancies[idx]) {
                idx = StdRandom.uniformInt(n * n);
            }

            int i = idx / wu.size;
            int j = idx % wu.size;
            wu.open(i, j);
            wu.attempts.add(idx);
        }

        System.out.printf("Percolation achieved in %d attempts%n", wu.attempts.size());
    }
}
