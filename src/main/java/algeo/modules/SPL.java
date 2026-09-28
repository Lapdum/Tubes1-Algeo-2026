package algeo.modules;

import java.io.*;
import java.util.*;

public class SPL {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        start();
    }

    private static void start() {
        System.out.println("Pilih metode input matriks augmented:");
        System.out.println("1. Input manual");
        System.out.println("2. Input file");

        boolean inputValidation = true;
        int inputMethod = 0;

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

        Matrix m = Matrix.inputMatrix();
        printSPL(m);

        System.out.println("\nPilih metode penyelesaian:");
        System.out.println("1. Metode Eliminasi Gauss");
        System.out.println("2. Metode Eliminasi Gauss-Jordan");
        System.out.println("3. Metode Matriks Balikan");
        System.out.println("4. Kaidah Cramer\n");

        boolean inputValidation = true;
        int inputMethod = 0;

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
                int rInitial = m.getRows();
                int cInitial = m.getCols();

                double[][] result = new double[rInitial][1];

                for (int i = 0; i < m.getRows(); i++) {
                    result[i][0] = m.getValue(i, m.getCols() - 1);
                }
                m = eliminasiInverse(m);

                boolean isInverse = true;
                for (int i = 0; i < rInitial; i++) {
                    if (m.flag[i]) {
                        isInverse = false;
                    }
                }

                if (isInverse == false || m.getCols() == cInitial) {
                    System.out.println("Metode inverse tidak dapat digunakan karena sistem tidak memiliki balikan!");
                } else {
                    double[][] inverseM = new double[rInitial][cInitial - 1];
                    Matrix n = new Matrix(m.getRows(), 1);
                    Matrix res = new Matrix(m.getRows(), 1);

                    int cntCols = cInitial - 1;
                    for (int i = 0; i < rInitial; i++) {
                        for (int j = 0; j < cInitial - 1; j++) {
                            inverseM[i][j] = m.getValue(i, j + cntCols);
                        }
                    }

                    m = Matrix.doubletoMatrix(inverseM, rInitial, cInitial - 1);
                    n = Matrix.doubletoMatrix(result, m.getRows(), 1);
                    res = Matrix.perkalianMatriks(m, n);

                    for (int i = 0; i < m.getRows(); i++) {
                        result[i][0] = res.getValue(i, 0);
                    }

                    buildAnswer(m, result);
                    System.out.println("Sistem persamaan linear memiliki solusi:");
                    printSolution(m.answerSPL);
                }
            } else {
                System.out.println("Kaidah Cramer lagi on progress!");
            }

        }

    }

    private static void fileInput() {
        System.out.println("Silahkan masukkan nama file SPL:");
    }

    private static boolean noSolution(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();

        for (int i = 0; i < r; i++) {
            if (m.flag[i] && m.getValue(i, c - 1) != 0) {
                return true;
            }
        }
        return false;
    }

    private static Matrix eliminasiGauss(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();
        int nVar = c - 1;

        System.out.println("Matriks Awal: ");
        Matrix.printMatrix(m);
        System.out.println();

        int pivotRow = 0;

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

            Matrix.cleanZeros(m);

            for (int j = 0; j < c; j++) {
                if (m.getValue(curr, j) != 0) {
                    m.isPivot[curr][j] = true;
                    m.isPivotCol[j] = true;
                    break;
                }
            }
            pivotRow++;
        }

        m = Matrix.moveZerosDown(m);
        for (int i = 0; i < r; i++) {
            m.flag[i] = Matrix.isAllZeroRow(m, i);
        }
        return m;
    }

    private static Matrix eliminasiGaussJordan(Matrix m) {
        m = eliminasiGauss(m);

        System.out.println("Starting jordan elimination:");

        int r = m.getRows();
        int c = m.getCols();

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

        return m;
    }

    private static Matrix eliminasiInverse(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();

        if (r != c - 1) {
            return m;
        }

        double[][] currM = new double[r][c + 2];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c - 1; j++) {
                currM[i][j] = m.getValue(i, j);
            }
        }

        int zeroC = c - 1;
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

    private static final String[] PARAM_NAMES = { "r", "s", "t", "u", "v", "w", "a", "b", "c", "d", "e" };

    private static String paramName(int slot) {
        if (slot <= PARAM_NAMES.length) {
            return PARAM_NAMES[slot - 1];
        }
        return "t" + slot;
    }

    private static void buildAnswer(Matrix m, double[][] sol) {
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

    private static double[][] substitusiMundur(Matrix m) {
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
                sol[j][paramSlot[j]] = 1.0;
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
            if (p == -1) {
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
                        sol[j][s] = 0.0;
                    }
                }
            }
        }

        return sol;
    }

    private static void printSolution(String[] answerSPL) {
        for (int i = 0; i < answerSPL.length; i++) {
            if (i != answerSPL.length - 1) {
                System.out.printf(answerSPL[i] + "; ");
            } else {
                System.out.printf("" + answerSPL[i]);
            }
        }
        System.out.println();
    }

    private static void printSPL(Matrix m) {
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