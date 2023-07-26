import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CoffeeShop coffeeShop = new CoffeeShop("Data_Pembeli.txt");
        coffeeShop.loadData();

        Scanner scanner = new Scanner(System.in);

        int mainChoice;
        boolean hasData = !coffeeShop.getCustomers().isEmpty();
        Map<String, Double> menu = TransaksiJualan.loadMenu("menu.txt"); // Load the menu

        Program program = new Program(coffeeShop, menu, scanner);

        do {

            System.out.println("\n===  Selamat Datang Di Ko-pilihan mu!  ===");
            System.out.println("\nMain Menu:");
            System.out.println("1. Data Pelanggan");
            System.out.println("2. Kasir");
            System.out.println("3. Riwayat Transaksi");
            System.out.println("4. Akhiri program");
            System.out.print("Masukkan Pilihanmu : ");
            mainChoice = scanner.nextInt();
            scanner.nextLine();

            switch (mainChoice) {
                case 1:
                    program.dataPelangganMenu();
                    break;

                case 2:
                    program.modeKasirMenu();
                    break;

                case 3:
                    program.riwayatTransaksiMenu();
                    break;

                case 4:
                    System.out.println("Keluar Dari Program.");
                    break;

                default:
                    System.out.println("Pilihan Tidak Tersedia. Silahkan Coba Lagi.");
                    break;
            }

            coffeeShop.saveData("Data_Pembeli.txt");
        } while (mainChoice != 4);
    }
}
