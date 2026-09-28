package algeo.modules;

import java.util.Scanner;

public class Matrix {
    int rows;
    int cols;
    double[][] data; // untuk menyimpan input dr user

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

    public static Matrix partialPivoting (Matrix m, int pivotRow, int col){ 
        int position = pivotRow;// nomor baris elemen pivot terbesar
        double max = m.getValue(pivotRow, col);


        for(int i=pivotRow+1; i<m.getRows(); i++){ //cari nilai absolut terbesar 1 kolom
            double value = m.getValue(i, col);
            
            //absolut
            double absValue;
            if (value < 0) {
                absValue = -value;
            } else {
                absValue = value;
            }

            double absMax;
            if (max < 0) {
                absMax = -max;
            } else {
                absMax = max;
            }

            if (absValue > absMax) {
                max = value;
                position = i;}
        }
        //proses tukar pivot yg punya nilai terbesar, gerak diagonal ke bawah
        if(position != pivotRow){
            for(int j = 0; j<m.getCols(); j++){
                double temp = m.getValue(pivotRow, j);

                m.set(pivotRow, j, m.getValue(position, j));
                m.set(position, j, temp);

            }
        }
        return m;

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
            double m2 = m.getValue(r2, i);
            double res = m1 + multiplier*m2;
            m.set(r1, i, res);
        }

        return m;
    }

    public static Matrix subtractRowbyRow(Matrix m, int r1, int r2, double multiplier) { // r1 target
        for (int i = 0; i < m.getCols(); i++) {
            double m1 = m.getValue(r1, i);
            double m2 = m.getValue(r2, i);
            double res = m1 - m2;
            m.set(r1, i, res);
        }

        return m;
    }
}
