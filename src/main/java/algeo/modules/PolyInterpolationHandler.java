package algeo.modules;

import java.util.Scanner;

public class PolyInterpolationHandler {
    public static void run(Scanner sc) {

        System.out.println("=== Interpolasi Polinomial ===");

        System.out.print("Masukkan jumlah titik: ");

        int n;

        try {
            n = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Jumlah titik harus berupa angka.");
            return;
        }

        if (n < 2 || n > 10) {
            System.out.println("Jumlah titik harus antara 2 dan 10.");
            return;
        }

        Matrix data = new Matrix(n, 2);

        try {

            for (int i = 0; i < n; i++) {

                System.out.println("Titik ke-" + (i + 1));

                System.out.print("x = ");
                double x = Double.parseDouble(sc.nextLine());

                System.out.print("y = ");
                double y = Double.parseDouble(sc.nextLine());

                data.set(i, 0, x);
                data.set(i, 1, y);
            }

            System.out.print("Masukkan x yang ingin dicari: ");
            double xCari = Double.parseDouble(sc.nextLine());

            double hasil = PolynomialInterpolation.interpolatePoly(data, xCari);

            System.out.println();
            PolynomialInterpolation.printSteps(data, xCari);

            System.out.println();
            PolynomialInterpolation.printPolynomial(data);

            System.out.println();
            System.out.println("Hasil interpolasi = " + hasil);

        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
