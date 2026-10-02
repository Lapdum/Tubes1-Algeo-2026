package algeo.modules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner;

public class InverseHandler {

    public static void run(Scanner sc) {

        System.out.println("=== Matriks Balikan ===");
        System.out.println("1. Augmented");
        System.out.println("2. Adjoint");
        System.out.print("Pilih metode: ");

        int pilihan;

        try {
            pilihan = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Pilihan harus berupa angka.");
            return;
        }

        if (pilihan != 1 && pilihan != 2) {
            System.out.println("Pilihan metode tidak valid.");
            return;
        }

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

        Matrix matrix;

        try {

            if (sumber == 1) {

                System.out.println();
                System.out.println("Masukkan matriks:");
                System.out.println("Gunakan spasi untuk memisahkan elemen.");
                System.out.println("Tekan Enter kosong untuk selesai.");

                matrix = Matrix.inputMatrix();

            } else {

                System.out.println();
                System.out.print("Masukkan nama/path file: ");

                String filename = sc.nextLine();

                matrix = readMatrixFromFile(filename);
            }

        } catch (NumberFormatException e) {

            System.out.println("Format angka pada input tidak valid.");
            return;

        } catch (IOException e) {

            System.out.println("File tidak dapat dibaca: " + e.getMessage());
            return;

        } catch (IllegalArgumentException e) {

            System.out.println(e.getMessage());
            return;
        }


        /*
         * Menjalankan algoritma sambil menyimpan output langkah
         * ke dalam String.
         */
        String hasilOutput;

        try {

            java.io.ByteArrayOutputStream output =
                new java.io.ByteArrayOutputStream();

            java.io.PrintStream originalOut = System.out;

            Matrix inverse;

            try {

                System.setOut(new java.io.PrintStream(output));

                if (pilihan == 1) {
                    inverse = Inverse.inverseGaussJordan(matrix);
                } else {
                    inverse = Inverse.inverseAdjoint(matrix);
                }

            } finally {

                System.setOut(originalOut);
            }

        StringBuilder header = new StringBuilder();

        header.append("=== Matriks Balikan ===\n");

        if (pilihan == 1) {
            header.append("Metode: Augmented [A | I] dengan Gauss-Jordan\n");
        } else {
            header.append("Metode: Adjoint\n");
        }

        header.append("\nInput Matriks:\n");

        java.text.DecimalFormatSymbols symbols =
            new java.text.DecimalFormatSymbols(java.util.Locale.US);

        java.text.DecimalFormat df =
            new java.text.DecimalFormat("0.###", symbols);

        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                double value = matrix.getValue(i, j);

                if (value == 0) {
                    value = 0;
                }

                header.append(df.format(value));

                if (j < matrix.getCols() - 1) {
                    header.append(" ");
                }
            }

            header.append("\n");
        }

        header.append("\n");

        hasilOutput = header.toString() + output.toString();

        System.out.print(hasilOutput);

            // Tambahkan hasil inverse
            hasilOutput += "\nMatriks Inverse:\n";

            System.out.println();
            System.out.println("Matriks Inverse:");

        

            for (int i = 0; i < inverse.getRows(); i++) {

                for (int j = 0; j < inverse.getCols(); j++) {

                    double value = inverse.getValue(i, j);

                    if (value == 0) {
                        value = 0;
                    }

                    String formatted = df.format(value);

                    System.out.print(
                        String.format("%8s ", formatted)
                    );

                    hasilOutput += formatted;

                    if (j < inverse.getCols() - 1) {
                        hasilOutput += " ";
                    }
                }

                System.out.println();
                hasilOutput += "\n";
            }

        } catch (IllegalArgumentException e) {

            System.out.println(e.getMessage());
            return;
        }



        System.out.println();
        System.out.print("Masukkan nama/path file output: ");

        String outputFilename = sc.nextLine();

        try {

            writeOutputToFile(outputFilename, hasilOutput);

            System.out.println(
                "Hasil berhasil disimpan ke: " + outputFilename
            );

        } catch (IOException e) {

            System.out.println(
                "Gagal menyimpan file: " + e.getMessage()
            );
        }
    }


    private static Matrix readMatrixFromFile(String filename)
            throws IOException {

        BufferedReader reader =
            new BufferedReader(new FileReader(filename));

        String line;

        int rows = 0;
        int cols = -1;

        double[][] temp = new double[1001][1001];

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            // Lewati baris kosong
            if (line.isEmpty()) {
                continue;
            }

            if (rows >= 1001) {
                reader.close();
                throw new IllegalArgumentException(
                    "Ukuran matriks melebihi batas 1001 baris."
                );
            }

            line = line.replace(",", ".");

            String[] tokens = line.split("\\s+");

            if (cols == -1) {
                cols = tokens.length;

                if (cols > 1001) {
                    reader.close();
                    throw new IllegalArgumentException(
                        "Ukuran matriks melebihi batas 1001 kolom."
                    );
                }

            } else if (tokens.length != cols) {

                reader.close();

                throw new IllegalArgumentException(
                    "Jumlah kolom pada setiap baris harus sama."
                );
            }


            for (int j = 0; j < tokens.length; j++) {

                String token = tokens[j];

                if (token.contains("/")) {

                    String[] fraction = token.split("/");

                    if (fraction.length != 2) {
                        reader.close();

                        throw new NumberFormatException(
                            "Format pecahan tidak valid: " + token
                        );
                    }

                    double numerator =
                        Double.parseDouble(fraction[0]);

                    double denominator =
                        Double.parseDouble(fraction[1]);

                    if (denominator == 0) {
                        reader.close();

                        throw new IllegalArgumentException(
                            "Penyebut tidak boleh 0."
                        );
                    }

                    temp[rows][j] =
                        numerator / denominator;

                } else {

                    temp[rows][j] =
                        Double.parseDouble(token);
                }
            }

            rows++;
        }

        reader.close();


        if (rows == 0 || cols == -1) {
            throw new IllegalArgumentException(
                "File tidak berisi matriks."
            );
        }


        Matrix matrix = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < cols; j++) {

                matrix.set(
                    i,
                    j,
                    temp[i][j]
                );
            }
        }

        return matrix;
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