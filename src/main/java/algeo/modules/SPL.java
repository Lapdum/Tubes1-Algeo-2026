package algeo.modules;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SPL {
    public static void main(String[] args) {
        start();
    }

    static Scanner sc = new Scanner(System.in);
    static int r, c = 0;
    static double[][] matrix = new double[11][12];

    static void start() {
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

    static void manual() {
        System.out.println("---------------------------------");
        System.out.println("|     Sistem Persamaan Linear    |");
        System.out.println("---------------------------------");
        System.out.println();

        System.out.println("Silahkan masukkan sistem persamaan linear anda dalam bentuk matriks augmented.");
        System.out.println("Ukuran maksimal matriks augmented adalah 11*12, termasuk hasil baris.");
        System.out.println("Apabila sudah selesai, silahkan ketik selain 'DONE' setelah baris terakhir.\n");
        System.out.println("Contoh input matriks augmented:");
        System.out.println("1 2 3 4");
        System.out.println("5 6 7 8");
        System.out.println("9 10 11 12");
        System.out.println("DONE\n");
        System.out.println("(Hasil input adalah matriks augmented berukuran 3*4)");
        System.out.println();

        System.out.println("Input matriks augmented:\n");

        boolean inputArray = true;

        while (inputArray) {
            String row = sc.nextLine();
            if (row.equals("DONE")) {
                inputArray = false;
            } else {
                row = row.replace(',', '.');
                String[] token = row.split(" ");
                extractNumber(token);
            }
        }

        System.out.println(r + " " + c + "\n\n\n");

        /*
         * for (int i = 0; i < r; i++) {
         * for (int j = 0; j < c; j++) {
         * System.out.printf(matrix[i][j] + " ");
         * }
         * System.out.println();
         * }
         */
        printSPL();

    }

    static void fileInput() {
        System.out.println("Silahkan masukkan nama file SPL:");
    }

    static void extractNumber(String[] token) {
        /*
         * Pattern p = Pattern.compile("(-)?(([//d])(0)|[1-9][0-9]*)(\\.)?([0-9]+)");
         * Matcher m = p.matcher(S);
         * 
         * while (m.find()) {
         * System.out.println("[" + S + "]");
         * System.out.println(r + " " + c + "\n\n\n");
         * 
         * matrix[r][c] = Double.parseDouble(m.group());
         * c += 1;
         * }
         * r += 1;
         */

        for (int i = 0; i < token.length; i++) {
            matrix[r][i] = Double.parseDouble(token[i].trim());
        }
        c = token.length;
        r += 1;
    }

    static void printSPL() {
        System.out.println("Hasil SPL dari input:");

        /* X1 + 2X2 + 3X3 = 4 */
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                double num = matrix[i][j];
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