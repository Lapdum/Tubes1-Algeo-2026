package algeo;

import algeo.modules.Matrix;

public class Testing {
    public static void main(String[] args) {
        Matrix m = Matrix.inputMatrix();

        for (int i = 0; i < m.getRows(); i++) {
            for (int j = 0; j < m.getCols(); j++) {
                System.out.printf(m.getValue(i, j) + " ");
            }
            System.out.println();
        }
    }
}
