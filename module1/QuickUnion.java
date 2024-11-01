public class QuickUnion implements UnionFind {
    private int[] ids;
    private int size;

    public QuickUnion(int n) {
        size = n;
        ids = new int[n];

        for (int i = 0; i < n; i++) {
            ids[i] = i;
        }
    }

    public static void main(String[] args) {

    }

    private int root(int i) {
        while (i != ids[i]) {
            i = ids[i];
        }
        return i;
    }

    public boolean connected(int p, int q) {
        if (p >= size || q >= size) {
            return false;
        }

        return root(p) == root(q);
    }

    public void union(int p, int q) {
        if (p >= size || q >= size) {
            System.out.println("Unable to connect requested numbers");
            return;
        }

        ids[root(p)] = root(q);
    }
}
