import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int pilihan;

        System.out.println("Pilih Aksi");
        System.out.println("1. Penjumlahan");
        System.out.println("2. Pengurangan");
        System.out.println("3. Perkalian");
        System.out.println("4. Pembagian");

        System.out.print("Masukkan pilihan Anda: ");
        pilihan = input.nextInt();

        switch (pilihan) {
            case 1:
                System.out.print("Masukkan bilangan pertama: ");
                int var1 = input.nextInt();
                System.out.print("Masukkan bilangan kedua: ");
                int var2 = input.nextInt();
                int hasilJumlah = var1 + var2;
                System.out.println("Hasil penjumlahan: " + hasilJumlah);
                break;
            case 2:
                System.out.print("Masukkan bilangan pertama: ");
                var1 = input.nextInt();
                System.out.print("Masukkan bilangan kedua: ");
                var2 = input.nextInt();
                int hasilKurang = var1 - var2;
                System.out.println("Hasil pengurangan: " + hasilKurang);
                break;
            case 3:
                System.out.print("Masukkan bilangan pertama: ");
                var1 = input.nextInt();
                System.out.print("Masukkan bilangan kedua: ");
                var2 = input.nextInt();
                int hasilKali = var1 * var2;
                System.out.println("Hasil perkalian: " + hasilKali);
                break;
            case 4:
                System.out.print("Masukkan bilangan pertama: ");
                var1 = input.nextInt();
                System.out.print("Masukkan bilangan kedua: ");
                var2 = input.nextInt();
                double hasilBagi = (double) var1 / var2;
                System.out.println("Hasil pembagian: " + hasilBagi);
                break;
            default:
                System.out.println("Pilihan tidak valid");
                break;
        }
    }
}
