package algeo.modules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class PolyInterpolationHandler {

    public static void run(Scanner sc) {

        System.out.println("=== Interpolasi Polinom ===");
        System.out.println();
        System.out.println("Sumber input:");
        System.out.println("1. Keyboard");
        System.out.println("2. File .txt");
        System.out.print("Pilih: ");
        int sumber;

        try {
            sumber = Integer.parseInt(sc.nextLine());
        } catch(NumberFormatException e) {
            System.out.println("Pilihan harus berupa angka.");
            return;}

        if (sumber != 1 && sumber != 2) {
            System.out.println("Pilihan sumber input tidak valid.");
            return;
        }

        Matrix data;
        double x;

        try {
            if (sumber == 1) {

                System.out.println();
                System.out.print("Masukkan jumlah titik: ");
                int n = Integer.parseInt(sc.nextLine());
                if (n < 2 || n > 10) {
                    throw new IllegalArgumentException(
                        "Jumlah titik harus antara 2 sampai 10."
                    );
                }

                data = new Matrix(n, 2);

                System.out.println();
                System.out.println(
                    "Masukkan pasangan x dan y untuk setiap titik."
                );
                System.out.println("Format: x y");

                for (int i = 0; i < n; i++) {

                    System.out.print("Titik " + (i + 1) + ": ");

                    String line = sc.nextLine().trim();
                    line = line.replace(",", ".");

                    String[] tokens = line.split("\\s+");

                    if (tokens.length != 2) {
                        throw new IllegalArgumentException(
                            "Setiap titik harus memiliki tepat 2 nilai: x y."
                        );
                    }

                    data.set(
                        i,
                        0,
                        Double.parseDouble(tokens[0])
                    );

                    data.set(
                        i,
                        1,
                        Double.parseDouble(tokens[1])
                    );
                }

                System.out.println();
                System.out.print(
                    "Masukkan nilai x yang ingin dicari: "
                );

                x = Double.parseDouble(
                    sc.nextLine().replace(",", ".")
                );

            } else {

                System.out.println();
                System.out.print("Masukkan nama/path file: ");
                String filename = sc.nextLine();
                data = readDataFromFile(filename);
                System.out.println();
                System.out.print(
                    "Masukkan nilai x yang ingin dicari: ");

                x = Double.parseDouble(
                    sc.nextLine().replace(",", ".")
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

        try {

            if (hasDuplicateX(data)) {
                throw new IllegalArgumentException(
                    "Nilai x tidak boleh sama."
                );
            }

            System.out.println();
            System.out.println("Data titik:");
            data.printMatrix();

            System.out.println();
            System.out.println(
                "Perhitungan interpolasi:"
            );
            PolynomialInterpolation.printSteps(data, x);

            double hasil = PolynomialInterpolation.interpolatePoly(data, x);

            System.out.println();
            System.out.println(
                "Nilai interpolasi pada x = " + formatNumber(x)
            );

            System.out.println(
                "f(" + formatNumber(x) + ") = "
                + formatNumber(hasil)
            );

            StringBuilder hasilOutput =
                new StringBuilder();

            hasilOutput.append(
                "=== Interpolasi Polinom ===\n\n"
            );

            hasilOutput.append("Data titik:\n");

            for (int i = 0; i < data.getRows(); i++) {

                hasilOutput.append("(")
                    .append(formatNumber(data.getValue(i, 0)))
                    .append(", ")
                    .append(formatNumber(data.getValue(i, 1)))
                    .append(")\n");
            }
            hasilOutput.append("\n");

            hasilOutput.append(
                "Nilai x = "
            ).append(formatNumber(x)).append("\n");
            hasilOutput.append(
                "Hasil interpolasi = "
            ).append(formatNumber(hasil)).append("\n");

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

        } catch (IllegalArgumentException e) {

            System.out.println(e.getMessage());

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
        String line;
        double[][] temp = new double[10][2];

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            if (rows >= 10) {
                reader.close();

                throw new IllegalArgumentException(
                    "Jumlah titik tidak boleh lebih dari 10."
                );
            }

            line = line.replace(",", ".");

            String[] tokens =
                line.split("\\s+");

            if (tokens.length != 2) {

                reader.close();

                throw new IllegalArgumentException(
                    "Setiap baris harus berisi x dan y."
                );
            }

            temp[rows][0] =
                Double.parseDouble(tokens[0]);

            temp[rows][1] =
                Double.parseDouble(tokens[1]);

            rows++;
        }

        reader.close();

        if (rows < 2) {

            throw new IllegalArgumentException(
                "Minimal diperlukan 2 titik."
            );
        }

        Matrix data =
            new Matrix(rows, 2);

        for (int i = 0; i < rows; i++) {

            data.set(
                i,
                0,
                temp[i][0]
            );

            data.set(
                i,
                1,
                temp[i][1]
            );
        }

        return data;
    }


    private static boolean hasDuplicateX(Matrix data) {

        for (int i = 0; i < data.getRows(); i++) {

            for (int j = i + 1; j < data.getRows(); j++) {

                if (
                    data.getValue(i, 0)
                    == data.getValue(j, 0)
                ) {
                    return true;
                }
            }
        }

        return false;
    }


    private static String formatNumber(double value) {

        java.text.DecimalFormatSymbols symbols =
            new java.text.DecimalFormatSymbols(
                java.util.Locale.US
            );

        java.text.DecimalFormat df =
            new java.text.DecimalFormat(
                "0.###",
                symbols
            );

        if (value == 0) {
            value = 0;
        }

        return df.format(value);
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