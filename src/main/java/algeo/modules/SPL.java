package algeo.modules;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SPL {
    public static void main(String[] args) {
        start();
    }

    private static Scanner sc = new Scanner(System.in);

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

    }

    private static void fileInput() {
        System.out.println("Silahkan masukkan nama file SPL:");
    }

    private static void printSPL(Matrix matrix) {
        int r = matrix.getRows();
        int c = matrix.getCols();

        System.out.println("Hasil SPL dari input:");

        /* X1 + 2X2 + 3X3 = 4 */
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                double num = matrix.getValue(i, j);
                if (j != c - 1) {
                    if (j != 0) {
                        if (num > 0) {
                            System.out.printf("+ ");
                        } else if (num < 0) {
                            System.out.printf("- ");
                            num *= -1;
                        } else {
                            continue;
                        }
                    }

                    if (num == (int) num) {
                        if (num > 1) {
                            System.out.printf(((int) num) + "X" + (j + 1) + " ");
                        } else if ((int) num == 1) {
                            System.out.printf("X" + (i + 1) + " ");
                        }
                    } else {
                        System.out.printf(num + "X" + (i + 1) + " ");
                    }
                } else {

                    if (num == (int) num) {
                        System.out.printf("= " + ((int) num));
                    } else {
                        System.out.printf("= " + num);
                    }
                }
            }
            System.out.println();
        }
    }
}