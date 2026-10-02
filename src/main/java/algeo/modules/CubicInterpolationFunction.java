package algeo.modules;

import algeo.modules.SPL;

public class CubicInterpolationFunction {
    Matrix a;
    Matrix b;
    Matrix c; 
    Matrix d;

    Matrix h;

    Matrix rKanan;

    Matrix koefc;

    Matrix SPLc;

    public void fillSegmen(Matrix matrix) {
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
        SPLc = eliminasiGaussJordan(SPLc);

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

    

    
}