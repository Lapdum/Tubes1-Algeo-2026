package algeo.modules;

public class CubicInterpolationFunction {
    Matrix a;
    Matrix b;
    Matrix c; 
    Matrix d;

    Matrix h;

    Matrix rKanan;

    Matrix koefc;

    Matrix SPLc;

    Matrix segmenFunc;

    public static Matrix fillSegmen(Matrix matrix) {
        a.cols = matrix.rows - 1; a.rows = 1;
        b.cols = matrix.rows - 1; b.rows = 1;
        c.cols = matrix.rows - 1; c.rows = 1;
        d.cols = matrix.rows - 1; d.rows = 1;

        h.cols = matrix.rows - 1; h.rows = 1;

        rKanan.cols = 1; rKanan.rows = matrix.rows;

        koefc.cols = matrix.rows; koefc.rows = matrix.rows - 2;

        for (int p = 0; p < a.rows; p++) {
            a.data[0][p] = matrix.data[1][p];
        }

        for (int p = 0; p < h.rows; p++) {
            h.data[0][p] = -matrix.data[p][0] + matrix.data[p+1][0]; 
        }

        for (int p = 0; p < rKanan.rows; p++) {
            rKanan.data[0][p] = ( (3/h.data[(p+1)][0]) * (a.data[(p+1)+1][0] - a.data[(p+1)][0]) ) - ( (3/h.data[(p+1)-1][0]) * (a.data[(p+1)][0] - a.data[(p+1)-1][0]) );
        }

        for (int p = 0; p < koefc.rows; p++) {
            if (p == 0) {
                koefc.data[p][p + 0] = 0;
                koefc.data[p][p + 1] = 2 * ( h.data[(p+1)-1][0] + h.data[(p+1)][0] );
                koefc.data[p][p + 2] = h.data[(p+1)][0];
            }
            else if (p == koefc.rows - 1) {
                koefc.data[p][p + 0] = h.data[(p+1)-1][0];
                koefc.data[p][p + 1] = 2 * ( h.data[(p+1)-1][0] + h.data[(p+1)][0] );
                koefc.data[p][p + 2] = 0;
            }
            else {
                koefc.data[p][p + 0] = h.data[(p+1)-1][0];
                koefc.data[p][p + 1] = 2 * ( h.data[(p+1)-1][0] + h.data[(p+1)][0] );
                koefc.data[p][p + 2] = h.data[(p+1)][0];
            }
        }

        SPLc = combine2M(koefc, rKanan);
        SPLc = SPL.eliminasiGaussJordan(SPLc);

        for (int p = 0; p < koefc.cols; p++) {
            koefc.data[0][p] = SPLc.data[SPLc.cols - 1][p];
        }

        for (int p = 0; p < b.cols; p++) {
            b.data[0][p] = ((a.data[0][p+1] - a.data[0][p]) / h.data[0][p]) - ((h.data[0][p]*((2*c.data[0][p]) + c.data[0][p+1])) / 3);
        }

        for (int p = 0; p < d.cols; p++) {
            d.data[0][p] = ((c.data[0][p+1] - c.data[0][p]) / 3*h.data[0][p]);
        }

        segmenFunc.cols = 4; segmenFunc.rows = matrix.rows - 1;

        for (int p = 0; p < segmenFunc.rows; p++) {
            segmenFunc.data[p][0] = a.data[0][p] - (b.data[0][p]*matrix.data[p][0]) + (c.data[0][p]*Math.pow((matrix.data[p][0]),2)) - (d.data[0][p]*Math.pow(matrix.data[0][p], 3));
            segmenFunc.data[p][1] = b.data[0][p] - ( matrix.data[p][0] * ( (2*c.data[0][p]) + (2*d.data[0][p]*matrix.data[p][0]) + (d.data[0][p]*matrix.data[p][0])) );
            segmenFunc.data[p][2] = c.data[0][p] - (3*d.data[0][p]*matrix.data[p][0]);
            segmenFunc.data[p][3] = d.data[0][p];
        }

        return segmenFunc;
    }

    public Matrix combine2M(Matrix m1, Matrix m2) {
        Matrix m3 = new Matrix(m1.rows + m2.rows, m1.cols + m2.cols);
        for (int i = 0; i < m1.rows; i++) {
            for (int j = 0; j < m1.cols; j++) {
                m3.data[i][j] = m1.data[i][j];
            }
        }

        for (int i = m1.rows - 1; i < m2.rows; i++) {
            for (int j = m1.cols - 1; j < m2.cols; j++) {
                m3.data[i][j] = m2.data[i - m1.rows][j - m2.cols];
            }
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