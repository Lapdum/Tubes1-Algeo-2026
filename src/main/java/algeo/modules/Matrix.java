package algeo.modules;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
        double[][] m = new double[12][13];
        int r = 0;
        int c = 0;
        boolean inputArray = true;
        while (inputArray) {
            String row = sc.nextLine();
            if (row.length() == 0 || r == 11) {
                inputArray = false;
            } else {
                row = row.replace(",", ".");

                String[] token = row.split(" ");
                for (int i = 0; i < token.length; i++) {
                    if (token[i].contains("/")) {
                        String[] numbers = token[i].split("/");
                        double numerator = Double.parseDouble(numbers[0]);
                        double denominator = Double.parseDouble(numbers[1]);
                        double decimal = numerator / denominator;
                        m[r][i] = decimal;
                    } else {
                        m[r][i] = Double.parseDouble(token[i]);
                    }
                }
                r += 1;
                if (c == 0) {
                    c = token.length;
                } else if (token.length != c) {
                    System.out.println("Belum aku bikin try catchnya");
                }
            }
        }

        Matrix returnM = new Matrix(r, c);

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                returnM.data[i][j] = m[i][j];
            }
        }

        return returnM;
    }

    public static Matrix inputFileMatrix() {
        System.out.printf("Masukkan nama file (termasuk extension .txt): ");
        String namaFile = sc.nextLine();

        File fileMatrix = new File("../../../test/" + namaFile);
        boolean validName = true;
        while (validName) {
            try (Scanner readFile = new Scanner(fileMatrix)) {
                System.out.println("File ditemukan!");
                validName = false;
            } catch (FileNotFoundException e) {
                System.out.println("File tidak ditemukan!");
                sc.next();
            }
        }

        double[][] m = new double[1001][1002];
        int r = 0;
        int c = 0;
        try (Scanner readRow = new Scanner(fileMatrix, StandardCharsets.UTF_8.name())) {
            while (readRow.hasNextLine()) {
                String row = readRow.nextLine();
                if (row.length() == 0 || r == 1001) {
                    System.out.println("Kepanjangan bruh!");
                } else {
                    row = row.replace(",", ".");

                    String[] token = row.split(" ");
                    for (int i = 0; i < token.length; i++) {
                        if (token[i].contains("/")) {
                            String[] numbers = token[i].split("/");
                            double numerator = Double.parseDouble(numbers[0]);
                            double denominator = Double.parseDouble(numbers[1]);
                            double decimal = numerator / denominator;
                            m[r][i] = decimal;
                        } else {
                            m[r][i] = Double.parseDouble(token[i]);
                        }
                    }
                    r += 1;
                    if (c == 0) {
                        c = token.length;
                    } else if (token.length != c) {
                        System.out.println("Belum aku bikin try catchnya");
                    }
                }
            }

            Matrix returnM = new Matrix(r, c);

            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    returnM.data[i][j] = m[i][j];
                }
            }

            return returnM;
        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan!");
            sc.next();
        }

        return null;
    }

    public static Matrix inputTitik() {
        double[][] m = new double[10][2];
        int r = 0;
        int c = 0;
        boolean inputArray = true;
        while (inputArray) {
            String row = sc.nextLine();
            if (row.length() == 0 || r == 10) {
                inputArray = false;
            } else {
                row = row.replace(",", ".");

                String[] token = row.split(" ");
                for (int i = 0; i < token.length; i++) {
                    if (token[i].contains("/")) {
                        String[] numbers = token[i].split("/");
                        double numerator = Double.parseDouble(numbers[0]);
                        double denominator = Double.parseDouble(numbers[1]);
                        double decimal = numerator / denominator;
                        m[r][i] = decimal;
                    } else {
                        m[r][i] = Double.parseDouble(token[i]);
                    }
                }
                r += 1;
                if (c == 0) {
                    c = token.length;
                } else if (token.length != c) {
                    System.out.println("Belum aku bikin try catchnya");
                }
            }
        }

        Matrix returnM = new Matrix(r, c);

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                returnM.data[i][j] = m[i][j];
            }
        }

        return returnM;
    }

    public static Matrix inputFileTitik() {
        System.out.printf("Masukkan nama file (termasuk extension .txt): ");
        String namaFile = sc.nextLine();

        File fileMatrix = new File("../../../test/" + namaFile);
        boolean validName = true;
        while (validName) {
            try (Scanner readFile = new Scanner(fileMatrix)) {
                System.out.println("File ditemukan!");
                validName = false;
            } catch (FileNotFoundException e) {
                System.out.println("File tidak ditemukan!");
                sc.next();
            }
        }

        double[][] m = new double[10][2];
        int r = 0;
        int c = 0;
        try (Scanner readRow = new Scanner(fileMatrix, StandardCharsets.UTF_8.name())) {
            while (readRow.hasNextLine()) {
                String row = readRow.nextLine();
                if (row.length() == 0 || r == 1001) {
                    System.out.println("Kepanjangan bruh!");
                } else {
                    row = row.replace(",", ".");

                    String[] token = row.split(" ");
                    for (int i = 0; i < token.length; i++) {
                        if (token[i].contains("/")) {
                            String[] numbers = token[i].split("/");
                            double numerator = Double.parseDouble(numbers[0]);
                            double denominator = Double.parseDouble(numbers[1]);
                            double decimal = numerator / denominator;
                            m[r][i] = decimal;
                        } else {
                            m[r][i] = Double.parseDouble(token[i]);
                        }
                    }
                    r += 1;
                    if (c == 0) {
                        c = token.length;
                    } else if (token.length != c) {
                        System.out.println("Belum aku bikin try catchnya");
                    }
                }
            }

            Matrix returnM = new Matrix(r, c);

            for (int i = 0; i < r; i++) {
                for (int j = 0; j < c; j++) {
                    returnM.data[i][j] = m[i][j];
                }
            }

            return returnM;
        } catch (FileNotFoundException e) {
            System.out.println("File tidak ditemukan!");
            sc.next();
        }

        return null;
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

    public static int partialPivoting(Matrix m, int pivotRow, int col) {

        int position = pivotRow;

        double max = m.getValue(pivotRow, col);

        if (max < 0) {
            max = -max;
        }

        for (int i = pivotRow + 1; i < m.getRows(); i++) {

            double value = m.getValue(i, col);

            double absValue;

            if (value < 0) {
                absValue = -value;
            } else {
                absValue = value;
            }

            if (absValue > max) {
                max = absValue;
                position = i;
            }
        }

        if (position != pivotRow) {

            for (int j = 0; j < m.getCols(); j++) {

                double temp = m.getValue(pivotRow, j);

                m.set(
                    pivotRow,
                    j,
                    m.getValue(position, j)
                );

                m.set(
                    position,
                    j,
                    temp
                );
            }
        }

        return position;
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
    public void printMatrix(){
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.###", symbols);

        for (int i = 0; i<rows; i++) {
            for(int j = 0; j< cols; j++) {
                double value = data[i][j];

                if(value == 0) {
                    value = 0;
                }
                System.out.print(df.format(value));
                if(j< cols-1) {
                    System.out.print(" ");
                }
            }
        }
    }
    
    
    public static double round3(double x) {
        if (x < 0) {
            return (int) (x * 1000 - 0.5) / 1000.0;
        }
        return (int) (x * 1000 + 0.5) / 1000.0;
    }

    // Print output

    public static void printMatrix(Matrix m) {
        int r = m.getRows();
        int c = m.getCols();

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                System.out.printf(round3(m.getValue(i, j)) + " ");
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
