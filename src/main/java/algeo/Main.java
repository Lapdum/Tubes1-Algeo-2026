package algeo;

import java.util.Scanner;

import algeo.modules.CubicInterpolationHandler;
import algeo.modules.InverseHandler;
import algeo.modules.ModuleDeterminanHandler;
import algeo.modules.PolyInterpolationHandler;
import algeo.modules.RegresiHandler;
import algeo.modules.SPLHandler;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("=================================");
            System.out.println("        ALJABAR LINEAR");
            System.out.println("=================================");
            System.out.println("1. Sistem Persamaan Linier");
            System.out.println("2. Determinan");
            System.out.println("3. Matriks Balikan");
            System.out.println("4. Interpolasi Polinomial");
            System.out.println("5. Interpolasi Spline");
            System.out.println("6. Regresi Linier");
            System.out.println("0. Keluar");
            System.out.println("=================================");
            System.out.print("Pilih menu: ");
            String input = sc.nextLine();
            int pilihan;

            try {
                pilihan = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka.");
                continue;
            }

            switch (pilihan) {

                case 1:
                    SPLHandler.run(sc);
                    break;
                case 2:
                    ModuleDeterminanHandler.run(sc);
                    break;
                case 3:
                    InverseHandler.run(sc);
                    break;
                case 4:
                    PolyInterpolationHandler.run(sc);
                    break;
                case 5:
                    CubicInterpolationHandler.run(sc);
                    break;
                case 6:
                    RegresiHandler.run(sc);
                    break;
                case 0:
                    running = false;
                    System.out.println("Program selesai.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid.");
            }
        }
        sc.close();
    }
}

