package algeo.modules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class RegresiHandler {

    public static void run(Scanner sc) {

        System.out.println("=== Regresi Splina Kubik ===");
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
        int jumlahKnot;
        Matrix posisiKnot;

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

            } else {

                System.out.println();
                System.out.print("Masukkan nama/path file: ");
                String filename = sc.nextLine();
                data = readDataFromFile(filename);
            }

            // ---------- input knot ----------
            System.out.println();
            System.out.print("Masukkan jumlah knot: ");
            jumlahKnot = Integer.parseInt(sc.nextLine());

            if (jumlahKnot < 0) {
                throw new IllegalArgumentException(
                    "Jumlah knot tidak boleh negatif."
                );
            }

            double[][] knotArr = new double[1][jumlahKnot];

            if (jumlahKnot > 0) {

                System.out.print(
                    "Masukkan posisi knot (dipisahkan spasi): "
                );

                String line = sc.nextLine().trim();
                line = line.replace(",", ".");

                String[] tokens = line.split("\\s+");

                if (tokens.length != jumlahKnot) {
                    throw new IllegalArgumentException(
                        "Jumlah posisi knot harus " + jumlahKnot + "."
                    );
                }

                for (int i = 0; i < jumlahKnot; i++) {
                    knotArr[0][i] = Double.parseDouble(tokens[i]);
                }
            }

            posisiKnot = Matrix.doubletoMatrix(knotArr, 1, jumlahKnot);

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

            int n = data.getRows();

            StringBuilder hasilOutput =
                new StringBuilder();

            hasilOutput.append(
                "=== Regresi Splina Kubik ===\n\n"
            );

            // ---------- data titik ----------
            System.out.println();
            System.out.println("Data titik:");
            data.printMatrix();

            hasilOutput.append("Data titik:\n");

            for (int i = 0; i < n; i++) {

                hasilOutput.append("(")
                    .append(formatNumber(data.getValue(i, 0)))
                    .append(", ")
                    .append(formatNumber(data.getValue(i, 1)))
                    .append(")\n");
            }
            hasilOutput.append("\n");

            // ---------- posisi knot ----------
            String knotText = "";

            if (jumlahKnot == 0) {
                knotText = "tidak ada knot";
            } else {
                for (int i = 0; i < jumlahKnot; i++) {
                    if (i > 0) {
                        knotText = knotText + "; ";
                    }
                    knotText = knotText + "knot " + (i + 1) + " = "
                        + formatNumber(posisiKnot.getValue(0, i));
                }
            }

            System.out.println();
            System.out.println("Posisi knot: " + knotText);

            hasilOutput.append("Posisi knot: ")
                .append(knotText).append("\n\n");

            // ---------- perhitungan ----------
            Matrix X = CubicRegression.regressX(data, jumlahKnot, posisiKnot);

            System.out.println();
            System.out.println("Sistem persamaan normal [X^T X | X^T y]:");
            Matrix.printMatrix(X);

            X = CubicRegression.eliminasiGaussTitik(X);

            System.out.println();
            System.out.println("Hasil eliminasi Gauss:");
            Matrix.printMatrix(X);

            if (SPL.noSolution(X)) {

                System.out.println();
                System.out.println("Regresi tidak memiliki solusi.");

                hasilOutput.append("Hasil regresi: Regresi tidak memiliki solusi.\n");

            } else {

                double[][] solution = SPL.substitusiMundur(X);
                SPL.buildAnswer(X, solution);

                String koefisien = SPL.solutionToString(X.answerSPL);
                String persamaan = CubicRegression.regresitoString(
                    solution, jumlahKnot, posisiKnot
                );

                String keterangan;
                if (X.freeVar) {
                    keterangan = "Solusi tidak unik, setiap parameter diambil bernilai 0.";
                } else {
                    keterangan = "Solusi tunggal.";
                }

                System.out.println();
                System.out.println("Koefisien regresi:");
                System.out.println(koefisien);
                System.out.println(keterangan);
                System.out.println();
                System.out.println("Persamaan regresi splina kubik:");
                System.out.println(persamaan);

                hasilOutput.append("Koefisien regresi:\n")
                    .append(koefisien).append("\n")
                    .append(keterangan).append("\n\n");
                hasilOutput.append("Persamaan regresi splina kubik:\n")
                    .append(persamaan).append("\n\n");

                // ---------- prediksi, diulang sampai input kosong ----------
                System.out.println();
                System.out.println(
                    "Masukkan nilai x untuk memprediksi y."
                );
                System.out.println(
                    "Tekan ENTER pada input kosong untuk selesai."
                );

                hasilOutput.append("Hasil prediksi:\n");

                boolean selesai = false;
                int jumlahPrediksi = 0;

                while (!selesai) {

                    System.out.print("x = ");
                    String input = sc.nextLine().trim();

                    if (input.isEmpty()) {
                        selesai = true;
                    } else {
                        try {
                            double xt = Double.parseDouble(input.replace(",", "."));
                            double yt = CubicRegression.regresiResult(
                                xt, solution, jumlahKnot, posisiKnot
                            );

                            String prediksi = "y(" + formatNumber(xt) + ") = "
                                + formatNumber(yt);

                            System.out.println(prediksi);
                            hasilOutput.append(prediksi).append("\n");
                            jumlahPrediksi++;

                        } catch (NumberFormatException e) {
                            System.out.println(
                                "Input harus berupa angka. Silakan masukkan ulang."
                            );
                        }
                    }
                }

                if (jumlahPrediksi == 0) {
                    hasilOutput.append("tidak ada prediksi.\n");
                }
            }

            // ---------- simpan ke file ----------
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


    private static String formatNumber(double value) {

        java.text.DecimalFormatSymbols symbols =
            new java.text.DecimalFormatSymbols(
                java.util.Locale.US
            );

        java.text.DecimalFormat df =
            new java.text.DecimalFormat(
                "0.####",
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