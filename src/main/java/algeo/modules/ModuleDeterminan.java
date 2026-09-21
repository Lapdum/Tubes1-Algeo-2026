package algeo.modules;

import java.util.Arrays;

import algeo.modules.Matrix;

public class ModuleDeterminan {
    Matrix mtrx = new Matrix();

    public void jalanAres() {
        //System.out.println(Arrays.deepToString(testMatrix));
        System.out.println(Arrays.deepToString(subMatrix(i, j, m, n, testMatrix)));
    }

    int[][] testMatrix = { {6, 7, 25, 2}, {2, 9, 11, 1}, {1, 9, 2, 4}, {0, 17, 25, 2} };
    int m = 4;
    int n = 4;

    public int ekspansiKofaktorBaris(int x, int n, int[][] testMatrix) {
        int det = 0;
        x = x -1;
        j = 0;
        while (j < n) {
            det += testMatrix[x][j]*cofaktor(x, j, m, n, testMatrix);
            j++;
        }
        return det;
    }

    static int cofaktor(int i, int j, int m, int n, int[][] testMatrix) {
        return (-1)^(i+j)*minor(i, j, m, n, testMatrix);
    }

    static int minor(int i, int j, int m, int n, int[][] testMatrix) { 
        subMatrix(i, j, m, n, testMatrix);
        if (m > 2) {
            return ekspansiKofaktorBaris();
        } else {
            return localDeterminan2x2();
        }
    }

    int i = 0;
    int j = 0;

    static int[][] subMatrix(int i, int j, int m, int n, int[][] testMatrix) {
        int[][] subM = new int[m-1][n-1];
        while (i < m-1) {
            if (j < n-1) {
                subM[i][j] += testMatrix[i+1][j+1];
                j++;
            } else {
                i++;
                j = 0;
            }
        } 
        return subM;
    }

    static int localDeterminan2x2(int[][] testMatrix) {
        return testMatrix[0][0]*testMatrix[1][1] - testMatrix[0][1]*testMatrix[1][0];
    }

    public boolean isSingular(int[][] Matrix) {
        if (ekspansiKofaktorBaris() == 0) {return true; }
        else { return false; }
    }

    public double reduksiBaris(int[][] Matrix, int countSwitch){
        int valueDiagonal = 
        
        return (-1)^countSwitch*valueDiagonal;
    }
}
    
