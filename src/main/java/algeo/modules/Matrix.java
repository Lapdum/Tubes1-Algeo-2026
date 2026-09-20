package algeo.modules;

import java.util.Scanner;

public class Matrix {
    private int rows;
    private int cols;
    private double[][] data; // untuk menyimpan input dr user

    private static Scanner sc = new Scanner(System.in);

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
                    m[r][i] = Double.parseDouble(token[i].trim());
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
}
