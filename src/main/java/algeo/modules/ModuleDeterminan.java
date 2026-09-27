package algeo.modules;

import java.util.Arrays;
import algeo.modules.Matrix;

public class ModuleDeterminan {

    public static void jalanAres() {
        Matrix test = new Matrix(4, 4);
        test.data = new double[][] {{1, 2, 3, 10}, {4, 5, 6, 11}, {7, 8, 9, 12}, {13, 14, 15, 16}};
        Matrix result = subMatrix(1, 1, test);
        double det = ekspansiKofaktorBaris(1, test);
        System.out.println(det);
        //System.out.println(Arrays.deepToString(result.data));
        }

    public static Double ekspansiKofaktorBaris(int x, Matrix matrix) { //baris ke-n
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

    public static double cofaktor(int i, int j, Matrix matrix) { //cofaktor (Cij)
        return Math.pow(-1, i+j+2)*minor(i, j, matrix);
    }

    public static double minor(int i, int j, Matrix matrix) { //minor (Mij)
        Matrix subM = subMatrix(i, j, matrix); 
        if (subM.cols > 2) {
            return ekspansiKofaktorBaris(1, subM);
        } else {
            if (i == 0 && j == 0) { return subM.data[1][1]; }
            else if (i == 0 && j == 1) { return subM.data[1][0]; }
            else if ( i == 1 && j == 0) { return subM.data[0][1]; }
            else { return subM.data[0][0]; }       
        }
    }

    public static Matrix subMatrix(int i, int j, Matrix matrix) {
        Matrix subM = new Matrix(matrix.rows - 1, matrix.cols - 1);
        int a = 0;
        int b = 0; 

        int x = 0;
        int y = 0;
        while (a < matrix.rows) {
            if (a != i) {
                y = 0;
                b = 0;
                while (b < matrix.cols) {
                    if (b != j) {
                        subM.data[x][y] = matrix.data[a][b];
                        y++;
                    }
                    b++;
                }
                x++;
            }
            a++;
        }
        return subM;
    }

    // static double localDeterminan2x2(Matrix matrix) {
        // return (matrix.data[0][0]*matrix.data[1][1]) - (matrix.data[0][1]*matrix.data[1][0]);
    // }

    public static boolean isSingular(Matrix matrix) {
        if (ekspansiKofaktorBaris(1, matrix) == 0) {return true; }
        else { return false; }
    }

    public static double reduksiBaris(Matrix matrix, int countSwitch){
        double valueDiagonal = 0;
        for(int i = 0; i < matrix.rows; i++){
            valueDiagonal *= matrix.data[i][i];
        }
        return Math.pow(-1, countSwitch)*valueDiagonal;
    }
}
