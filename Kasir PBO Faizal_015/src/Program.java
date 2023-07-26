import java.util.Map;
import java.util.Scanner;

public class Program {
    private CoffeeShop coffeeShop;
    private Map<String, Double> menu;
    private Scanner scanner;

    public Program(CoffeeShop coffeeShop, Map<String, Double> menu, Scanner scanner) {
        this.coffeeShop = coffeeShop;
        this.menu = menu;
        this.scanner = scanner;
    }

    public void dataPelangganMenu() {
        boolean pengulangan = true;

        System.out.println("\nData Pelanggan Menu:");
        System.out.println("1. Tampilkan Semua Data Pelanggan");
        System.out.println("2. Tambah Data Pelanggan");
        System.out.println("3. Hapus Data Pelanggan");
        System.out.println("4. Hapus Semua Data");
        System.out.println("5. Tampilkan Data Pelanggan Berdasarkan Nama dan Telepon");
        System.out.println("6. Kembali Ke Menu Utama");
        System.out.print("Masukkan Pilihanmu : ");
        int dataPembeliChoice = scanner.nextInt();
        scanner.nextLine();

        switch (dataPembeliChoice) {
            case 1:
                coffeeShop.displayData();
                break;

            case 2:
                addCustomerData();
                break;

            case 3:
                removeCustomerData();
                break;

            case 4:
                resetCustomerData();
                break;

            case 5:
                displayCustomerDataByNameAndPhone();
                break;

            case 6:
                System.out.println("Kembali Ke Menu Utama.");
                pengulangan = false;
                break;

            default:
                System.out.println("Pilihan Tidak Tersedia, Silahkan Coba Lagi.");
                break;
        }
        if (pengulangan){
            dataPelangganMenu();
        }
    }

    public void addCustomerData() {
        System.out.println("\nIsi Keterangan Berikut Ini :");
        System.out.print("Nama : ");
        String name = scanner.nextLine();
        System.out.print("No Hp: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        coffeeShop.addCustomer(new Customer(name, email, phoneNumber));
        coffeeShop.saveData("Data_Pembeli.txt");
    }

    public void removeCustomerData() {
        System.out.print("Masukkan nama pelanggan yang ingin dihapus: ");
        String nameToRemove = scanner.nextLine();
        coffeeShop.removeCustomerByName(nameToRemove);
    }

    public void resetCustomerData() {
        System.out.print("Apakah Anda yakin ingin mengosongkan data pelanggan? (y/t): ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("y")) {
            coffeeShop.resetData();
            System.out.println("Data pelanggan telah dihapus.");
        } else {
            System.out.println("Pengosongan data dibatalkan.");
        }
    }

    public void displayCustomerDataByNameAndPhone() {
        System.out.print("Masukkan Nama Pelanggan : ");
        String searchName = scanner.nextLine();
        System.out.print("Masukkan No Hp Pelanggan: ");
        String searchPhoneNumber = scanner.nextLine();

        coffeeShop.displayCustomerTransactions("Riwayat_Transaksi_Customers.txt", searchName, searchPhoneNumber);
    }

    public void modeKasirMenu() {
        boolean pengulangan = true;

        System.out.println("\nMode Kasir Menu:");
        System.out.println("1. Tampilkan Semua Menu");
        System.out.println("2. Transaksi");
        System.out.println("3. Kembali Ke Menu Utama");
        System.out.print("Masukkan Pilihanmu : ");
        int cashierChoice = scanner.nextInt();
        scanner.nextLine();

        switch (cashierChoice) {
            case 1:
                TransaksiJualan.displayMenu(menu);
                break;

            case 2:
                performTransaction();
                break;

            case 3:
                System.out.println("Kembali Ke Menu Utama.");
                pengulangan = false;
                break;

            default:
                System.out.println("Pilihan Tidak Tersedia. Silahkan Coba Lagi.");
                break;
        }
        if (pengulangan){
            modeKasirMenu();
        }
    }

    public void performTransaction() {
        System.out.println("\n==>> Mode Transaksi <<==");
        System.out.print("Nama Pelanggan  : ");
        String customerName = scanner.nextLine();
        System.out.println("Selamat Datang, " + customerName + "!");

        System.out.print("No HP Pelanggan : ");
        String customerPhoneNumber = scanner.nextLine();

        Customer existingCustomer = coffeeShop.getCustomerByName(customerName);
        if (existingCustomer != null && existingCustomer.getPhoneNumber().equals(customerPhoneNumber)) {
            System.out.println("Data pelanggan sudah ada dalam database.");
            System.out.println("Nama Pelanggan  : " + existingCustomer.getName());
            System.out.println("No Telepon      : " + existingCustomer.getPhoneNumber());
        } else {
            System.out.println("Pelanggan belum terdaftar, isikan email.");
            System.out.print("Email Pelanggan : ");
            String customerEmail = scanner.nextLine();

            coffeeShop.addCustomer(new Customer(customerName, customerEmail, customerPhoneNumber));
            coffeeShop.saveData("Data_Pembeli.txt");

        }

        // Mengumpulkan data pembelian
        Map<String, Integer> purchasedItems = TransaksiJualan.collectPurchasedItems(menu);

        double totalAmount = TransaksiJualan.performTransaction(purchasedItems, menu); // Memanggil metode performTransaction dari TransaksiJualan
        System.out.print("Total Uang Pelanggan: ");
        double paymentAmount = scanner.nextDouble();
        scanner.nextLine();

        if (paymentAmount < totalAmount) {
            System.out.println("Maaf Uang Tidak Mencukupi. Silahkan Mengulangi Transaksi.");
        } else {
            double changeAmount = paymentAmount - totalAmount;
            System.out.println("Total Belanja : Rp." + totalAmount);
            System.out.println("Uang Pelanggan: Rp." + paymentAmount);
            System.out.println("Kembalian     : Rp." + changeAmount);
            System.out.println("Terima Kasih, " + customerName + " Telah Berbelanja Di Ko-pilihan mu!");

            // Save transaction data to the file
            String receiptCode = TransaksiJualan.generateReceiptCode();
            TransaksiJualan.saveTransactionData(receiptCode, customerName, customerPhoneNumber, paymentAmount, purchasedItems, menu);
            coffeeShop.saveTransactionDataCustomer(receiptCode, customerName, customerPhoneNumber, paymentAmount, purchasedItems, menu);
            TransaksiJualan.displayTransactionHistory("Riwayat_Transaksi.txt", receiptCode);
        }
    }

    public void riwayatTransaksiMenu() {
        boolean pengulangan = true;

        System.out.println("\nRiwayat Transaksi Menu:");
        System.out.println("1. Tampilkan Riwayat Transaksi");
        System.out.println("2. Kembali Ke Menu Utama.");
        System.out.print("Masukkan Pilihanmu : ");
        int transactionHistoryChoice = scanner.nextInt();
        scanner.nextLine();

        switch (transactionHistoryChoice) {
            case 1:
                System.out.print("Masukkan Kode Struk: ");
                String receiptCode = scanner.nextLine();
                TransaksiJualan.displayTransactionHistory("Riwayat_Transaksi.txt", receiptCode);
                break;

            case 2:
                System.out.println("Kembali Ke Menu Utama.");
                pengulangan = false;
                break;

            default:
                System.out.println("Pilihan Tidak Tersedia. Silahkan Coba Lagi.");
                break;
        }
        if (pengulangan){
            modeKasirMenu();
        }
    }
}
