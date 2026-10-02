package algeo.modules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CubicInterpolationHandler {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        run(sc);
        sc.close();
    }
    public static void run(Scanner sc) {

        System.out.println("=== Interpolasi Splina Kubik Natural ===");
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
                    "Masukkan " + n + " pasang titik, satu titik per baris dengan format: x y"
                );
                System.out.println("Pecahan boleh ditulis a/b, contoh: 1/2");

                int i = 0;

                while (i < n) {

                    String line = sc.nextLine().trim();

                    if (line.isEmpty()) {
                        continue;
                    }

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
                        parseNumber(tokens[0])
                    );

                    data.set(
                        i,
                        1,
                        parseNumber(tokens[1])
                    );

                    i++;
                }

            } else {

                System.out.println();
                System.out.print("Masukkan nama/path file: ");
                String filename = sc.nextLine();
                data = readDataFromFile(filename);
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

            if (!isXIncreasing(data)) {
                throw new IllegalArgumentException(
                    "Nilai x harus terurut naik dan tidak boleh sama."
                );
            }

            // {segmenFunc, a, b, c, d}
            Matrix[] hasil = CubicInterpolationFunction.fillSegmen(data);
            Matrix segmen = hasil[0];
            Matrix a = hasil[1];
            Matrix b = hasil[2];
            Matrix c = hasil[3];
            Matrix d = hasil[4];

            int n = data.getRows();
            double batasBawah = data.getValue(0, 0);
            double batasAtas = data.getValue(n - 1, 0);

            StringBuilder hasilOutput =
                new StringBuilder();

            hasilOutput.append(
                "=== Interpolasi Splina Kubik Natural ===\n\n"
            );
            hasilOutput.append(
                "Metode interpolasi: Interpolasi Splina Kubik Natural\n\n"
            );

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

            String domainInterpolasi =
                "[" + formatNumber(batasBawah) + ", "
                + formatNumber(batasAtas) + "]";

            System.out.println();
            System.out.println(
                "Domain interpolasi: " + domainInterpolasi
            );

            hasilOutput.append("Domain interpolasi: ")
                .append(domainInterpolasi).append("\n\n");

            System.out.println();
            System.out.println("Persamaan setiap segmen:");
            System.out.println();

            hasilOutput.append("Persamaan setiap segmen:\n\n");

            for (int p = 0; p < n - 1; p++) {

                double xi = data.getValue(p, 0);
                double xiBerikut = data.getValue(p + 1, 0);

                String g = "(x"
                    + CubicInterpolationFunction.plusOrmin(0 - xi)
                    + formatNumber(Matrix.absolute(xi)) + ")";

                String bentukSegmen = "S" + p + "(x) = "
                    + formatNumber(a.getValue(p, 0))
                    + CubicInterpolationFunction.plusOrmin(b.getValue(p, 0))
                    + formatNumber(Matrix.absolute(b.getValue(p, 0))) + g
                    + CubicInterpolationFunction.plusOrmin(c.getValue(p, 0))
                    + formatNumber(Matrix.absolute(c.getValue(p, 0))) + g + "^2"
                    + CubicInterpolationFunction.plusOrmin(d.getValue(p, 0))
                    + formatNumber(Matrix.absolute(d.getValue(p, 0))) + g + "^3";

                String bentukBiasa = "S" + p + "(x) = "
                    + formatNumber(segmen.getValue(p, 0))
                    + CubicInterpolationFunction.plusOrmin(segmen.getValue(p, 1))
                    + formatNumber(Matrix.absolute(segmen.getValue(p, 1))) + "x"
                    + CubicInterpolationFunction.plusOrmin(segmen.getValue(p, 2))
                    + formatNumber(Matrix.absolute(segmen.getValue(p, 2))) + "x^2"
                    + CubicInterpolationFunction.plusOrmin(segmen.getValue(p, 3))
                    + formatNumber(Matrix.absolute(segmen.getValue(p, 3))) + "x^3";

                String domainSegmen =
                    "[" + formatNumber(xi) + ", "
                    + formatNumber(xiBerikut) + "]";

                System.out.println(
                    "Segmen ke-" + p + ", domain " + domainSegmen
                );
                System.out.println(bentukSegmen);
                System.out.println("atau");
                System.out.println(bentukBiasa);
                System.out.println();

                hasilOutput.append("Segmen ke-").append(p)
                    .append(", domain ").append(domainSegmen).append("\n");
                hasilOutput.append(bentukSegmen).append("\n");
                hasilOutput.append("atau\n");
                hasilOutput.append(bentukBiasa).append("\n\n");
            }

            System.out.println("Nilai turunan kedua pada setiap knot:");
            System.out.println("S''(xi) = 2ci");
            System.out.println();

            for (int p = 0; p < n; p++) {

                System.out.println(
                    "S''(x" + p + ") = S''("
                    + formatNumber(data.getValue(p, 0)) + ") = "
                    + formatNumber(2 * c.getValue(p, 0))
                );
            }

            double xt = 0;
            boolean valid = false;

            while (!valid) {

                System.out.println();
                System.out.print(
                    "Masukkan nilai x yang ingin dievaluasi: "
                );

                try {
                    xt = parseNumber(
                        sc.nextLine().trim().replace(",", ".")
                    );

                    if (xt < batasBawah || xt > batasAtas) {
                        System.out.println(
                            "x = " + formatNumber(xt)
                            + " berada di luar domain interpolasi "
                            + domainInterpolasi
                            + ". Silakan masukkan ulang."
                        );
                    } else {
                        valid = true;
                    }

                } catch (IllegalArgumentException e) {
                    System.out.println(
                        "Input harus berupa angka atau pecahan a/b. Silakan masukkan ulang."
                    );
                }
            }

            int seg = 0;
            while (seg < n - 2 && xt > data.getValue(seg + 1, 0)) {
                seg++;
            }

            double t = xt - data.getValue(seg, 0);
            double hasilEvaluasi = a.getValue(seg, 0)
                + b.getValue(seg, 0) * t
                + c.getValue(seg, 0) * t * t
                + d.getValue(seg, 0) * t * t * t;

            System.out.println();
            System.out.println("Hasil evaluasi:");
            System.out.println(
                "x = " + formatNumber(xt) + " berada pada segmen S" + seg
            );
            System.out.println(
                "S(" + formatNumber(xt) + ") = S" + seg + "("
                + formatNumber(xt) + ") = " + formatNumber(hasilEvaluasi)
            );

            hasilOutput.append("Hasil evaluasi:\n");
            hasilOutput.append("S(").append(formatNumber(xt))
                .append(") = S").append(seg).append("(")
                .append(formatNumber(xt)).append(") = ")
                .append(formatNumber(hasilEvaluasi)).append("\n");

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

        } catch (IllegalArgumentException e) {

            System.out.println(e.getMessage());

        } catch (IOException e) {

            System.out.println(
                "Gagal menyimpan file: "
                + e.getMessage()
            );
        }
    }


    // menerima angka biasa (2, -3.5) dan pecahan (1/2, -3/4)
    private static double parseNumber(String token) {

        if (token.contains("/")) {

            String[] bagian = token.split("/");

            if (bagian.length != 2) {
                throw new NumberFormatException(
                    "Format pecahan tidak valid: " + token
                );
            }

            double pembilang = Double.parseDouble(bagian[0]);
            double penyebut = Double.parseDouble(bagian[1]);

            if (penyebut == 0) {
                throw new IllegalArgumentException(
                    "Penyebut pecahan tidak boleh nol: " + token
                );
            }

            return pembilang / penyebut;
        }

        return Double.parseDouble(token);
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
                parseNumber(tokens[0]);

            temp[rows][1] =
                parseNumber(tokens[1]);

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


    private static boolean isXIncreasing(Matrix data) {

        for (int i = 1; i < data.getRows(); i++) {

            if (
                data.getValue(i, 0)
                <= data.getValue(i - 1, 0)
            ) {
                return false;
            }
        }

        return true;
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