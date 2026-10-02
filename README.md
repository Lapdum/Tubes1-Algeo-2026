# Aljabar Linier dan Geometri - Tugas Besar 1

Implementasi pustaka komputasi Aljabar Linier dan Geometri menggunakan Java untuk Tugas Besar 1 IF2123 Aljabar Linier dan Geometri Semester I 2026/2027.

Program berbentuk CLI (Command Line Interface) dan menyediakan berbagai metode perhitungan aljabar linier serta komputasi numerik.

## Fitur

Program menyediakan fitur:

1. **Sistem Persamaan Linier (SPL)**
   - Eliminasi Gauss
   - Eliminasi Gauss-Jordan
   - Metode Matriks Balikan
   - Kaidah Cramer

2. **Determinan**
   - Ekspansi Kofaktor Baris
   - Ekspansi Kofaktor Kolom
   - Reduksi Baris

3. **Matriks Balikan**
   - Eliminasi Gauss-Jordan
   - Adjoin

4. **Interpolasi Polinomial**

5. **Natural Cubic Spline**

6. **Regresi Spline Kubik**

Program mendukung input melalui keyboard maupun file `.txt`, serta menyediakan output pada layar dan file `.txt`.

## Requirements

- Java 17 atau lebih baru
- Maven 3.6.3 atau lebih baru

Periksa instalasi dengan perintah berikut:

```bash
java --version
mvn --version

## Struktur direktori

```text
.
├── src
│   └── main
│       └── java
│           └── algeo
│               ├── modules
│               │   ├── CubicInterpolationFunction.java
│               │   ├── CubicInterpolationHandler.java
│               │   ├── CubicRegression.java
│               │   ├── Inverse.java
│               │   ├── InverseHandler.java
│               │   ├── Matrix.java
│               │   ├── ModuleContoh.java
│               │   ├── ModuleDeterminan.java
│               │   ├── ModuleDeterminanHandler.java
│               │   ├── PolyInterpolationHandler.java
│               │   ├── PolynomialInterpolation.java
│               │   ├── RegresiHandler.java
│               │   ├── SPL.java
│               │   └── SPLHandler.java
│               │
│               ├── App.java
│               ├── Main.java
│               └── Testing.java
│
├── test
├── hasil
├── .gitignore
├── pom.xml
└── README.md
```

Kode program diletakkan di dalam `src/main/java/algeo`. Kelas utama program adalah `algeo.App`.

- `bin`: berkas hasil kompilasi atau JAR final
- `docs`: laporan tugas besar
- `src`: kode sumber program
- `test`: berkas kasus uji

## Menjalankan program

Kompilasi proyek:

```bash
mvn clean compile
```

Jalankan program CLI:

```bash
mvn exec:java
```

Buat berkas JAR:

```bash
mvn clean package
```

Berkas JAR akan tersedia di dalam direktori `target`.

Untuk menggunakan JavaFX, sesuaikan kelas `App.java`, kemudian jalankan:

```bash
mvn clean javafx:run
```

Lengkapi kembali README kelompok dengan deskripsi program, alur penggunaan, dan cara menjalankan program sebelum pengumpulan.
