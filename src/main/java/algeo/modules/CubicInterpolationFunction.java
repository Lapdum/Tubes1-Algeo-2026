package algeo.modules;

public class CubicInterpolationFunction {
    public boolean isXandY(Matrix m) {
        return m.cols <= 2;
    }

    public boolean isXascending(Matrix m) {
        for (int p = 1; p < m.rows; p++) {
            if (m.data[p][0] > m.data[p-1][0]) {return false;}
        }
        return true;
    }

    public boolean isXnotDouble(Matrix m) {
        for (int p = 0; p < m.rows; p++) {
            for (int q = 0; q < m.rows; q++) {
                double cek = m.data[p][0];
                if (cek == m.data[q][0]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static Matrix[] fillSegmen(Matrix matrix) {

        Matrix a = new Matrix(matrix.rows, 1); //7
        Matrix b = new Matrix(matrix.rows - 1, 1); //6
        Matrix c = new Matrix(matrix.rows, 1); //7
        Matrix d = new Matrix(matrix.rows - 1, 1); //6

        Matrix h = new Matrix(matrix.rows - 1, 1); //6

        Matrix rKanan = new Matrix(matrix.rows, 1); //7

        Matrix koefc = new Matrix(matrix.rows, matrix.rows); //7x7


        Matrix SPLc = new Matrix(matrix.rows, matrix.rows); //7x8

        Matrix segmenFunc = new Matrix(matrix.rows - 1, 4); //6x4
   
        for (int p = 0; p < matrix.rows; p++) {
            a.data[p][0] = matrix.data[p][1];
        }

        for (int p = 0; p < h.rows; p++) {
            h.data[p][0] = matrix.data[p+1][0] - matrix.data[p][0] ; 
        }

        for (int p = 0; p < rKanan.rows; p++) {
            if (p == 0) {
                rKanan.data[0][0] = 0;
            }
            else if (p == rKanan.rows - 1) {
                rKanan.data[rKanan.rows - 1][0] = 0;
            }
            else {
                rKanan.data[p][0] = ( (3/h.data[p][0]) * (a.data[p+1][0] - a.data[p][0]) ) - ( (3/h.data[p-1][0]) * (a.data[p][0] - a.data[p-1][0]) );
            }
        }

        for (int p = 0; p < koefc.rows; p++) {
            if (p == 0) {
                koefc.data[0][0] = 1;           
            } 
            else if (p == koefc.rows - 1) {
                koefc.data[p][p] = 1;
            }
            else {
                koefc.data[p][p - 1] = h.data[p-1][0];
                koefc.data[p][p] = 2 * (h.data[p-1][0] + h.data[p][0]);
                koefc.data[p][p + 1] = h.data[p][0];
            }
        }

        SPLc = specialCombine(koefc, rKanan);
        SPLc = SPL.eliminasiGaussJordan(SPLc);

        for (int p = 0; p < c.rows; p++) {
            c.data[p][0] = SPLc.data[p][SPLc.cols - 1];
        }

        for (int p = 0; p < b.rows; p++) {
            b.data[p][0] = ((a.data[p+1][0] - a.data[p][0]) / h.data[p][0]) - ((h.data[p][0]*((2*c.data[p][0]) + c.data[p+1][0])) / 3);
        }

        for (int p = 0; p < d.rows; p++) {
            d.data[p][0] = ((c.data[p+1][0] - c.data[p][0]) / (3*h.data[p][0]));
        }

        // segmenFunc.cols = 4; segmenFunc.rows = matrix.rows - 1;

        for (int p = 0; p < segmenFunc.rows; p++) {
            segmenFunc.data[p][0] = a.data[p][0] - (b.data[p][0]*matrix.data[p][0]) + (c.data[p][0]*Matrix.power((matrix.data[p][0]),2)) - (d.data[p][0]*Matrix.power(matrix.data[p][0], 3));
            segmenFunc.data[p][1] = b.data[p][0] - ( matrix.data[p][0] * ( (2*c.data[p][0]) - (2*d.data[p][0]*matrix.data[p][0]) - (d.data[p][0]*matrix.data[p][0])) );
            segmenFunc.data[p][2] = c.data[p][0] - (3*d.data[p][0]*matrix.data[p][0]);
            segmenFunc.data[p][3] = d.data[p][0];
        }

        return new Matrix[] {segmenFunc, a, b, c, d};
    }

    public static Matrix specialCombine(Matrix m1, Matrix m2) {
        Matrix m3 = new Matrix(m1.rows, m1.cols + 1);
        for (int i = 0; i < m1.rows; i++) {
            for (int j = 0; j < m1.cols; j++) {
                m3.data[i][j] = m1.data[i][j];
            }
        }
        for (int i = 0; i < m2.rows; i++ ) {
            m3.data[i][m3.cols -1] = m2.data[i][0];
        }
        return m3;
    }

    public static Matrix derivativeTwo(Matrix S) { // gajadi dipakai, sayang dihapus
        Matrix ddS = new Matrix(S.rows, S.cols - 2);
        for (int p = 0; p < S.rows; p++) {
            ddS.data[p][0] = S.data[p][2]*2;
            ddS.data[p][1] = S.data[p][3]*6;
        }
        return ddS;
    }

    public static String plusOrmin(double angka) {
        if (angka < 0) {return " - ";}
        else {
            return "+";
        }
    }

    public static void printOutput(Matrix m, Matrix Msegmen, Matrix a, Matrix b, Matrix c, Matrix d) { // udah di cek dan benar
        System.out.print("Metode Interpolasi: Interpolasi Splina Kubik Natural");
        System.out.println();

        System.out.println("Titik-titik sampel:");
        System.out.println();
        m.printMatrix();
        System.out.println();

        System.out.println("Domain interpolasi:");
        System.out.println("[" + m.data[0][0] + ", " + m.data[m.rows - 1][0] + "]");
        System.out.println();

        System.out.println("Persamaan hasil interpolasi segmen");
        System.out.println();

        for (int p = 0; p < Msegmen.rows; p++) {
            String g = "(x" + plusOrmin(0 - m.data[p][0]) + Matrix.absolute(m.data[p][0]) + ")";
            
            System.out.println("Segmen ke-" + p);
            System.out.println("S" + (p) + "(x) = " + a.data[p][0] 
            + plusOrmin(b.data[p][0]) + Matrix.absolute(b.data[p][0]) + g
            + plusOrmin(b.data[p][0]) + Matrix.absolute(b.data[p][0]) + g + "^2"
            + plusOrmin(b.data[p][0]) + Matrix.absolute(b.data[p][0]) + g + "^3");
            System.out.println();
            System.out.println("atau");
            System.out.println();
            System.out.println("S" + (p) + "(x) = " + Msegmen.data[p][0] 
            + plusOrmin(Msegmen.data[p][1]) + Matrix.absolute(Msegmen.data[p][1]) + "x"
            + plusOrmin(Msegmen.data[p][2]) + Matrix.absolute(Msegmen.data[p][2]) + "x^2"
            + plusOrmin(Msegmen.data[p][3]) + Matrix.absolute(Msegmen.data[p][3]) + "x^3");
            System.out.println(", dengan domain [" + m.data[p][0] + ", " + m.data[p+1][0] + "]");
            System.out.println();
        }

        System.out.println("Nilai turunan kedua pada setiap knot");
        System.out.println();
        System.out.println("Pada knot, nilai turunan kedua menjadi:");
        System.out.println();
        System.out.println("S''(xi) = 2ci");
        System.out.println();
        
        
        for (int p = 0; p < m.rows; p++) {
            System.out.println("Knot x" + p + " = " + 2*c.data[p][0]);
            System.out.println();
        }
    }
}