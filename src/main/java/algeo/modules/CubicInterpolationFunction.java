package algeo.modules;

import java.util.Arrays;

public class CubicInterpolationFunction {
    public void main(String[] args) {
        System.out.println("hai asd");
        Matrix test = new Matrix(7, 2);
        test.data = new double[][] {{0, 1}, {1, 1}, {3, 9}, {4, 9}, {6, 15}, {7, 30}, {9, 64}};
        Matrix segmen = fillSegmen(test);
        System.out.println("bwwwwaaao");
        System.out.println(Arrays.deepToString(segmen.data));
        System.out.println("baaao");
    }


    public boolean isXandY(Matrix m) {
        return m.cols <= 2;
    }

    public boolean isXcorrect(Matrix m) {
        for (int p = 1; p < m.rows; p++) {
            if (m.data[p][0] > m.data[p-1][0]) {return false;}
        }
        return true;
    }

    public Matrix fillSegmen(Matrix matrix) {

        Matrix a = new Matrix(matrix.rows, 1); //7
        Matrix b = new Matrix(matrix.rows - 1, 1); //6
        Matrix c = new Matrix(matrix.rows, 1); //7
        Matrix d = new Matrix(matrix.rows - 1, 1); //6

        Matrix h = new Matrix(matrix.rows - 1, 1); //6

        Matrix rKanan = new Matrix(matrix.rows, 1); //7

        Matrix koefc = new Matrix(matrix.rows, matrix.rows); //7x7


        Matrix SPLc = new Matrix(matrix.rows, matrix.rows); //7x8

        Matrix segmenFunc = new Matrix(matrix.rows - 1, 4); //6x4
        
        //a.cols = matrix.rows - 1; a.rows = 1;
        //b.cols = matrix.rows - 1; b.rows = 1;
        //c.cols = matrix.rows - 1; c.rows = 1;
        //d.cols = matrix.rows - 1; d.rows = 1;

        //h.cols = matrix.rows - 1; h.rows = 1;

        //rKanan.cols = 1; rKanan.rows = matrix.rows;

        //koefc.cols = matrix.rows; koefc.rows = matrix.rows - 2;

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
            segmenFunc.data[p][0] = a.data[p][0] - (b.data[p][0]*matrix.data[p][0]) + (c.data[p][0]*Math.pow((matrix.data[p][0]),2)) - (d.data[p][0]*Math.pow(matrix.data[p][0], 3));
            segmenFunc.data[p][1] = b.data[p][0] - ( matrix.data[p][0] * ( (2*c.data[p][0]) - (2*d.data[p][0]*matrix.data[p][0]) - (d.data[p][0]*matrix.data[p][0])) );
            segmenFunc.data[p][2] = c.data[p][0] - (3*d.data[p][0]*matrix.data[p][0]);
            segmenFunc.data[p][3] = d.data[p][0];
        }

        return segmenFunc;
    }

    public Matrix specialCombine(Matrix m1, Matrix m2) {
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

    public Matrix derivativeTwo(Matrix S) {
        Matrix ddS = new Matrix(S.rows, S.cols - 2);
        for (int p = 0; p < S.rows; p++) {
            ddS.data[p][0] = S.data[p][2]*2;
            ddS.data[p][1] = S.data[p][3]*6;
        }
        return ddS;
    }
    
}