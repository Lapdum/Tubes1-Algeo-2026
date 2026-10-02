package algeo.modules;

public class ModuleDeterminan {

    public static Double ekspansiKofaktorKolom(int x, Matrix matrix) { // kolom ke-x
        if (matrix.rows != matrix.cols) {
            return null;
        } else {
            x = x - 1;
            double det = 0;
            for (int c = 0; c < matrix.rows; c++) {
                det += matrix.data[c][x] * cofaktor(c, x, matrix);
            }
            return det;
        }
    }

    public static Double ekspansiKofaktorBaris(int x, Matrix matrix) { // baris ke-x
        if (matrix.rows != matrix.cols) {
            return null;
        } else {
            x = x - 1;
            double det = 0;
            for (int c = 0; c < matrix.cols; c++) {
                det += matrix.data[x][c] * cofaktor(x, c, matrix);
            }
            return det;
        }
    }

    public static double cofaktor(int i, int j, Matrix matrix) { // cofaktor (Cij)
        return Matrix.power(-1, i + j + 2) * minor(i, j, matrix);
    }

    public static double minor(int i, int j, Matrix matrix) { // minor (Mij)
        Matrix subM = subMatrix(i, j, matrix);
        if (subM.cols > 2) {
            return ekspansiKofaktorBaris(1, subM);
        } else {
            return localDeterminan2x2(subM);
            // if (i == 0 && j == 0) { return subM.data[1][1]; }
            // else if (i == 0 && j == 1) { return subM.data[1][0]; }
            // else if ( i == 1 && j == 0) { return subM.data[0][1]; }
            // else { return subM.data[0][0]; }
        }
    }

    public static Matrix subMatrix(int i, int j, Matrix matrix) {
        Matrix subM = new Matrix(matrix.rows - 1, matrix.cols - 1);
        int a = 0;
        int b = 0;

        int x = 0;
        int y = 0;
        while (a < matrix.rows) {
            if (a != i) {
                y = 0;
                b = 0;
                while (b < matrix.cols) {
                    if (b != j) {
                        subM.data[x][y] = matrix.data[a][b];
                        y++;
                    }
                    b++;
                }
                x++;
            }
            a++;
        }
        return subM;
    }

    static double localDeterminan2x2(Matrix matrix) {
        return (matrix.data[0][0] * matrix.data[1][1]) - (matrix.data[0][1] * matrix.data[1][0]);
    }

    public static boolean isSingular(Matrix matrix) {
        if (ekspansiKofaktorBaris(1, matrix) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static Double determinanReduksiBaris(Matrix matrix) {
        int n = matrix.getRows();

        if (n != matrix.getCols()) {
            return null;
        }

        double[][] salinan = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                salinan[i][j] = matrix.getValue(i, j);
            }
        }
        Matrix m = Matrix.doubletoMatrix(salinan, n, n);

        int countSwitch = 0;

        for (int k = 0; k < n; k++) {

            int bestPivot = Matrix.partialPivoting(m, k, k);

            if (bestPivot == -1) {
                return 0.0;
            }

            if (bestPivot != k) {
                m = Matrix.switchRow(m, k, bestPivot);
                countSwitch++;
            }

            double pivot = m.getValue(k, k);

            for (int j = k + 1; j < n; j++) {
                double multiplier = m.getValue(j, k) / pivot;

                if (multiplier != 0) {
                    m = Matrix.subtractRowbyRow(m, j, k, multiplier);
                }
            }
        }

        return reduksiBaris(m, countSwitch);
    }

    public static double reduksiBaris(Matrix matrix, int countSwitch) {
        double valueDiagonal = 1;
        for (int i = 0; i < matrix.rows; i++) {
            valueDiagonal *= matrix.data[i][i];
        }
        return Matrix.power(-1, countSwitch) * valueDiagonal;
    }

    // test
}