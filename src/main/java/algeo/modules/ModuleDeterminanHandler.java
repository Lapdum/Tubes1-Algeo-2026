package algeo.modules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Scanner;

public class ModuleDeterminanHandler {

    public static void run(Scanner sc) {

        System.out.println("=== Determinan Matriks ===");
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
        int k = 1;

        try {
            if (sumber == 1) {

                System.out.println();
                System.out.print("Masukkan jumlah baris: ");
                int rows = Integer.parseInt(sc.nextLine());

                System.out.print("Masukkan jumlah kolom: ");
                int cols = Integer.parseInt(sc.nextLine());

                if (rows < 1 || cols < 1) {
                    throw new IllegalArgumentException(
                        "Jumlah baris dan kolom minimal 1."
                    );
                }

                data = new Matrix(rows, cols);

                System.out.println();
                System.out.println(
                    "Masukkan elemen setiap baris, dipisahkan spasi."
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
                        data.set(
                            i,
                            j,
                            Double.parseDouble(tokens[j])
                        );
                    }
                }

            } else {

                System.out.println();
                System.out.print("Masukkan nama/path file: ");
                String filename = sc.nextLine();
                data = readDataFromFile(filename);
            }

            System.out.println();
            System.out.println("Metode perhitungan determinan:");
            System.out.println("1. Ekspansi kofaktor baris");
            System.out.println("2. Ekspansi kofaktor kolom");
            System.out.println("3. Reduksi baris");
            System.out.print("Pilih: ");

            metode = Integer.parseInt(sc.nextLine());

            if (metode < 1 || metode > 3) {
                throw new IllegalArgumentException(
                    "Pilihan metode tidak valid."
                );
            }

            if (metode != 3 && data.rows == data.cols && data.rows > 1) {

                if (metode == 1) {
                    System.out.print(
                        "Ekspansi sepanjang baris ke- (1 sampai "
                        + data.rows + "): "
                    );
                } else {
                    System.out.print(
                        "Ekspansi sepanjang kolom ke- (1 sampai "
                        + data.cols + "): "
                    );
                }

                k = Integer.parseInt(sc.nextLine());

                if (k < 1 || k > data.rows) {
                    throw new IllegalArgumentException(
                        "Nomor baris/kolom tidak valid."
                    );
                }
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
        String label;

        if (metode == 1) {
            namaMetode = "Ekspansi kofaktor baris";
            label = "baris";
        } else if (metode == 2) {
            namaMetode = "Ekspansi kofaktor kolom";
            label = "kolom";
        } else {
            namaMetode = "Reduksi baris";
            label = "";
        }

        try {

            System.out.println();
            System.out.println("Metode: " + namaMetode);
            System.out.println();
            System.out.println("Matriks input:");
            data.printMatrix();

            StringBuilder hasilOutput =
                new StringBuilder();

            hasilOutput.append(
                "=== Determinan Matriks ===\n\n"
            );

            hasilOutput.append("Metode perhitungan determinan: ")
                .append(namaMetode).append("\n\n");

            hasilOutput.append("Matriks input:\n");

            for (int i = 0; i < data.rows; i++) {
                for (int j = 0; j < data.cols; j++) {
                    hasilOutput.append(
                        formatNumber(data.getValue(i, j))
                    ).append("\t");
                }
                hasilOutput.append("\n");
            }
            hasilOutput.append("\n");

            if (data.rows != data.cols) {

                System.out.println();
                System.out.println(
                    "Matriks tidak memiliki determinan."
                );

                hasilOutput.append(
                    "Hasil determinan: Matriks tidak memiliki determinan.\n"
                );

            } else {

                int n = data.rows;
                double det = 0;

                System.out.println();
                System.out.println("Langkah-langkah perhitungan:");
                System.out.println();

                if (metode == 3) {

                    double[][] salinan = new double[n][n];
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            salinan[i][j] = data.getValue(i, j);
                        }
                    }
                    Matrix m = Matrix.doubletoMatrix(salinan, n, n);

                    int countSwitch = 0;
                    boolean adaPivotNol = false;

                    System.out.println(
                        "Reduksi baris hingga matriks berbentuk segitiga atas."
                    );
                    System.out.println(
                        "Pertukaran baris membalik tanda determinan, "
                        + "operasi Ri - k x Rj tidak mengubah determinan."
                    );
                    System.out.println();

                    for (int kol = 0; kol < n; kol++) {

                        int bestPivot = Matrix.partialPivoting(m, kol, kol);

                        if (bestPivot == -1) {
                            System.out.println(
                                "Kolom " + (kol + 1)
                                + " tidak memiliki pivot tidak nol, sehingga det(A) = 0."
                            );
                            adaPivotNol = true;
                            break;
                        }

                        if (bestPivot != kol) {
                            m = Matrix.switchRow(m, kol, bestPivot);
                            countSwitch++;

                            System.out.println(
                                "Tukar baris " + (kol + 1)
                                + " dengan baris " + (bestPivot + 1) + ":"
                            );
                            m.printMatrix();
                            System.out.println();
                        }

                        double pivot = m.getValue(kol, kol);

                        for (int j = kol + 1; j < n; j++) {
                            double multiplier = m.getValue(j, kol) / pivot;

                            if (multiplier != 0) {
                                m = Matrix.subtractRowbyRow(m, j, kol, multiplier);

                                System.out.println(
                                    "R" + (j + 1) + " = R" + (j + 1) + " - ("
                                    + formatNumber(multiplier) + ") x R" + (kol + 1) + ":"
                                );
                                m.printMatrix();
                                System.out.println();
                            }
                        }
                    }

                    if (adaPivotNol) {
                        det = 0;
                    } else {
                        String diagonal = "";
                        for (int i = 0; i < n; i++) {
                            if (i > 0) {
                                diagonal = diagonal + " x ";
                            }
                            diagonal = diagonal + "(" + formatNumber(m.getValue(i, i)) + ")";
                        }

                        det = ModuleDeterminan.reduksiBaris(m, countSwitch);

                        System.out.println(
                            "Jumlah pertukaran baris: " + countSwitch
                        );
                        System.out.println(
                            "det(A) = (-1)^" + countSwitch + " x " + diagonal
                            + " = " + formatNumber(det)
                        );
                    }

                } else if (n == 1) {

                    det = data.getValue(0, 0);

                    System.out.println(
                        "Matriks berukuran 1x1, det(A) = a11 = "
                        + formatNumber(det)
                    );

                } else {

                    System.out.println(
                        "Ekspansi kofaktor sepanjang " + label
                        + " ke-" + k
                    );
                    System.out.println(
                        "det(A) = jumlah a_ij * C_ij, dengan C_ij = (-1)^(i+j) * M_ij"
                    );
                    System.out.println();

                    String penjumlahan = "";

                    for (int t = 0; t < n; t++) {

                        int i;
                        int j;

                        if (metode == 1) {
                            i = k - 1;
                            j = t;
                        } else {
                            i = t;
                            j = k - 1;
                        }

                        Matrix sub = ModuleDeterminan.subMatrix(i, j, data);

                        double minor;
                        if (sub.rows == 1) {
                            minor = sub.getValue(0, 0);
                        } else {
                            minor = ModuleDeterminan.minor(i, j, data);
                        }

                        double tanda;
                        if ((i + j) % 2 == 0) {
                            tanda = 1;
                        } else {
                            tanda = -1;
                        }

                        double kofaktor = tanda * minor;
                        double suku = data.getValue(i, j) * kofaktor;
                        det += suku;

                        String ij = "" + (i + 1) + (j + 1);

                        System.out.println(
                            "a" + ij + " = " + formatNumber(data.getValue(i, j))
                        );
                        System.out.println(
                            "Submatriks M" + ij + " (hapus baris " + (i + 1)
                            + " dan kolom " + (j + 1) + "):"
                        );
                        sub.printMatrix();
                        System.out.println(
                            "M" + ij + " = " + formatNumber(minor)
                        );
                        System.out.println(
                            "C" + ij + " = (-1)^(" + (i + 1) + "+" + (j + 1)
                            + ") * " + formatNumber(minor)
                            + " = " + formatNumber(kofaktor)
                        );
                        System.out.println(
                            "a" + ij + " * C" + ij + " = "
                            + formatNumber(data.getValue(i, j)) + " * "
                            + formatNumber(kofaktor) + " = "
                            + formatNumber(suku)
                        );
                        System.out.println();

                        if (t > 0) {
                            penjumlahan = penjumlahan + " + ";
                        }
                        penjumlahan = penjumlahan + "(" + formatNumber(suku) + ")";
                    }

                    System.out.println(
                        "det(A) = " + penjumlahan + " = " + formatNumber(det)
                    );
                }

                System.out.println();
                System.out.println(
                    "Hasil determinan: det(A) = " + formatNumber(det)
                );

                hasilOutput.append("Hasil determinan: det(A) = ")
                    .append(formatNumber(det)).append("\n");
            }

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
        double[][] temp = new double[10][10];

        while ((line = reader.readLine()) != null) {

            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            if (rows >= 10) {
                reader.close();

                throw new IllegalArgumentException(
                    "Jumlah baris tidak boleh lebih dari 10."
                );
            }

            line = line.replace(",", ".");

            String[] tokens =
                line.split("\\s+");

            if (rows == 0) {
                cols = tokens.length;

                if (cols > 10) {
                    reader.close();

                    throw new IllegalArgumentException(
                        "Jumlah kolom tidak boleh lebih dari 10."
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

        Matrix data =
            new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                data.set(
                    i,
                    j,
                    temp[i][j]
                );
            }
        }

        return data;
    }


    private static String formatNumber(double value) {

        DecimalFormatSymbols symbols =
            new DecimalFormatSymbols(
                Locale.US
            );

        DecimalFormat df =
            new DecimalFormat(
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