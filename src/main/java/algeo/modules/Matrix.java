package algeo.modules;

import java.util.*;
import java.io.*;
import java.text.*;
import java.nio.charset.StandardCharsets;

public class Matrix {
    public int rows;
    public int cols;
    public double[][] data; // untuk menyimpan input dr user
    public double[][] determinantCramer;
    public boolean[] flag;
    public boolean[] isPivotCol;
    public boolean[][] isPivot;
    public String[] answerSPL;
    public boolean freeVar;

    private static Scanner sc = new Scanner(System.in);
    private static final double EPS = 1e-9;

    // Fungsi Dasar Matriks
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols; // this itu artiny objek yg lagi dipake/dimiliki oleh konstruktor (public
                          // matrix)
        this.freeVar = false;
        this.data = new double[rows][cols];
        this.determinantCramer = new double[cols - 1][1];
        this.flag = new boolean[rows];
        this.isPivotCol = new boolean[cols];
        this.isPivot = new boolean[rows][cols - 1];
        this.answerSPL = new String[cols];

    }

    public static Matrix inputMatrix() {
        return bacaManual(11, 12, 0);
    }

    public static Matrix inputFileMatrix() {
        return bacaFile(1001, 1002, 0);
    }

    public static Matrix inputTitik() {
        return bacaManual(10, 2, 2);
    }

    public static Matrix inputFileTitik() {
        return bacaFile(10, 2, 2);
    }

    private static Matrix bacaManual(int maxRows, int maxCols, int fixedCols) {
        double[][] m = new double[maxRows][maxCols];
        int r = 0;
        int c = fixedCols;

        while (r < maxRows) {
            String row = sc.nextLine();
            if (row.trim().isEmpty()) {
                break;
            }
            try {
                double[] nilai = parseBaris(row);
                if (nilai.length > maxCols) {
                    System.out.println("Jumlah kolom maksimal " + maxCols + ". Ulangi baris ini.");
                    continue;
                }
                if (c != 0 && nilai.length != c) {
                    System.out.println("Jumlah kolom harus " + c + ". Ulangi baris ini.");
                    continue;
                }
                c = nilai.length;
                for (int j = 0; j < c; j++) {
                    m[r][j] = nilai[j];
                }
                r++;
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid: " + e.getMessage() + ". Ulangi baris ini.");
            }
        }

        return doubletoMatrix(m, r, c);
    }

    private static Matrix bacaFile(int maxRows, int maxCols, int fixedCols) {
        File f = mintaFile();
        if (f == null) {
            return null;
        }

        double[][] m = new double[maxRows][maxCols];
        int r = 0;
        int c = fixedCols;
        int noBaris = 0;

        try (Scanner in = new Scanner(f, StandardCharsets.UTF_8.name())) {
            while (in.hasNextLine()) {
                String row = in.nextLine();
                noBaris++;
                if (row.trim().isEmpty()) {
                    continue;
                }
                if (r == maxRows) {
                    System.out.println("Jumlah baris melebihi batas " + maxRows + ".");
                    return null;
                }
                double[] nilai = parseBaris(row);
                if (nilai.length > maxCols || (c != 0 && nilai.length != c)) {
                    System.out.println("Baris " + noBaris + ": jumlah kolom tidak sesuai.");
                    return null;
                }
                c = nilai.length;
                for (int j = 0; j < c; j++) {
                    m[r][j] = nilai[j];
                }
                r++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan!");
            return null;
        } catch (NumberFormatException e) {
            System.out.println("Baris " + noBaris + ": " + e.getMessage() + ".");
            return null;
        }

        return doubletoMatrix(m, r, c);
    }

    private static File mintaFile() {
        while (true) {
            System.out.print("Masukkan nama file (termasuk extension .txt): ");
            String nama = sc.nextLine().trim();
            if (nama.isEmpty()) {
                return null;
            }
            File f = new File("../../../test/" + nama);
            if (f.isFile()) {
                System.out.println("File ditemukan!");
                return f;
            }
            System.out.println("File tidak ditemukan!");
        }
    }

    public static double[] parseBaris(String row) {
        String[] token = row.trim().replace(",", ".").split("\\s+");
        double[] nilai = new double[token.length];

        for (int i = 0; i < token.length; i++) {
            if (token[i].contains("/")) {
                String[] p = token[i].split("/");
                if (p.length != 2) {
                    throw new NumberFormatException("pecahan \"" + token[i] + "\" tidak valid");
                }
                double penyebut = parseAngka(p[1]);
                if (penyebut == 0) {
                    throw new NumberFormatException("penyebut nol pada \"" + token[i] + "\"");
                }
                nilai[i] = parseAngka(p[0]) / penyebut;
            } else {
                nilai[i] = parseAngka(token[i]);
            }
        }
        return nilai;
    }

    public static double parseAngka(String s) {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("\"" + s + "\" bukan angka!");
        }
    }

    public static Matrix doubletoMatrix(double[][] m, int r, int c) {
        Matrix returnM = new Matrix(r, c);

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                returnM.data[i][j] = m[i][j];
            }
        }

        return returnM;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double getValue(int row, int col) {
        return data[row][col];
    }

    public void set(int row, int col, double value) {
        data[row][col] = value;
    }

    // Operasi Baris Elementer

    public static int partialPivoting(Matrix m, int r, int c) {
        double mx = 1e-9;
        int pos = -1;
        for (int i = r; i < m.getRows(); i++) {
            double num = absolute(m.getValue(i, c));
            if (num > mx) {
                mx = num;
                pos = i;
            }
        }

        return pos;
    }

    public static Matrix round3All(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                double num = round3(m.getValue(i, j));
                m.set(i, j, num);
            }
        }

        return m;
    }

    public static Matrix cleanZeros(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();
        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                if (m.getValue(i, j) > -EPS && m.getValue(i, j) < EPS) {
                    m.set(i, j, 0.0);
                }
            }
        }
        return m;
    }

    public static boolean isAllZeroRow(Matrix m, int row) {
        int c = m.getCols();
        for (int j = 0; j < c - 1; j++) {
            if (m.getValue(row, j) != 0) {
                return false;
            }
        }
        return true;
    }

    public static Matrix moveZerosDown(Matrix m) {
        int r = m.getRows();
        int last = r - 1;
        for (int i = 0; i < last; i++) {
            if (isAllZeroRow(m, i)) {
                while (last > i && isAllZeroRow(m, last)) {
                    last--;
                }
                if (last > i) {
                    m = switchRow(m, i, last);
                    last--;
                }
            }
        }
        return m;
    }

    public static Matrix switchRow(Matrix m, int initialPos, int targetPos) {
        if (targetPos >= 0) {
            for (int i = 0; i < m.getCols(); i++) {
                double a = m.getValue(initialPos, i);
                double b = m.getValue(targetPos, i);
                m.set(targetPos, i, a);
                m.set(initialPos, i, b);
            }
        }

        return m;
    }

    public static double absolute(double a) {
        if (a < 0) {
            a *= -1;
        }

        return a;
    }

    public static Matrix multiplyRow(Matrix m, int r, double multiplier) {
        for (int i = 0; i < m.getCols(); i++) {
            double replace = multiplier * m.getValue(r, i);
            m.set(r, i, replace);
        }

        return m;
    }

    public static Matrix addRowbyRow(Matrix m, int r1, int r2, double multiplier) { // r1 target
        for (int i = 0; i < m.getCols(); i++) {
            double m1 = m.getValue(r1, i);
            double m2 = m.getValue(r2, i) * multiplier;
            double res = m1 + m2;
            m.set(r1, i, res);
        }

        return m;
    }

    public static Matrix subtractRowbyRow(Matrix m, int r1, int r2, double multiplier) { // r1 target
        for (int i = 0; i < m.getCols(); i++) {
            double m1 = m.getValue(r1, i);
            double m2 = m.getValue(r2, i) * multiplier;
            double res = m1 - m2;
            m.set(r1, i, res);
        }

        return m;
    }

    public static double round3(double x) {
        if (x < 0) {
            return (int) (x * 1000 - 0.5) / 1000.0;
        }
        return (int) (x * 1000 + 0.5) / 1000.0;
    }

    // Print output

    public static String matrixToString(Matrix m) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                sb.append(round3(m.getValue(i, j))).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static void printMatrix(Matrix m) {
        System.out.println(matrixToString(m));
    }

    public void printMatrix() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.###", symbols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double value = data[i][j];

                if (value == 0) {
                    value = 0;
                }
                System.out.print(df.format(value));
                if (j < cols - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

    }

    public static Matrix transposeMatrix(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();
        Matrix transposeM = new Matrix(c, r);

        for (int i = 0; i < c; i++) {
            for (int j = 0; j < r; j++) {
                transposeM.set(i, j, m.getValue(j, i));
            }
        }

        return transposeM;
    }

    // Operasi matriks

    public static Matrix perkalianMatriks(Matrix m1, Matrix m2) {
        Matrix returnM = new Matrix(m1.getRows(), m2.getCols());
        double num = 0;

        for (int i = 0; i < m1.getRows(); i++) {
            for (int j = 0; j < m2.getCols(); j++) {
                returnM.set(i, j, 0);
                for (int k = 0; k < m1.getCols(); k++) {
                    num += (m1.getValue(i, k) * m2.getValue(k, j));
                }
                returnM.set(i, j, num);
                num = 0;
            }
        }

        return returnM;
    }

    // Operasi bilangan

    public static double power(double a, double n) { // a to the power of n
        if (n == 0) {
            return 1;
        } else {
            return a * power(a, (n - 1));
        }
    }
}
