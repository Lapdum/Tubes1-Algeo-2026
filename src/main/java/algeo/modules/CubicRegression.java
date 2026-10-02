package algeo.modules;

import java.util.*;

public class CubicRegression {

    private static Scanner sc = new Scanner(System.in);

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

        Matrix X = makeMatrixX(titikSampel, jumlahKnot, posisiKnot);

        X = SPL.eliminasiGauss(X);
        double[][] solution = SPL.substitusiMundur(X);
        Matrix sol = Matrix.doubletoMatrix(solution, solution.length, solution[0].length);

        System.out.println("Printing solution:");
        Matrix.printMatrix(sol);

        if (SPL.noSolution(X)) {
            SPL.printSPL(X);
            System.out.println();
            System.out.println("Regresi tidak memiliki solusi");
        } else {
            SPL.buildAnswer(X, solution);
            System.out.println();
            System.out.println("Regresi memiliki solusi:");
            SPL.printSolution(X.answerSPL);
        }

        System.out.printf("Posisi knot yang digunakan : ");
        for (int i = 0; i < jumlahKnot; i++) {
            System.out.printf("knot " + (i + 1) + " = " + posisiKnot.getValue(0, i));
        }

        System.out.println("\nPersamaan regresi splina: ");
        printRegresi(solution, jumlahKnot, posisiKnot);
        System.out.println("\n");

        boolean inputValidation = true;
        sc.nextLine();
        System.out.printf("Silahkan input nilai untuk melihat hasil prediksi persamaan regresi:\n");
        System.out.printf("(Jika ingin menyelesaikan uji prediksi, tekan 'ENTER' pada nilai kosong.)\n");

        while (inputValidation) {
            double num;
            double res;
            try {
                System.out.printf("nilai Xt: ");
                String Xt = sc.nextLine();

                if (Xt.length() == 0) {
                    inputValidation = false;
                } else {
                    Xt = Xt.replace(",", ".");
                    if (Xt.contains("/")) {
                        String[] numbers = Xt.split("/");
                        double numerator = Double.parseDouble(numbers[0]);
                        double denominator = Double.parseDouble(numbers[1]);
                        double decimal = numerator / denominator;
                        num = decimal;
                    } else {
                        num = Double.parseDouble(Xt);
                    }

                    res = regresiResult(num, solution, jumlahKnot, posisiKnot);
                    System.out.printf("\nHasilnya dari y(" + num + ") adalah " + res + "\n\n");
                }

            } catch (InputMismatchException e) {
                System.out.println("Input harus berupa angka!");
                sc.next();
            }
        }

    }

    private static void fileInput() {

    }

    public static void printRegresi(double[][] solution, int jumlahKnot, Matrix posisiKnot) {
        System.out.printf("y(x) = ");
        boolean first = true;
        for (int i = 0; i < jumlahKnot + 4; i++) {
            double num = Matrix.round3(solution[i][0]);
            if (num != 0) {
                if (first) {
                    if (num == (int) num) {
                        System.out.printf("" + (int) num);
                    } else {
                        System.out.printf("" + num);
                    }
                    first = false;
                } else {
                    num = Matrix.absolute(num);

                    if (num != 1) {
                        if (num == (int) num) {
                            System.out.printf("" + (int) num);
                        } else {
                            System.out.printf("" + num);
                        }
                    }
                }

                if (i >= 1 && i < 4) {
                    if (i > 1) {
                        System.out.printf("x^" + i);
                    } else {
                        System.out.printf("x");
                    }
                } else if (i >= 4) {
                    System.out.printf("(x - " + posisiKnot.getValue(0, i - 4) + ")^3");
                }

                if (i != jumlahKnot + 3) {
                    double coef = Matrix.round3(solution[i + 1][0]);

                    if (coef == 0) {
                        continue;
                    } else if (coef < 0) {
                        System.out.printf(" - ");
                    } else {
                        System.out.printf(" + ");
                    }
                }
            }

            if (first)

            {
                System.out.printf("0");
            }
        }
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

        System.out.println("Matrix X");
        Matrix.printMatrix(matrixX);
        System.out.println();
        System.out.println("Matrix Y");
        Matrix.printMatrix(matrixY);

        Matrix transposeX = Matrix.transposeMatrix(matrixX);

        System.out.println();
        System.out.println("Matrix transpose X");
        Matrix.printMatrix(transposeX);

        Matrix matrixXTX = Matrix.perkalianMatriks(transposeX, matrixX);
        Matrix matrixXTY = Matrix.perkalianMatriks(transposeX, matrixY);

        Matrix.cleanZeros(matrixXTX);
        Matrix.cleanZeros(matrixXTY);

        System.out.println();
        System.out.println("Matrix XTX");
        Matrix.printMatrix(matrixXTX);
        System.out.println();
        System.out.println("Matrix XTY");
        Matrix.printMatrix(matrixXTY);

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
}
