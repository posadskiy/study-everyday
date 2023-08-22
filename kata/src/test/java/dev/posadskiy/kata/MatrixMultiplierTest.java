package dev.posadskiy.kata;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class MatrixMultiplierTest {
    @Test
    public void test1x1() {
        Assert.assertArrayEquals(new double[][] { { 6 } },
            MatrixMultiplier.getMatrixProduct(
                new double[][] { { 2 } },
                new double[][] { { 3 } }));
    }

    @Test
    public void test2x2() {
        Assert.assertArrayEquals(new double[][] { { 19, 22 }, { 43, 50 } },
            MatrixMultiplier.getMatrixProduct(
                new double[][] { { 1, 2 }, { 3, 4 } },
                new double[][] { { 5, 6 }, { 7, 8 } }));
    }

    @Test
    public void testDecimals() {
        Assert.assertArrayEquals(new double[][] { { 0.7, 1.0 }, { 1.5, 2.2 } },
            MatrixMultiplier.getMatrixProduct(
                new double[][] { { 0.5, 1 }, { 1.5, 2 } },
                new double[][] { { 0.2, 0.4 }, { 0.6, 0.8 } }));
    }

    @Test
    public void testAsymmetric() {
        Assert.assertArrayEquals(new double[][] { { 73, 31, 78 }, { 54, 13, 43 }, { 106, 32, 94 }, { 63, 36, 81 } },
            MatrixMultiplier.getMatrixProduct(
                new double[][] { { 7, 3 }, { 2, 5 }, { 6, 8 }, { 9, 0 } },
                new double[][] { { 7, 4, 9 }, { 8, 1, 5 } }));
    }

    @Test
    public void testInvalid() {
        Assert.assertArrayEquals(null,
            MatrixMultiplier.getMatrixProduct(new double[][] { { 1, 2 }, { 3, 4 } }, new double[][] { { 2, 4 } }));
    }

    private static double[][] sol(double[][] a, double[][] b) {

        int aRows = a.length;
        int aCols = a[0].length;
        int bRows = b.length;
        int bCols = b[0].length;

        if (aCols != bRows) {
            return null;
        }

        double[][] product = new double[aRows][];

        for (int n = 0; n < aRows; n++) {
            double[] row = new double[bCols];
            for (int m = 0; m < bCols; m++) {
                double result = 0;
                for (int i = 0; i < aCols; i++) {
                    result += a[n][i] * b[i][m];
                }
                row[m] = result;
            }
            ;
            product[n] = row;
        }
        return product;
    }

    private static final Random rnd = new Random();

    private double[][] constructMatrix(int h, int w) {
        return IntStream.range(0, h)
            .mapToObj(i -> DoubleStream.generate(() -> Math.random() * 200 - 100).limit(w).toArray())
            .toArray(double[][]::new);
    }

    @Test
    public void testRandom() {

        for (int i = 0; i < 100; i++) {

            int m = rnd.nextInt(10) + 1;
            int n = rnd.nextInt(10) + 1;
            int k = rnd.nextInt(10) + 1;

            double[][] a, b, expected;

            if (Math.random() > 0.2) {
                a = constructMatrix(m, k);
                b = constructMatrix(k, n);
                expected = sol(a, b);
            } else {
                a = constructMatrix(m, rnd.nextInt(10) + 1);
                do {
                    b = constructMatrix(rnd.nextInt(10) + 1, n);
                } while (a[0].length == b.length);
                expected = null;
            }
            double[][] actual = MatrixMultiplier.getMatrixProduct(a, b);

            if (expected == null) {
                if(actual != null)
                    System.out.println(Arrays.toString(actual[0]));
                Assert.assertNull(actual);
            } else {

                Assert.assertEquals("Matrix row count does not match", expected.length, actual.length);
                IntStream.range(0, actual.length)
                    .forEach(row -> Assert.assertArrayEquals(expected[row], actual[row], 1e-9));
            }
        }
    }
}
