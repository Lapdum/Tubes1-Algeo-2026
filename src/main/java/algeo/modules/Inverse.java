package algeo.modules;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Inverse {
    private static void printAugmentedMatrix(Matrix matrix, int n) {
        DecimalFormatSymbols symbols =
            new DecimalFormatSymbols(Locale.US);
        DecimalFormat df =
            new DecimalFormat("0.###", symbols);

        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                if (j == n) {
                    System.out.print("| ");
                }
                double value = matrix.getValue(i, j);
                if (value == 0) {
                    value = 0; }

             System.out.printf("%8s ", df.format(value));
            }

            System.out.println();
        }
    }



    private static String formatNumber(double value) {

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.###", symbols);

        if (value == 0) {
            value = 0;}
        return df.format(value);
    }
    public static Matrix inverseGaussJordan(Matrix matrix){
        if(matrix.getRows() != matrix.getCols()){
            System.out.println();
            throw new IllegalArgumentException("Matriks harus persegi");
        }
        int n = matrix.getRows();
        Matrix augmented = new Matrix(n, 2*n); //jml kolom matrix semula ditambah kolom matrix identitas
       
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                augmented.set(i, j, matrix.getValue(i, j));//matrix augmented jadi sebelah kiri

                if(i==j){ // r == c
                    augmented.set(i, j+n, 1); //matriks identitas di sebelah kanan
                } else{
                    augmented.set(i, j+n, 0);
                }
            }
        }
        System.out.println();
        System.out.println("Matrix augmented [ A | I ]:");
        printAugmentedMatrix(augmented, n);

        for(int i= 0; i<n; i++){
            int position = findPivotPosition(augmented,i,i);
            Matrix.partialPivoting(augmented, i, i);
            if(position != i){
                System.out.println();
                System.out.println(
                    "R" + (i + 1) + " <-> R" + (position + 1)
                );

                printAugmentedMatrix(augmented, n);
            }

            
          
            //pivot
            double pivot = augmented.getValue(i, i);
            if(pivot == 0){
                throw new IllegalArgumentException("Matriks tidak memiliki balikan");
            }
            Matrix.multiplyRow(augmented, i, 1.0/pivot); //sebaris dibagi 1/pivot
            System.out.println();
            System.out.println( "R" + (i + 1) + " <- (1/" + formatNumber(pivot) + ")R" + (i + 1));
            printAugmentedMatrix(augmented, n);
            

            for (int r = 0; r<n; r++){  //elim dibawah pivot
                if(r != i){
                    double factor = augmented.getValue(r, i);

                    if(factor != 0){
                        Matrix.addRowbyRow(augmented, r, i, -factor);

                        System.out.println();
                        System.out.println(
                            "R"+(r+1)+" = R"+(r+1)+ " - (" + formatNumber(factor) + ")R" + (i+1));
                            printAugmentedMatrix(augmented, n);
                            
                    }

                }
            }
        }
        Matrix inverse = new Matrix(n, n);
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n;j++){
                inverse.set(i, j, augmented.getValue(i, j+n));
            }
        }
        return inverse;
    }

    public static Matrix inverseAdjoint(Matrix matrix){
        if(matrix.getRows() != matrix.getCols()){
            System.out.println();
            throw new IllegalArgumentException("Matrix harus persegi.");
        }
        int n = matrix.getRows();
        double det = ModuleDeterminan.ekspansiKofaktorBaris(1, matrix);

        System.out.println("Determinan = " + det);

        if(det == 0){
            System.out.println();
            throw new IllegalArgumentException("Matrix tidak memiliki balikan");

        }
        Matrix cofactor = new Matrix(n,n);

        for(int i = 0; i<n; i++){
            for (int j=0;j<n;j++){
                double value = ModuleDeterminan.cofaktor(i,j,matrix);
                cofactor.set(i,j,value);
            }
        }
        System.out.println();
        System.out.println("Matriks Kofaktor:");
        cofactor.printMatrix();

        Matrix adjoint = new Matrix(n,n);
        
         for(int i = 0; i<n; i++){
            for (int j=0;j<n;j++) {
                adjoint.set(i,j, cofactor.getValue(j,i));
            }
        }
        System.out.println();
        System.out.println("Matriks Adjoint:");
        adjoint.printMatrix();

        System.out.println();
        System.out.println("Perhitungan Matriks Inverse:");
        System.out.println("A^-1 = (1 / " + formatNumber(det) + ") * Adjoint");

        System.out.println();
        System.out.println("A^-1 = (1 / " + formatNumber(det) + ") *");

        adjoint.printMatrix();

        System.out.println();
        System.out.println("Pembagian setiap elemen dengan determinan:");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                double value = adjoint.getValue(i, j);

                System.out.print(
                    formatNumber(value)
                    + " / "
                    + formatNumber(det)
                );

                if (j < n - 1) {
                    System.out.print("    ");
                }
            }

            System.out.println();
        }
       

        Matrix inverse = new Matrix(n,n);

        for(int i = 0; i<n; i++){
            for (int j=0;j<n;j++){
                inverse.set(i,j,adjoint.getValue(i,j)/det);
            }
     
        }



        return inverse;
    }

    private static int findPivotPosition(Matrix matrix, int pivotRow, int col) {

        int position = pivotRow;
        double max = matrix.getValue(pivotRow, col);

        for (int i = pivotRow + 1; i < matrix.getRows(); i++) {

            double value = matrix.getValue(i, col);

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
                position = i;
            }
        }

        return position;
    }
    
}
