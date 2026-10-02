package algeo.modules;

public class PolynomialInterpolation {
    public static double interpolatePoly(Matrix data, double x){
       double[] coefficients = getPolynomialCoefficients(data);
       double hasil = 0;
       double power = 1;
       for(int i =0; i< coefficients.length; i++){
        hasil += coefficients[i]*power;
        power *= x;
       }
       return hasil;

    }


    public static double printSteps(Matrix data, double x) {
        int n = data.getRows();
        System.out.println( "metode : Interpolasi Polinomial dengan Eliminasi Gauss");
        System.out.println();
        System.out.println("Derajat polinom = " + (n-1));
        System.out.println();

        System.out.println("Bentuk umum polinom: ");
        System.out.print("P(x) = a0");

        for(int i = 1; i<n; i++){
            System.out.print(" + a"+ i +"x");
            if(i>1){
                System.out.print("^"+i);
            }
        }


        System.out.println();

        System.out.println();

        System.out.println("Membentuk SPL dari titik data:");

        for (int i = 0; i < n; i++) {
            double xi = data.getValue(i, 0);
            double yi = data.getValue(i, 1);

            System.out.println(
                "P(" + xi + ") = " + yi
            );
        }

        Matrix augmented = buildInterpolationMatrix(data);

        System.out.println();

        System.out.println("Matriks augmented:");

        printAugmented(augmented);

        double[] coefficients = SPL.solveForInterpolation(augmented);
        System.out.println();
        System.out.println("Koefisien:");
        for (int i = 0; i < coefficients.length; i++) {
            System.out.println("a" + i + " = " + formatNumber(coefficients[i]));
        }

        System.out.println();
        System.out.println("Persamaan interpolasi:");
        printPolynomial(coefficients);
        System.out.println();
        double hasil =
            evaluate(coefficients, x);


        return hasil;
    }
    



    public static double[] getPolynomialCoefficients(Matrix data) { 
        int n = data.getRows();
        Matrix augmented = buildInterpolationMatrix(data); 
        return SPL.solveForInterpolation(augmented);


    }


    private static Matrix buildInterpolationMatrix(Matrix data) {
        int n = data.getRows();

        Matrix augmented =
            new Matrix(n, n + 1);

        for (int i = 0; i < n; i++) {
            double x = data.getValue(i, 0);
            double y = data.getValue(i, 1);
            double power = 1;

            for (int j = 0; j < n; j++) {
                augmented.set(i, j, power);
                power *= x;
            }
            augmented.set(i, n, y);
        }
        return augmented;
    }



    public static void printPolynomial(double[] coefficients) {

        System.out.print("P(x) = ");
        boolean first = true;

        for (int i = coefficients.length - 1; i >= 0; i--) {

            double value = coefficients[i];
            if (value == 0) {
                continue;}

            if (!first) {
                if (value >= 0) {
                    System.out.print(" + ");
                } else {
                    System.out.print(" - ");
                    value = -value;
                }

            } else if (value < 0) {

                System.out.print("-");
                value = -value;
            }

            System.out.print(formatNumber(value));
            if (i >= 1) {
                System.out.print("x");}

            if (i >= 2) {
                System.out.print("^" + i);
            }

            first = false;
        }

        if (first) {
            System.out.print("0");
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



    private static double evaluate(
            double[] coefficients,
            double x) {

        double hasil = 0;
        double power = 1;

        for (int i = 0;
            i < coefficients.length;
            i++) {

            hasil += coefficients[i] * power;

            power *= x;
        }

        return hasil;
    }


    private static void printAugmented(Matrix matrix) {

        int n = matrix.getRows();

        for (int i = 0; i < matrix.getRows(); i++) {

            for (int j = 0; j < matrix.getCols(); j++) {

                if (j == n) {
                    System.out.print("| ");
                }

                System.out.print(
                    formatNumber(matrix.getValue(i, j))
                );

                if (j < matrix.getCols() - 1) {
                    System.out.print(" ");
                }
            }

            System.out.println();
        }
    }


    private static String formatNumber(double value) {

        java.text.DecimalFormatSymbols symbols =
            new java.text.DecimalFormatSymbols(
                java.util.Locale.US
            );

        java.text.DecimalFormat df =
            new java.text.DecimalFormat(
                "0.###",
                symbols
            );

        if (value == 0) {
            value = 0;
        }
        return df.format(value);
    }

}
