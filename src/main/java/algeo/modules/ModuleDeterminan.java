package algeo.modules;

import java.util.Arrays;

public class ModuleDeterminan {
    public Double ekspansiKofaktorBaris(int x, Matrix matrix) { //baris ke-n
        if (matrix.rows != matrix.cols) { return null; }
        else {
            x = x-1;
            double det = 0;
            for (int c = 0; c < matrix.cols; c++) {
                det += matrix.data[x][c]*cofaktor(x, c, matrix);
            }
            return det;
        }
    }

    public double cofaktor(int i, int j, Matrix matrix) { //cofaktor (Cij)
        return Math.pow(-1, i+j+2)*minor(i, j, matrix);
    }

    public double minor(int i, int j, Matrix matrix) { //minor (Mij)
        subMatrix(i, j, matrix); 
        if (matrix.cols > 2) {
            return ekspansiKofaktorBaris(1, matrix);
        } else {
            if (i == 0 && j == 0) { return matrix.data[1][1]; }
            else if (i == 0 && j == 1) { return matrix.data[1][0]; }
            else if ( i == 1 && j == 0) { return matrix.data[0][1]; }
            else ( i == 1 && j == 1) { return matrix.data[0][0]; }       
        }
    }

    static Matrix subMatrix(int i, int j, Matrix matrix) {
        Matrix subM = new Matrix(matrix.rows, matrix.cols);
        while (i < matrix.rows-1) {
            if (j < matrix.cols-1) {
                subM.data[i][j] += matrix.data[i+1][j+1];
                j++;
            } else {
                i++;
                j = 0;
            }
        } 
        return subM;
    }

    // static double localDeterminan2x2(Matrix matrix) {
        // return (matrix.data[0][0]*matrix.data[1][1]) - (matrix.data[0][1]*matrix.data[1][0]);
    // }

    public boolean isSingular(Matrix matrix) {
        if (ekspansiKofaktorBaris(1, matrix) == 0) {return true; }
        else { return false; }
    }

    public double reduksiBaris(Matrix matrix, int countSwitch){
        double valueDiagonal = 0;
        for(int i = 0; i < matrix.rows; i++){
            valueDiagonal *= matrix.data[i][i];
        }
        return Math.pow(-1, countSwitch)*valueDiagonal;
    }
}
