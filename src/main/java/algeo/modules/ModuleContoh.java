package algeo.modules;

import java.util.Arrays;

public class ModuleContoh {
    public void jalankan() {
        System.out.println("Tugas Besar 1 Aljabar Linier dan Geometri Tahun 2027/2027");
        Matrix test = new Matrix(3, 2);
        test.data = new double[][] {{0, 0}, {1, 1}, {2, 0}};
        Matrix segmen = CubicInterpolationFunction.fillSegmen(test);
        System.out.println(Arrays.deepToString(segmen.data));
    }
}