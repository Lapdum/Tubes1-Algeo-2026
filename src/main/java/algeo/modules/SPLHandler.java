package algeo.modules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class SPLHandler {

    public static void run(Scanner sc) {

        System.out.println("=== Sistem Persamaan Linier ===");
        System.out.println();
        System.out.println("Sumber input:");
        System.out.println("1. Keyboard");
        System.out.println("2. File .txt");
        System.out.print("Pilih: ");
        int sumber;

        try {
            sumber = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Pilihan harus berupa angka.");
            return;
        }

        if (sumber != 1 && sumber != 2) {
            System.out.println("Pilihan sumber input tidak valid.");
            return;
        }

        Matrix data;
        int metode;

        try {
            if (sumber == 1) {

                System.out.println();
                System.out.print("Masukkan jumlah persamaan (baris): ");
                int rows = Integer.parseInt(sc.nextLine());

                System.out.print(
                    "Masukkan jumlah kolom matriks augmented (jumlah variabel + 1): "
                );
                int cols = Integer.parseInt(sc.nextLine());

                if (rows < 1 || cols < 2) {
                    throw new IllegalArgumentException(
                        "Minimal 1 baris dan 2 kolom."
                    );
                }

                if (rows > 11 || cols > 12) {
                    throw new IllegalArgumentException(
                        "Ukuran maksimal matriks augmented adalah 11x12."
                    );
                }

                double[][] temp = new double[rows][cols];

                System.out.println();
                System.out.println(
                    "Masukkan elemen setiap baris matriks augmented, dipisahkan spasi."
                );

                for (int i = 0; i < rows; i++) {

                    System.out.print("Baris " + (i + 1) + ": ");

                    String line = sc.nextLine().trim();
                    line = line.replace(",", ".");

                    String[] tokens = line.split("\\s+");

                    if (tokens.length != cols) {
                        throw new IllegalArgumentException(
                            "Setiap baris harus memiliki tepat "
                            + cols + " elemen."
                        );
                    }

                    for (int j = 0; j < cols; j++) {
                        temp[i][j] = Double.parseDouble(tokens[j]);
                    }
                }

                data = Matrix.doubletoMatrix(temp, rows, cols);

            } else {

                System.out.println();
                System.out.print("Masukkan nama/path file: ");
                String filename = sc.nextLine();
                data = readDataFromFile(filename);
            }

            System.out.println();
            System.out.println("Metode penyelesaian:");
            System.out.println("1. Eliminasi Gauss");
            System.out.println("2. Eliminasi Gauss-Jordan");
            System.out.println("3. Matriks Balikan");
            System.out.println("4. Kaidah Cramer");
            System.out.print("Pilih: ");

            metode = Integer.parseInt(sc.nextLine());

            if (metode < 1 || metode > 4) {
                throw new IllegalArgumentException(
                    "Pilihan metode tidak valid."
                );
            }

        } catch (NumberFormatException e) {

            System.out.println(
                "Format angka pada input tidak valid."
            );
            return;

        } catch (IOException e) {

            System.out.println(
                "File tidak dapat dibaca: " + e.getMessage()
            );
            return;

        } catch (IllegalArgumentException e) {

            System.out.println(e.getMessage());
            return;
        }

        String namaMetode;

        if (metode == 1) {
            namaMetode = "Eliminasi Gauss";
        } else if (metode == 2) {
            namaMetode = "Eliminasi Gauss-Jordan";
        } else if (metode == 3) {
            namaMetode = "Matriks Balikan";
        } else {
            namaMetode = "Kaidah Cramer";
        }

        int r = data.getRows();
        int c = data.getCols();

        // matriks input dicatat sebelum perhitungan, karena eliminasi bisa mengubah isi matriks
        StringBuilder hasilOutput =
            new StringBuilder();

        hasilOutput.append(
            "=== Sistem Persamaan Linier ===\n\n"
        );
        hasilOutput.append("Metode penyelesaian: ")
            .append(namaMetode).append("\n\n");
        hasilOutput.append("Matriks augmented input:\n");
        hasilOutput.append(Matrix.matrixToString(data)).append("\n");

        System.out.println();
        System.out.println("Metode: " + namaMetode);
        System.out.println();
        SPL.printSPL(data);

        System.out.println("Langkah-langkah perhitungan:");
        System.out.println();

        String hasil;

        if (metode == 1 || metode == 2) {

            Matrix m;

            if (metode == 1) {
                m = SPL.eliminasiGauss(data);
            } else {
                m = SPL.eliminasiGaussJordan(data);
            }

            if (SPL.noSolution(m)) {

                hasil = "Sistem persamaan linear tidak memiliki solusi.";

            } else {

                SPL.buildAnswer(m, SPL.substitusiMundur(m));
                String solusi = SPL.solutionToString(m.answerSPL);

                if (m.freeVar) {
                    hasil = "Sistem persamaan linear memiliki solusi banyak:\n"
                        + solusi + "\n"
                        + "dengan setiap parameter merupakan bilangan riil.";
                } else {
                    hasil = "Sistem persamaan linear memiliki solusi tunggal:\n"
                        + solusi;
                }
            }

        } else if (metode == 3) {

            if (r != c - 1) {

                hasil = "Metode matriks balikan tidak dapat digunakan "
                    + "karena matriks koefisien tidak persegi.";

            } else {

                int n = r;
                Matrix mInverse = SPL.eliminasiInverse(data);

                boolean adaBalikan = true;
                for (int i = 0; i < n; i++) {
                    if (mInverse.flag[i]) {
                        adaBalikan = false;
                    }
                }

                if (!adaBalikan) {

                    hasil = "Metode matriks balikan tidak dapat digunakan "
                        + "karena matriks koefisien tidak memiliki balikan.";

                } else {

                    double[][] inverseArr = new double[n][n];
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            inverseArr[i][j] = mInverse.getValue(i, j + n);
                        }
                    }

                    double[][] bArr = new double[n][1];
                    for (int i = 0; i < n; i++) {
                        bArr[i][0] = data.getValue(i, c - 1);
                    }

                    Matrix inverse = Matrix.doubletoMatrix(inverseArr, n, n);
                    Matrix b = Matrix.doubletoMatrix(bArr, n, 1);
                    Matrix x = Matrix.perkalianMatriks(inverse, b);

                    System.out.println("Matriks balikan A^-1:");
                    Matrix.printMatrix(inverse);
                    System.out.println();

                    System.out.println("Vektor b:");
                    Matrix.printMatrix(b);
                    System.out.println();

                    System.out.println("X = A^-1 x b:");
                    Matrix.printMatrix(x);
                    System.out.println();

                    double[][] solusiArr = new double[n][1];
                    for (int i = 0; i < n; i++) {
                        solusiArr[i][0] = x.getValue(i, 0);
                    }

                    SPL.buildAnswer(data, solusiArr);

                    hasil = "Sistem persamaan linear memiliki solusi tunggal:\n"
                        + SPL.solutionToString(data.answerSPL);
                }
            }

        } else {

            if (r != c - 1) {

                hasil = "Kaidah Cramer tidak dapat digunakan "
                    + "karena matriks koefisien tidak persegi.";

            } else {

                Matrix m = SPL.kaidahCramer(data);

                if (m == null) {
                    hasil = "Kaidah Cramer tidak dapat digunakan karena det(A) = 0.";
                } else {
                    SPL.buildAnswer(m, m.determinantCramer);
                    hasil = "Sistem persamaan linear memiliki solusi tunggal:\n"
                        + SPL.solutionToString(m.answerSPL);
                }
            }
        }

        System.out.println();
        System.out.println("Hasil:");
        System.out.println(hasil);

        hasilOutput.append("Hasil SPL:\n")
            .append(hasil).append("\n");

        try {

            System.out.println();
            System.out.print(
                "Masukkan nama/path file output: "
            );

            String outputFilename = sc.nextLine();

            writeOutputToFile(
                outputFilename,
                hasilOutput.toString()
            );
            System.out.println(
                "Hasil berhasil disimpan ke: "
                + outputFilename
            );

        } catch (IOException e) {

            System.out.println(
                "Gagal menyimpan file: "
                + e.getMessage()
            );
        }
    }


    private static Matrix readDataFromFile(String filename)
            throws IOException {

        BufferedReader reader =
            new BufferedReader(
                new FileReader(filename)
            );
        int rows = 0;
        int cols = 0;
        String line;
        double[][] temp = new double[11][12];

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            if (rows >= 11) {
                reader.close();

                throw new IllegalArgumentException(
                    "Jumlah baris tidak boleh lebih dari 11."
                );
            }

            line = line.replace(",", ".");

            String[] tokens =
                line.split("\\s+");

            if (rows == 0) {
                cols = tokens.length;

                if (cols < 2 || cols > 12) {
                    reader.close();

                    throw new IllegalArgumentException(
                        "Jumlah kolom harus antara 2 sampai 12."
                    );
                }
            }

            if (tokens.length != cols) {

                reader.close();

                throw new IllegalArgumentException(
                    "Setiap baris harus memiliki jumlah elemen yang sama."
                );
            }

            for (int j = 0; j < cols; j++) {
                temp[rows][j] =
                    Double.parseDouble(tokens[j]);
            }

            rows++;
        }

        reader.close();

        if (rows < 1) {

            throw new IllegalArgumentException(
                "File tidak berisi matriks."
            );
        }

        double[][] hasil = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                hasil[i][j] = temp[i][j];
            }
        }

        return Matrix.doubletoMatrix(hasil, rows, cols);
    }


    private static void writeOutputToFile(
            String filename,
            String output)
            throws IOException {

        PrintWriter writer =
            new PrintWriter(
                new FileWriter(filename)
            );

        writer.print(output);
        writer.close();
    }

}