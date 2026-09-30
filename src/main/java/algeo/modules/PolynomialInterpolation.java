package algeo.modules;

public class PolynomialInterpolation {
    public static double interpolatePoly(Matrix data, double x){
        if (hasDuplicateX(data)) {
        throw new IllegalArgumentException("Nilai x tidak boleh sama.");}
        
        int n = data.getRows(); //ambil jml titik
        double hasil = 0; 
        for(int i=0; i<n;i++){
            double li = 1; //nyimpen li(x)
            for(int j=0;j<n;j++){
                if(i !=j){
                    double xi = data.getValue(i, 0);
                    double xj = data.getValue(j, 0);
                    li = li*(x - xj)/(xi - xj);
                }
            }
            double yi = data.getValue(i, 1);
            hasil = hasil + yi*li;
        }
        return hasil;
    }



    public static void printSteps(Matrix data, double x) {
        int n = data.getRows();

        System.out.println("x yang dicari = " + x);
        System.out.println();

        for (int i = 0; i < n; i++) {

            System.out.print("L" + i + "(x) = ");
            boolean first = true;

            for (int j = 0; j < n; j++) {

                if (i != j) {
                    double xi = data.getValue(i, 0);
                    double xj = data.getValue(j, 0);

                    if (!first) {
                        System.out.print(" * ");
                    }
                System.out.print(
                    "(x - " + xj + ") / (" + xi + " - " + xj + ")");
                    first = false;
                }
            }

                System.out.println();
        }

        System.out.println();
        System.out.print("P(x) = ");

        for (int i = 0; i < n; i++) {

            double yi = data.getValue(i, 1);
            if (i > 0) {
                System.out.print(" + ");
            }

            System.out.print(yi + "L" + i + "(x)");
        }
            System.out.println();
            double hasil = interpolatePoly(data, x);
            System.out.println("P(" + x + ") = " + hasil);
    }
    



    private static double[] multiplyPolynomial(double[] a, double[] b) {

        double[] hasil = new double[a.length + b.length - 1];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                hasil[i + j] = hasil[i + j] + a[i] * b[j];
            }
        }

        return hasil;
    }



    public static double[] getPolynomialCoefficients(Matrix data) {

        int n = data.getRows();

        double[] coefficients = new double[n];

        for (int i = 0; i < n; i++) {

            double xi = data.getValue(i, 0);
            double yi = data.getValue(i, 1);

            double[] basis = {1};
            double denominator = 1;

            for (int j = 0; j < n; j++) {

                if (i != j) {

                    double xj = data.getValue(j, 0);

                    double[] factor = {-xj, 1};

                    basis = multiplyPolynomial(basis, factor);

                    denominator = denominator * (xi - xj);
                }
            }

            double multiplier = yi / denominator;

            for (int k = 0; k < basis.length; k++) {
                coefficients[k] =
                    coefficients[k] + multiplier * basis[k];
            }
        }

        return coefficients;
    }



    public static void printPolynomial(Matrix data) {

        double[] coefficients = getPolynomialCoefficients(data);

        System.out.print("P(x) = ");

        for (int i = coefficients.length - 1; i >= 0; i--) {

            double value = coefficients[i];

            if (value == 0) {
                continue;
            }

            if (i != coefficients.length - 1 && value > 0) {
                System.out.print(" + ");
            } else if (value < 0) {
                System.out.print(" - ");
                value = -value;
            }

            if (i == 0) {
                System.out.print(value);
            } else if (i == 1) {
                System.out.print(value + "x");
            } else {
                System.out.print(value + "x^" + i);
            }
        }

        System.out.println();
    }


    private static boolean hasDuplicateX(Matrix data) {

        int n = data.getRows();

        for (int i = 0; i < n; i++) {

            double xi = data.getValue(i, 0);

            for (int j = i + 1; j < n; j++) {

                double xj = data.getValue(j, 0);

                if (xi == xj) {
                    return true;
                }
            }
        }

        return false;
    }
}
