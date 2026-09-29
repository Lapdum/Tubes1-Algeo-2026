package algeo.modules;

public class Inverse {
    public static Matrix inverseGaussJordan(Matrix matrix) {
        if (matrix.getRows() != matrix.getCols()) {
            throw new IllegalArgumentException("Matriks harus persegi");
        }

        int n = matrix.getRows();
        Matrix augmented = new Matrix(n, 2 * n); // jml kolom matrix semula ditambah kolom matrix identitas

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                augmented.set(i, j, matrix.getValue(i, j));// matrix augmented jadi sebelah kiri

                if (i == j) { // r == c
                    augmented.set(i, j + n, 1); // matriks identitas di sebelah kanan
                } else {
                    augmented.set(i, j + n, 0);
                }
            }
        }
        System.out.println("Matrix augmented [A|I]:");
        Matrix.printMatrix(augmented);

        for (int i = 0; i < n; i++) {
            Matrix.partialPivoting(augmented, i, i);

            Matrix.printMatrix(augmented);
            // pivot
            double pivot = augmented.getValue(i, i);
            if (pivot == 0) {
                throw new IllegalArgumentException("Matriks tidak memiliki balikan");
            }
            Matrix.multiplyRow(augmented, i, 1.0 / pivot); // sebaris dibagi 1/pivot
            System.out.println("R" + (i + 1) + " <- (1/" + pivot + ")R" + (i + 1));
            Matrix.printMatrix(augmented);

            for (int r = 0; r < n; r++) { // elim dibawah pivot
                if (r != i) {
                    double factor = augmented.getValue(r, i);

                    if (factor != 0) {
                        Matrix.addRowbyRow(augmented, r, i, -factor);

                        System.out.println(
                                "R" + (r + 1) + " = R" + (r + 1) + " - (" + factor + ")R" + (i + 1));
                        Matrix.printMatrix(augmented);

                    }

                }
            }
        }
        Matrix inverse = new Matrix(n, n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse.set(i, j, augmented.getValue(i, j + n));
            }
        }
        return inverse;
    }

    public static Matrix inverseAdjoint(Matrix matrix) {
        if (matrix.getRows() != matrix.getCols()) {
            throw new IllegalArgumentException("Matrix harus persegi.");
        }
        int n = matrix.getRows();
        double det = ModuleDeterminan.ekspansiKofaktorBaris(1, matrix);

        System.out.println("Determinan = " + det);

        if (det == 0) {
            throw new IllegalArgumentException("Matrix tidak memiliki balikan");

        }
        Matrix cofactor = new Matrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double value = ModuleDeterminan.cofaktor(i, j, matrix);
                cofactor.set(i, j, value);
            }
        }
        System.out.println("Matriks Kofaktor:");
        Matrix.printMatrix(cofactor);

        Matrix adjoint = new Matrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjoint.set(i, j, cofactor.getValue(i, j));
            }
        }
        System.out.println("Matriks Adjoint:");
        Matrix.printMatrix(adjoint);

        Matrix inverse = new Matrix(n, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse.set(i, j, adjoint.getValue(i, j) / det);
            }

        }
        System.out.println("Matriks Inverse:");
        Matrix.printMatrix(inverse);

        return inverse;
    }

}
