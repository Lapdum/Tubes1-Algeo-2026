package algeo.modules;

import java.util.*;

public class SPL {

    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int inputMethod = 0;
        boolean inputValidation = true;

        System.out.println("Pilih metode input matriks augmented:");
        System.out.println("1. Input manual");
        System.out.println("2. Input file");

        while (inputValidation) {
            try {
                System.out.println("Metode input: ");
                inputMethod = sc.nextInt();

                if ((inputMethod == 1) || (inputMethod == 2)) {
                    inputValidation = false;

                } else {
                    System.out.println("Input angka tidak ada pada opsi!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Input harus berupa angka!");
                sc.next();
            }
        }

        sc.nextLine();

        if (inputMethod == 1) {
            manual();
        } else {
            fileInput();
        }
    }

    private static void manual() {
        int inputMethod;
        Matrix m;

        System.out.println("---------------------------------");
        System.out.println("|     Sistem Persamaan Linear    |");
        System.out.println("---------------------------------");
        System.out.println();

        System.out.println("Silahkan masukkan sistem persamaan linear anda dalam bentuk matriks augmented.");
        System.out.println("Ukuran maksimal matriks augmented adalah 11*12, termasuk hasil baris.");
        System.out.println("Apabila sudah selesai, silahkan tekan 'ENTER' pada baris kosong.\n");
        System.out.println("Contoh input matriks augmented:");
        System.out.println("1 2 3 4");
        System.out.println("5 6 7 8");
        System.out.println("9 10 11 12");
        System.out.println();
        System.out.println("(Hasil input adalah matriks augmented berukuran 3*4)");
        System.out.println();

        System.out.println("Input matriks augmented:\n");

        m = Matrix.inputMatrix();
        printSPL(m);

        inputMethod = chooseMethod();
        methods(inputMethod, m);
    }

    private static void fileInput() {
        int inputMethod;
        Matrix m;

        System.out.println("---------------------------------");
        System.out.println("|     Sistem Persamaan Linear    |");
        System.out.println("---------------------------------");
        System.out.println();

        m = Matrix.inputFileMatrix();
        printSPL(m);

        inputMethod = chooseMethod();
        methods(inputMethod, m);
    }

    private static int chooseMethod() {
        int inputMethod = 0;
        boolean inputValidation = true;

        System.out.println("\nPilih metode penyelesaian:");
        System.out.println("1. Metode Eliminasi Gauss");
        System.out.println("2. Metode Eliminasi Gauss-Jordan");
        System.out.println("3. Metode Matriks Balikan");
        System.out.println("4. Kaidah Cramer\n");

        while (inputValidation) {
            try {
                System.out.println("Metode penyelesaian yang dipilih:");
                inputMethod = sc.nextInt();

                if ((inputMethod == 1) || (inputMethod == 2) || (inputMethod == 3) || inputMethod == 4) {
                    inputValidation = false;
                } else {
                    System.out.println("Input angka tidak ada pada opsi!");

                }
            } catch (InputMismatchException e) {
                System.out.println("Input harus berupa angka!");
                sc.next();
            }

            sc.nextLine();

        }
        return inputMethod;
    }

    private static void methods(int inputMethod, Matrix m) {
        if (inputMethod == 1) {
            m = eliminasiGauss(m);

            if (noSolution(m)) {
                printSPL(m);
                System.out.println();
                System.out.println("Sistem persamaan linear tidak memiliki solusi");
            } else {
                buildAnswer(m, substitusiMundur(m));
                printSPL(m);
                System.out.println();
                System.out.println("Sistem persamaan linear memiliki solusi:");
                printSolution(m.answerSPL);
            }
        } else if (inputMethod == 2) {
            m = eliminasiGaussJordan(m);

            if (noSolution(m)) {
                printSPL(m);
                System.out.println();
                System.out.println("Sistem persamaan linear tidak memiliki solusi");
            } else {
                buildAnswer(m, substitusiMundur(m));
                printSPL(m);
                System.out.println();
                System.out.println("Sistem persamaan linear memiliki solusi:");
                printSolution(m.answerSPL);
            }
        } else if (inputMethod == 3) {
            int rInitial, cInitial, cntCols;
            double[][] result, inverseM;
            Matrix mInverse, n, res;
            boolean isInverse;

            rInitial = m.getRows();
            cInitial = m.getCols();
            cntCols = cInitial - 1;

            result = new double[rInitial][1];
            inverseM = new double[rInitial][cInitial - 1];

            mInverse = eliminasiInverse(m);

            isInverse = true;

            for (int i = 0; i < rInitial; i++) {
                if (mInverse.flag[i]) {
                    isInverse = false;
                }
            }

            if (isInverse == false || mInverse.getCols() == cInitial) {
                System.out.println("Metode inverse tidak dapat digunakan karena sistem tidak memiliki balikan!");
            } else {
                for (int i = 0; i < m.getRows(); i++) {
                    result[i][0] = m.getValue(i, m.getCols() - 1);
                }

                for (int i = 0; i < rInitial; i++) {
                    for (int j = 0; j < cInitial - 1; j++) {
                        inverseM[i][j] = m.getValue(i, j + cntCols);
                    }
                }

                mInverse = Matrix.doubletoMatrix(inverseM, rInitial, cInitial - 1);
                n = Matrix.doubletoMatrix(result, mInverse.getRows(), 1);
                res = Matrix.perkalianMatriks(mInverse, n);

                for (int i = 0; i < m.getRows(); i++) {
                    result[i][0] = res.getValue(i, 0);
                }

                buildAnswer(mInverse, result);
                System.out.println("Sistem persamaan linear memiliki solusi:");
                printSolution(mInverse.answerSPL);
            }
        } else {
            m = kaidahCramer(m);

            buildAnswer(m, m.determinantCramer);
            System.out.println("Sistem persamaan linear memiliki solusi:");
            printSolution(m.answerSPL);
        }

    }

    public static boolean noSolution(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();

        for (int i = 0; i < r; i++) {
            if (m.flag[i] && m.getValue(i, c - 1) != 0) {
                return true;
            }
        }
        return false;
    }

    public static Matrix eliminasiGauss(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();
        int nVar = c - 1;
        int pivotRow = 0;

        System.out.println("Matriks Awal: ");
        Matrix.printMatrix(m);
        System.out.println();

        for (int i = 0; i < nVar && pivotRow < r; i++) {
            int curr = pivotRow;
            int curc = i;

            int bestPivot = Matrix.partialPivoting(m, curr, curc);

            if (bestPivot == -1) {
                continue;
            } else if (bestPivot != curr) {
                m = Matrix.switchRow(m, curr, bestPivot);
                System.out.println(
                        "After partial pivoting between row " + (curr + 1) + " and row " + (bestPivot + 1) + ":");
                Matrix.printMatrix(m);
                System.out.println();
            }

            double num = m.getValue(curr, curc);

            // Normalize the row
            if (num != 1) {
                m = Matrix.multiplyRow(m, curr, 1 / (num));
                num = m.getValue(curr, curc);
                System.out.println("After normalizing row " + (curr + 1) + ":");
                Matrix.printMatrix(m);
                System.out.println();
            }

            // Make row below collumn to zero
            for (int j = curr + 1; j < r; j++) {
                double multiplier = m.getValue(j, curc);
                m = Matrix.subtractRowbyRow(m, j, curr, multiplier);

                System.out.println(
                        "After subtracting row " + (j + 1) + " with row " + (curr + 1) + " multiplied by " + multiplier
                                + ":");
                Matrix.printMatrix(m);
                System.out.println();
            }
            pivotRow++;
        }

        m = Matrix.cleanZeros(m);
        m = Matrix.moveZerosDown(m);

        for (int i = 0; i < r; i++) {
            boolean checker = true;
            for (int j = 0; j < c - 1; j++) {
                if (m.getValue(i, j) != 0 && checker) {
                    m.isPivot[i][j] = true;
                    m.isPivotCol[j] = true;
                    checker = false;
                }
            }
        }

        for (int i = 0; i < r; i++) {
            m.flag[i] = Matrix.isAllZeroRow(m, i);
        }
        return m;
    }

    public static Matrix eliminasiGaussJordan(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();

        m = eliminasiGauss(m);

        for (int i = r - 1; i >= 0; i--) {
            for (int j = 0; j < c - 1; j++) {
                if (m.isPivot[i][j]) {
                    for (int k = i - 1; k >= 0; k--) {
                        double multiplier = m.getValue(k, j);
                        m = Matrix.subtractRowbyRow(m, k, i, multiplier);

                        System.out.println(
                                "After subtracting row " + (k + 1) + " with row " + (i + 1) + " multiplied by "
                                        + multiplier
                                        + ":");
                        Matrix.printMatrix(m);
                        System.out.println();
                    }
                }
            }
        }
        m = Matrix.cleanZeros(m);
        return m;
    }

    public static Matrix eliminasiInverse(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();
        int zeroC = c - 1;
        double[][] currM = new double[r][c + 2];

        if (r != zeroC) {
            return m;
        }
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c - 1; j++) {
                currM[i][j] = m.getValue(i, j);
            }
        }

        for (int i = 0; i < r; i++) {
            for (int j = c - 1; j < c + 2; j++) {
                if (zeroC == j) {
                    currM[i][j] = 1;
                } else {
                    currM[i][j] = 0;
                }
            }
            zeroC++;
        }

        m = Matrix.doubletoMatrix(currM, r, c + 2);
        m = eliminasiGaussJordan(m);
        m = Matrix.moveZerosDown(m);

        for (int i = 0; i < r; i++) {
            boolean isZero = true;
            for (int j = 0; j < c - 1; j++) {
                if (m.getValue(i, j) != 0) {
                    isZero = false;
                }
            }
            m.flag[i] = isZero;
        }

        return m;
    }

    public static Matrix kaidahCramer(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();
        Matrix b;
        Matrix aInitial;
        Matrix aModified;

        // Fill A and b

        double[][] aDouble = new double[r][c - 1];
        double[][] bDouble = new double[r][1];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c - 1; j++) {
                aDouble[i][j] = m.getValue(i, j);
            }
        }

        for (int i = 0; i < r; i++) {
            bDouble[i][0] = m.getValue(i, c - 1);
        }

        b = Matrix.doubletoMatrix(bDouble, r, 1);
        aInitial = Matrix.doubletoMatrix(aDouble, r, c - 1);
        aModified = Matrix.doubletoMatrix(aDouble, r, c - 1);

        double detAInitial = ModuleDeterminan.ekspansiKofaktorBaris(1, aInitial);

        for (int cols = 0; cols < c - 1; cols++) {
            for (int row = 0; row < r; row++) {
                double num = b.getValue(row, 0);
                aModified.set(row, cols, num);
            }
            m.determinantCramer[cols][0] = ModuleDeterminan.ekspansiKofaktorBaris(1, aModified);

            for (int row = 0; row < r; row++) {
                double num = aInitial.getValue(row, cols);
                aModified.set(row, cols, num);
            }
        }

        for (int i = 0; i < c - 1; i++) {
            System.out.println("" + m.determinantCramer[i][0]);
        }

        for (int i = 0; i < c - 1; i++) {
            m.determinantCramer[i][0] /= detAInitial;
        }

        return m;
    }

    public static final String[] PARAM_NAMES = { "r", "s", "t", "u", "v", "w", "a", "b", "c", "d", "e" };

    public static String paramName(int slot) {
        if (slot <= PARAM_NAMES.length) {
            return PARAM_NAMES[slot - 1];
        }
        return "t" + slot;
    }

    public static void buildAnswer(Matrix m, double[][] sol) {
        int n = sol.length;
        int k = sol[0].length - 1;

        for (int i = 0; i < n; i++) {
            String line = "X" + (i + 1) + " = ";
            boolean first = true;

            if (sol[i][0] != 0) {
                double num = Matrix.round3(sol[i][0]);

                if (num == (int) num) {
                    line += "" + (int) num;
                } else {
                    line += "" + num;
                }

                first = false;
            }

            for (int j = 1; j <= k; j++) {
                double coef = sol[i][j];
                if (coef == 0) {
                    continue;
                }

                if (first) {
                    if (coef < 0) {
                        line += "-";
                    }
                } else {
                    if (coef < 0) {
                        line += " - ";
                    } else {
                        line += " + ";
                    }
                }

                double mag;

                if (coef < 0) {
                    mag = -coef;
                } else {
                    mag = coef;
                }

                if (mag != 1) {
                    double num = Matrix.round3(mag);

                    if (num == (int) num) {
                        line += "" + (int) num;
                    } else {
                        line += "" + num;
                    }
                }

                line += paramName(j);
                first = false;
            }

            if (first) {
                line += "0";
            }

            m.answerSPL[i] = line;
        }
    }

    public static double[][] substitusiMundur(Matrix m) {
        int r = m.getRows();
        int c = m.getCols() - 1;

        int[] paramSlot = new int[c];
        int k = 0;

        for (int i = 0; i < c; i++) {
            if (m.isPivotCol[i]) {
                paramSlot[i] = -1;
            } else {
                m.freeVar = true;
                k++;
                paramSlot[i] = k;
            }
        }

        double[][] sol = new double[c][k + 1];

        for (int j = 0; j < c; j++) {
            if (paramSlot[j] != -1) {
                sol[j][paramSlot[j]] = 1;
            }
        }

        for (int i = r - 1; i >= 0; i--) {
            int p = -1;
            for (int j = 0; j < c; j++) {
                if (m.isPivot[i][j]) {
                    p = j;
                    break;
                }
            }
            if (m.flag[i]) {
                continue;
            }

            sol[p][0] = m.getValue(i, c);

            for (int j = p + 1; j < c; j++) {
                double coef = m.getValue(i, j);
                if (coef == 0) {
                    continue;
                }

                for (int s = 0; s <= k; s++) {
                    sol[p][s] -= coef * sol[j][s];
                }
            }

            double pivotVal = m.getValue(i, p);

            for (int s = 0; s <= k; s++) {
                sol[p][s] /= pivotVal;
            }

            final double EPS = 1e-9;
            for (int j = 0; j < c; j++) {
                for (int s = 0; s <= k; s++) {

                    if (sol[j][s] > -EPS && sol[j][s] < EPS) {
                        sol[j][s] = 0;
                    }
                }
            }
        }

        return sol;
    }

    public static void printSolution(String[] answerSPL) {
        for (int i = 0; i < answerSPL.length; i++) {
            if (answerSPL[i] != null) {
                if (i != answerSPL.length - 1) {
                    System.out.printf(answerSPL[i] + "; ");
                } else {
                    System.out.printf("" + answerSPL[i]);
                }
            }
        }
        System.out.println();
    }

    public static void printSPL(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();

        for (int i = 0; i < r; i++) {
            if (m.flag[i]) {
                continue;
            }
            for (int j = 0; j < c; j++) {
                double currNum = Matrix.round3(m.getValue(i, j));

                if (currNum != 0 && j != c - 1) {
                    if (j != 0 && currNum < 0) {
                        currNum *= -1;
                    }

                    if (currNum != 1) {
                        if (currNum == (int) currNum) {
                            System.out.print((int) currNum);
                        } else {
                            System.out.print(currNum);
                        }
                    }

                    System.out.printf("X" + (j + 1) + " ");

                    if (j != c - 2) {
                        double nextNum = m.getValue(i, j + 1);
                        if (nextNum < 0) {
                            System.out.printf("- ");
                        } else if (nextNum > 0) {
                            System.out.printf("+ ");
                        }

                    }

                } else if (j == c - 1) {
                    System.out.printf("= ");
                    if (currNum == (int) currNum) {
                        System.out.print((int) currNum);
                    } else {
                        System.out.print(currNum);
                    }
                }
            }
            System.out.println();
        }
    }
}