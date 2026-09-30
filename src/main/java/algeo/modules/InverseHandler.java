package algeo.modules;

import java.util.Scanner;

public class InverseHandler {
    public static void run(Scanner sc) {

        System.out.println("=== Matriks Balikan ===");
        System.out.println("1. Gauss-Jordan");
        System.out.println("2. Adjoint");
        System.out.print("Pilih metode: ");

        int pilihan;

        try {
            pilihan = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka.");
            return;
        }

        if (pilihan != 1 && pilihan != 2) {
            System.out.println("Pilihan tidak valid.");
            return;
        }

        System.out.println("Masukkan matriks:");
        System.out.println("Gunakan spasi untuk memisahkan elemen.");
        System.out.println("Tekan Enter kosong untuk selesai.");

        Matrix matrix;

        try {
            matrix = Matrix.inputMatrix();
        } catch (NumberFormatException e) {
            System.out.println("Input matriks harus berupa angka.");
            return;
        }

        try {

            if (pilihan == 1) {
                Matrix inverse = Inverse.inverseGaussJordan(matrix);

                System.out.println("Matriks Inverse:");
                inverse.printMatrix();

            } else {
                Matrix inverse = Inverse.inverseAdjoint(matrix);

                System.out.println("Matriks Inverse:");
                inverse.printMatrix();
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
