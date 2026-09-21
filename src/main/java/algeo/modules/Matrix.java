package algeo.modules;

import java.util.Scanner;

public class Matrix {
    private int rows;
    private int cols;
    private double[][] data; // untuk menyimpan input dr user

    private static Scanner sc = new Scanner(System.in);

    // Fungsi Dasar Matriks
    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols; // this itu artiny objek yg lagi dipake/dimiliki oleh konstruktor (public
                          // matrix)
        this.data = new double[rows][cols];

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

    public Matrix partialPivoting(Matrix m, int c) {
        double mx = 0;
        int pos = 0;
        for (int i = 0; i < m.getRows(); i++) {
            if (m.getValue(i, c) > mx) {
                mx = m.getValue(i, c);
                pos = i;
            }
        }

        if (pos > 0) {
            for (int i = 0; i < m.getCols(); i++) {
                double a = m.getValue(0, i);
                double b = m.getValue(pos, i);
                m.set(pos, i, a);
                m.set(0, i, b);
            }
        }

        return m;
    }

    public Matrix multiplyRow(Matrix m, int r, double multiplier) {
        for (int i = 0; i < m.getCols(); i++) {
            double replace = multiplier * m.getValue(r, i);
            m.set(r, i, replace);
        }

        return m;
    }

    public Matrix addRowbyRow(Matrix m, int r1, int r2) { // r1 target
        for (int i = 0; i < m.getCols(); i++) {
            double m1 = m.getValue(r1, i);
            double m2 = m.getValue(r2, i);
            double res = m1 + m2;
            m.set(r1, i, res);
        }

        return m;
    }

    public Matrix subtractRowbyRow(Matrix m, int r1, int r2) { // r1 target
        for (int i = 0; i < m.getCols(); i++) {
            double m1 = m.getValue(r1, i);
            double m2 = m.getValue(r2, i);
            double res = m1 - m2;
            m.set(r1, i, res);
        }

        return m;
    }
}
