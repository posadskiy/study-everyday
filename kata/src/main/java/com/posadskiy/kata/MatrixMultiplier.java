package com.posadskiy.kata;

/**
 * In mathematics, a matrix (plural matrices) is a rectangular array of numbers. Matrices have many applications in programming, from performing transformations in 2D space to machine learning.
 * <p>
 * One of the most useful operations to perform on matrices is matrix multiplication, which takes a pair of matrices and produces another matrix – known as the dot product. Multiplying matrices is very different to multiplying real numbers, and follows its own set of rules.
 * <p>
 * Unlike multiplying real numbers, multiplying matrices is non-commutative: in other words, multiplying matrix a by matrix b will not give the same result as multiplying matrix b by matrix a.
 * <p>
 * Additionally, not all pairs of matrix can be multiplied. For two matrices to be multipliable, the number of columns in matrix a must match the number of rows in matrix b.
 * <p>
 * There are many introductions to matrix multiplication online, including at Khan Academy, and in the classic MIT lecture series by Herbert Gross.
 * <p>
 * To complete this kata, write a function that takes two matrices - a and b - and returns the dot product of those matrices. If the matrices cannot be multiplied, return null/None/Nothing or similar.
 * <p>
 * Each matrix will be represented by a two-dimensional list (a list of lists). Each inner list will contain one or more numbers, representing a row in the matrix.
 * <p>
 * For example, the following matrix:
 * <p>
 * |1 2|
 * |3 4|
 * <p>
 * Would be represented as:
 * <p>
 * [[1, 2], [3, 4]]
 * <p>
 * It can be assumed that all lists will be valid matrices, composed of lists with equal numbers of elements, and which contain only numbers. The numbers may include integers and/or decimal points.
 */
public class MatrixMultiplier {

    public static double[][] getMatrixProduct(double[][] a, double[][] b) {
        if (a[0].length != b.length) {
            return null;
        }
        double[][] res = new double[a.length][b[0].length];
        for (int i = 0; i < a.length; ++i) {
            for (int j = 0; j < b[0].length; ++j) {
                double c = 0;
                for (int k = 0; k < b.length; ++k) {
                    c += a[i][k] * b[k][j];
                }
                res[i][j] = c;
            }
        }

        return res;
    }

}
