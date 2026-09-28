package algeo.modules;

public class Inverse {
    public static Matrix inverseGaussJordan(Matrix matrix){
        if(matrix.getRows() != matrix.getCols()){
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

        for(int i= 0; i<n; i++){
            Matrix.partialPivoting(augmented,i,i);
            //pivot
            double pivot = augmented.getValue(i, i);
            if(pivot == 0){
                throw new IllegalArgumentException("Matriks tidak memiliki balikan");
            }
            Matrix.multiplyRow(augmented, i, 1.0/pivot); //sebaris dibagi 1/pivot
            for (int r = 0; r<n; r++){//elim dibawah pivot
                if(r != i){
                    double factor = augmented.getValue(r, i);
                    Matrix.addRowbyRow(augmented, r, i, -factor);
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


    
}
