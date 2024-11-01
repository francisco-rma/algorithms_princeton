import java.util.ArrayList;
import java.util.List;

public class PercolationWeightedUnion implements UnionFind {
    private int size;

    private int[] ids;
    private int[] sizes;
    private boolean[] vacancies;

    private int virtualTop;
    private int virtualBottom;

    public PercolationWeightedUnion(int n) {
        size = n;

        virtualTop = size * size;
        virtualBottom = size * size + 1;

        ids = new int[(size * size) + 2];
        sizes = new int[size * size + 2];
        vacancies = new boolean[(size * size) + 2];

        for (int i = 0; i < size * size + 2; i++) {
            ids[i] = i;
            sizes[i] = 1;
            vacancies[i] = true;
        }

        int bottomRow = (size * size) - size;

        for (int i = 0; i < size; i++) {
            union(i, virtualTop);
            union(bottomRow + i, virtualBottom);
        }
    }

    private int root(int i) {
        while (i != ids[i]) {
            i = ids[i];
        }

        return i;
    }

    public boolean connected(int p, int q) {
        if (p >= size * size || q >= size * size) {
            return false;
        }

        return (!vacancies[p] && !vacancies[q]) && (root(p) == root(q));
    }

    public void union(int p, int q) {
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

    public void open(int n) {
        vacancies[n] = false;
        List<Integer> neighbours = listNeighbours(n);

        for (int i = 0; i < neighbours.size(); i++) {
            int value = neighbours.get(i);
            if (!vacancies[value]) {
                union(n, i);
            }
        }
    }

    public List<Integer> listNeighbours(int idx) {
        List<Integer> neighbours = new ArrayList<Integer>();
        int i = idx / size;
        int j = idx % size;

        if (i != 0) {
            neighbours.add(idx - size);
        }
        else if (i < size) {
            neighbours.add(idx + size);
        }

        if (j != 0) {
            neighbours.add(idx - 1);
        }
        else if (j < size) {
            neighbours.add(idx + 1);
        }

        return neighbours;
    }

    public boolean percolates() {
        return connected(virtualTop, virtualBottom);
    }

    public static void main(String[] args) {
        int n = 2;
        PercolationWeightedUnion wu = new PercolationWeightedUnion(n);
        wu.union(0, 3);

        wu.open(0);
        // wu.open(3);

        for (int i = 0; i < n * n; i++) {
            System.out.printf("Index: %d%n", i);

            System.out.printf("Neighbours: %n");
            List<Integer> neighbours = wu.listNeighbours(i);
            for (int j = 0; j < neighbours.size(); j++) {
                System.out.printf("%d, ", neighbours.get(j));
            }
            System.out.printf("%nValue: %d%n", wu.ids[i]);
            System.out.printf("Open: %s%n", !wu.vacancies[i]);
            System.out.printf("%n");
        }


        System.out.printf("Connection 0-3: %s%n", wu.connected(0, 3));
        System.out.printf("Percolates: %s%n", wu.connected(0, 3));
    }
}