/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private Percolation engine;
    private double[] results;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        results = new double[trials];

        for (int i = 0; i < trials; i++) {
            int count = 0;

            engine = new Percolation(n);

            while (!engine.percolates()) {
                int idx = StdRandom.uniformInt(n * n);
                int row = idx / n;
                int col = idx % n;

                while (engine.isOpen(row, col)) {
                    int openSites = engine.numberOfOpenSites();
                    idx = StdRandom.uniformInt(n * n);
                    row = idx / n;
                    col = idx % n;
                }

                engine.open(row, col);
                count += 1;
            }

            System.out.printf("Percolation achieved in %d attempts%n", count);
            double total = n * n;
            double result = ((double) count) / total;
            results[i] = result;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((1.96 * stddev()) / Math.sqrt(results.length));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((1.96 * stddev()) / Math.sqrt(results.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        while (true) {
            try {
                int n = Integer.parseInt(args[0]);
                int trials = Integer.parseInt(args[1]);
                break;
            }
            catch (IllegalArgumentException e) {
                System.out.println("Invalid arguments, try again");
            }
        }

        PercolationStats stats = new PercolationStats(200, 100);

        System.out.printf("mean threshold: %f%n", stats.mean());
        System.out.printf("stddev threshold: %f%n", stats.stddev());
        System.out.printf("95%% confidence interval: [%f......%f%n]", stats.confidenceLo(),
                          stats.confidenceHi());
    }
}
