package algeo.modules;

import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class CubicRegression {

    private static Scanner sc = new Scanner(System.in);
    private static StringBuilder content = new StringBuilder();

    public static void main(String[] args) {
        System.out.println("Pilih metode input matriks augmented:");
        System.out.println("1. Input manual");
        System.out.println("2. Input file");

        boolean inputValidation = true;
        int inputMethod = 0;

        while (inputValidation) {
            try {
                System.out.printf("Metode input: ");
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

    private static void note(String s) {
        System.out.println(s);
        content.append(s).append("\n");
    }

    private static void manual() {
        System.out.println("---------------------------------");
        System.out.println("|       Regresi Splina Kubik    |");
        System.out.println("---------------------------------");
        System.out.println();

        System.out.println("Silahkan masukkan pasangan titik sample.");
        System.out.println("Jumlah maksimal titik sample adalah 10 titik.");
        System.out.println("Apabila sudah selesai, silahkan tekan 'ENTER' pada baris kosong.\n");
        System.out.println("Contoh input titik sample:");
        System.out.println("1 2");
        System.out.println("5 6");
        System.out.println("9 10");
        System.out.println();
        System.out.println("(Hasil input adalah 3 titik sample)");
        System.out.println();

        System.out.println("Input titik sample:\n");

        Matrix titikSampel = Matrix.inputTitik();

        System.out.printf("Input jumlah knot:\n");
        int jumlahKnot = sc.nextInt();

        System.out.println("Input posisi knot:");
        double[][] posisiKnotDouble = new double[1][jumlahKnot];

        for (int i = 0; i < jumlahKnot; i++) {
            posisiKnotDouble[0][i] = sc.nextDouble();
        }

        Matrix posisiKnot = Matrix.doubletoMatrix(posisiKnotDouble, 1, jumlahKnot);

        methodTitik(titikSampel, jumlahKnot, posisiKnot);

    }

    private static void fileInput() {
        System.out.println("---------------------------------");
        System.out.println("|     Sistem Persamaan Linear    |");
        System.out.println("---------------------------------");
        System.out.println();

        Matrix titikSampel = Matrix.inputFileTitik();

        System.out.printf("Input jumlah knot:\n");
        int jumlahKnot = sc.nextInt();

        System.out.println("Input posisi knot:");
        double[][] posisiKnotDouble = new double[1][jumlahKnot];

        for (int i = 0; i < jumlahKnot; i++) {
            posisiKnotDouble[0][i] = sc.nextDouble();
        }

        Matrix posisiKnot = Matrix.doubletoMatrix(posisiKnotDouble, 1, jumlahKnot);

        methodTitik(titikSampel, jumlahKnot, posisiKnot);

    }

    private static void methodTitik(Matrix titikSampel, int jumlahKnot, Matrix posisiKnot) {
        sc.nextLine();
        content.setLength(0);
        content.append("\n");
        Matrix X = makeMatrixX(titikSampel, jumlahKnot, posisiKnot);

        X = eliminasiGaussTitik(X);

        if (SPL.noSolution(X)) {
            note("Regresi tidak memiliki solusi");
            outputRegression();
            return;
        }

        double[][] solution = SPL.substitusiMundur(X);
        SPL.buildAnswer(X, solution);
        System.out.println();

        if (X.freeVar) {
            note("Regresi memiliki solusi banyak:");
            note(SPL.solutionToString(X.answerSPL));
            note("Dengan setiap parametrik merupakan bilangan rill.");
        } else {
            note("Regresi memiliki solusi:");
            note(SPL.solutionToString(X.answerSPL));
        }
        System.out.println();

        StringBuilder knot = new StringBuilder();
        for (int i = 0; i < jumlahKnot; i++) {
            if (i > 0) {
                knot.append("; ");
            }
            knot.append("knot ").append(i + 1).append(" = ").append("" + posisiKnot.getValue(0, i));
        }
        note("Posisi knot yang digunakan : ");
        if (jumlahKnot == 0) {
            note("tidak ada knot.");
        } else {
            note(knot.toString());
        }
        note("");

        if (X.freeVar) {
            note("Persamaan regresi splina (solusi tidak unik, setiap parametrik bernilai 0): ");
        } else {
            note("Persamaan regresi splina: ");
        }

        note(regresitoString(solution, jumlahKnot, posisiKnot));
        note("");

        System.out.printf("Silahkan input nilai untuk melihat hasil prediksi persamaan regresi:\n");
        System.out.printf("(Jika ingin menyelesaikan uji prediksi, tekan 'ENTER' pada nilai kosong.)\n");

        while (true) {

            System.out.printf("nilai Xt: ");
            String Xt = sc.nextLine().trim();

            if (Xt.isEmpty()) {
                break;
            }

            try {
                double xt = Matrix.parseAngka(Xt);
                double yt = regresiResult(xt, solution, jumlahKnot, posisiKnot);
                note("y(" + ("" + xt) + ") = " + ("" + Matrix.round3(yt)));
            } catch (NumberFormatException e) {
                System.out.println("\"" + Xt + "\" bukan angka yang valid!");
            }

        }
        outputRegression();
    }

    private static String regresitoString(double[][] solution, int jumlahKnot, Matrix posisiKnot) {
        StringBuilder sb = new StringBuilder("y(x) = ");
        boolean first = true;

        for (int i = 0; i < jumlahKnot + 4; i++) {
            double num = Matrix.round3(solution[i][0]);
            if (num == 0) {
                continue;
            }

            if (first) {
                if (num == (int) num) {
                    sb.append("" + (int) num);
                } else {
                    sb.append("" + num);
                }
                first = false;
            } else {
                num = Matrix.absolute(num);

                if (num != 1) {
                    if (num == (int) num) {
                        sb.append((int) num);
                    } else {
                        sb.append(num);
                    }
                }

            }

            if (i >= 1 && i < 4) {
                if (i > 1) {
                    sb.append("x^").append(i);
                } else {
                    sb.append("x");
                }
            } else if (i >= 4) {
                sb.append("(x - ").append(posisiKnot.getValue(0, i - 4)).append(")^3");
            }

            if (i != jumlahKnot + 3) {
                double coef = Matrix.round3(solution[i + 1][0]);

                if (coef == 0) {
                    continue;
                } else if (coef < 0) {
                    sb.append(" - ");
                } else {
                    sb.append(" + ");
                }
            }
        }

        if (first)

        {
            sb.append("0");
        }

        return sb.toString();
    }

    private static Matrix makeMatrixX(Matrix titikSampel, int jumlahKnot, Matrix posisiKnot) {
        int sizeColX = 4 + jumlahKnot;
        int sizeRowX = titikSampel.getRows();
        int rowY = titikSampel.getRows();
        int returnRow = 4 + jumlahKnot;
        int returnCol = 4 + jumlahKnot + 1;

        Matrix matrixX = new Matrix(sizeRowX, sizeColX);
        Matrix matrixY = new Matrix(rowY, 1);
        Matrix returnM = new Matrix(returnRow, returnCol);

        for (int colX = 0; colX < sizeColX; colX++) {
            for (int rowX = 0; rowX < sizeRowX; rowX++) {
                double num = titikSampel.getValue(rowX, 0);
                if (colX < 4) {
                    matrixX.set(rowX, colX, Matrix.power(num, colX));
                } else {
                    num -= posisiKnot.getValue(0, colX - 4);

                    if (num < 0) {
                        num = 0;
                    }
                    matrixX.set(rowX, colX, Matrix.power(num, 3));

                }
            }
        }

        for (int i = 0; i < rowY; i++) {
            matrixY.set(i, 0, titikSampel.getValue(i, 1));
        }

        Matrix transposeX = Matrix.transposeMatrix(matrixX);
        Matrix matrixXTX = Matrix.perkalianMatriks(transposeX, matrixX);
        Matrix matrixXTY = Matrix.perkalianMatriks(transposeX, matrixY);

        Matrix.cleanZeros(matrixXTX);
        Matrix.cleanZeros(matrixXTY);

        for (int col = 0; col < returnCol; col++) {
            for (int row = 0; row < returnRow; row++) {
                if (col != returnCol - 1) {
                    returnM.set(row, col, matrixXTX.getValue(row, col));
                } else {
                    returnM.set(row, col, matrixXTY.getValue(row, 0));

                }
            }
        }

        return returnM;
    }

    private static double regresiResult(double num, double[][] solution, int jumlahKnot, Matrix posisiKnot) {
        double result = 0;

        for (int i = 0; i < jumlahKnot + 4; i++) {
            double x = num;
            double addition = 0;
            double sol = Matrix.round3(solution[i][0]);
            if (sol != 0) {
                if (i == 0) {
                    addition += sol;
                } else {
                    if (i >= 1 && i < 4) {
                        if (i == 1) {
                            addition += x * sol;
                        } else {
                            addition += Matrix.power(x, i) * sol;
                        }
                    } else if (i >= 4) {
                        x -= posisiKnot.getValue(0, i - 4);
                        if (x > 0) {
                            addition += Matrix.power(x, 3) * sol;
                        }
                    }
                }
            }
            System.out.println("" + addition);
            result += addition;
        }
        return Matrix.round3(result);
    }

    public static Matrix eliminasiGaussTitik(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();
        int nVar = c - 1;
        int pivotRow = 0;

        for (int i = 0; i < nVar && pivotRow < r; i++) {
            int curr = pivotRow;
            int curc = i;

            int bestPivot = Matrix.partialPivoting(m, curr, curc);

            if (bestPivot == -1) {
                continue;
            } else if (bestPivot != curr) {
                m = Matrix.switchRow(m, curr, bestPivot);
            }

            double num = m.getValue(curr, curc);

            // Normalize the row
            if (num != 1) {
                m = Matrix.multiplyRow(m, curr, 1 / (num));
                num = m.getValue(curr, curc);
            }

            // Make row below collumn to zero
            for (int j = curr + 1; j < r; j++) {
                double multiplier = m.getValue(j, curc);
                m = Matrix.subtractRowbyRow(m, j, curr, multiplier);
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

    private static void outputRegression() {
        System.out.print("Nama file output (termasuk .txt):");
        String name = sc.nextLine().trim();

        try (PrintWriter out = new PrintWriter("../../../test/" + name, StandardCharsets.UTF_8)) {
            out.print(content);
            System.out.println("Hasil disimpan di " + name);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan file: " + e.getMessage());
        }
    }

}
