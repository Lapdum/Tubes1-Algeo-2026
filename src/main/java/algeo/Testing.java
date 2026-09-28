package algeo;

import algeo.modules.Matrix;
import algeo.modules.Inverse;

public class Testing {
    public static void main(String[] args) {
        Matrix m = Matrix.inputMatrix();
        m.printMatrix();
        Matrix inverse = Inverse.inverseAdjoint(m);

        for (int i = 0; i < inverse.getRows(); i++) {
            for (int j = 0; j < inverse.getCols(); j++) {
                System.out.printf(inverse.getValue(i, j) + " ");
            }
            System.out.println();
        }
    }
}
