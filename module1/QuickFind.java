/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class QuickFind implements UnionFind {
    private int[] ids;

    private int size;

    public QuickFind(int n) {
        size = n;
        ids = new int[n];

        for (int i = 0; i < n; i++) {
            ids[i] = i;
        }
    }

    public static void main(String[] args) {
        int n = 10;
        QuickFind uf = new QuickFind(n);
        uf.union(1, 2);
        uf.union(3, 4);
        uf.union(5, 6);
        uf.union(7, 8);
        uf.union(5, 0);
        uf.union(9, 1);
        uf.union(6, 5);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    System.out.printf("%d -- %d: %s%n", i, j, uf.connected(i, j));
                }
            }
        }
    }

    public boolean connected(int p, int q) {
        if (p >= size || q >= size) {
            return false;
        }
        return ids[p] == ids[q];
    }

    public void union(int p, int q) {
        if (p >= size || q >= size) {
            System.out.println("Unable to connect requested numbers");
            return;
        }
        int target = ids[q];
        int old = ids[p];

        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == old) {
                ids[i] = target;
            }
        }
    }
}
