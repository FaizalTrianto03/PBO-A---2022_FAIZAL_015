import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CoffeeShop {
    private List<Customer> customers;
    private String filename;

    public CoffeeShop(String filename) {
        this.filename = filename;
        customers = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        if (checkExistingCustomer(customer)) {
            System.out.println("\n<<Data Pelanggan sudah terdaftar sebelumnya.>>");
        } else {
            customers.add(customer);
            System.out.println("\n<<Data berhasil ditambahkan.>>");
        }
    }

    private boolean checkExistingCustomer(Customer customer) {
        for (Customer existingCustomer : customers) {
            if (existingCustomer.getName().equalsIgnoreCase(customer.getName()) &&
                    existingCustomer.getPhoneNumber().equals(customer.getPhoneNumber())) {
                return true;
            }
        }
        return false;
    }

    public void removeCustomerByName(String name) {
        if (customers.isEmpty()) {
            System.out.println("\nBelum ada data yang terdaftar.");
        } else {
            Iterator<Customer> iterator = customers.iterator();
            while (iterator.hasNext()) {
                Customer customer = iterator.next();
                if (customer.getName().equals(name)) {
                    iterator.remove();
                    System.out.println("\n<<Data pelanggan berhasil dihapus!>>");
                    return;
                }
            }
            System.out.println("\n<<Pelanggan tidak ditemukan.>>");
        }
    }

    public void resetData() {
        customers.clear();
        System.out.println("\n<<Seluruh data pelanggan berhasil dihapus.>>");
    }

    public void saveData(String filename) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            for (Customer customer : customers) {
                writer.println(customer.getName() + "," + customer.getEmail() + "," + customer.getPhoneNumber());
            }
//            System.out.println("\nData berhasil tersimpan!.");
        } catch (FileNotFoundException e) {
            System.out.println("\n<<Terjadi error ketika menyimpan data.>>");
        }
    }

    public void loadData() {
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                if (data.length == 3) {
                    String name = data[0];
                    String email = data[1];
                    String phoneNumber = data[2];

                    if (!email.endsWith("@gmail.com")) {
                        System.out.println("Format email tidak valid: " + email);
                        continue;
                    }
                    Customer customer = new Customer(name, email, phoneNumber);
                    customers.add(customer);
                }
            }
            System.out.println("Data berhasil dimuat.");
        } catch (FileNotFoundException e) {
            System.out.println("Terjadi kesalahan saat memuat data.");
        }
    }


    public void displayData() {
        if (customers.isEmpty()) {
            System.out.println("\nBelum ada data yang terdaftar.");
        } else {
            int index = 1;
            for (Customer customer : customers) {
                System.out.println("\nData " + index + ":");
                System.out.println("Nama : " + customer.getName());
                System.out.println("No HP: " + customer.getPhoneNumber());
                System.out.println("Email: " + customer.getEmail());
                System.out.println("----------------------");
                index++;
            }
        }
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public Customer getCustomerByName(String name) {
        for (Customer customer : customers) {
            if (customer.getName().equalsIgnoreCase(name)) {
                return customer;
            }
        }
        return null;
    }

    public static void displayCustomerTransactions(String filename, String customerName, String customerNoTelp) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            System.out.println("\nTransaksi atas nama: " + customerName);
            boolean found = false; // Flag to check if receipt code is found

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith(customerName + "(" + customerNoTelp + ")")) {
                    found = true;
                    // Skip printing customer name and phone number
                } else if (found && line.startsWith("----------------------------------------")) {
                    found = false; // Reached the end of the transaction entry
                    System.out.println(line); // Print the line with dashes
                } else if (found && !line.startsWith("Uang Pelanggan: Rp.") && !line.startsWith("Kembalian     : Rp.")) {
                    System.out.println(line); // Print other transaction details except for uang pembeli and kembalian
                }
            }
            scanner.close();

            if (!found) {
                System.out.println("Data tidak ditemukan.");
            }
        } catch (FileNotFoundException e) {
            System.out.println("\nGagal menampilkan riwayat transaksi: " + e.getMessage());
        }
    }


    public static void saveTransactionDataCustomer(String receiptCode,String voucherCode, String customerName, String customerNoTelp, double totalAmount, double paymentAmount, double changeAmount, Map<String, Integer> purchasedItems, Map<String, Double> menu) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd (HH:mm:ss)");
        LocalDateTime now = LocalDateTime.now();
        String transactionDate = dtf.format(now);

        try (PrintWriter writer = new PrintWriter(new FileWriter("Riwayat_Transaksi_Customers.txt", true))) {
            writer.println(customerName + "(" + customerNoTelp + ")");
            writer.println("\n===>>> Kode Transaksi: " + receiptCode + " <<<===");
            writer.println("\nNama Pelanggan   : " + customerName);
            writer.println("Tanggal Transaksi: " + transactionDate);
            writer.println("No HP Pelanggan  : " + customerNoTelp);
            writer.println("Daftar Pembelian :");

            // Menghitung lebar kolom berdasarkan data terpanjang
            int maxItemLength = "Item".length();
            int maxHargaLength = "Harga".length();
            int maxJumlahLength = "Jumlah".length();
            int maxTotalLength = "Total".length();

            for (Map.Entry<String, Integer> entry : purchasedItems.entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                double itemPrice = menu.get(itemName);
                double totalPrice = itemPrice * quantity;

                maxItemLength = Math.max(maxItemLength, itemName.length());
                maxHargaLength = Math.max(maxHargaLength, String.format("%.0f", itemPrice).length());
                maxJumlahLength = Math.max(maxJumlahLength, String.valueOf(quantity).length());
                maxTotalLength = Math.max(maxTotalLength, String.format("%.0f", totalPrice).length());
            }

            // Cetak header tabel
            String headerFormat = "| %-" + maxItemLength + "s | %-" + maxHargaLength + "s | %-" + maxJumlahLength + "s | %-" + maxTotalLength + "s |";
            writer.println("\n" + String.format(headerFormat, "Item", "Harga", "Jumlah", "Total"));
            writer.println("|" + "-".repeat(maxItemLength + 2) + "|" + "-".repeat(maxHargaLength + 2) + "|" + "-".repeat(maxJumlahLength + 2) + "|" + "-".repeat(maxTotalLength + 2) + "|");

            // Cetak detail pembelian
            for (Map.Entry<String, Integer> entry : purchasedItems.entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                double itemPrice = menu.get(itemName);
                double totalPrice = itemPrice * quantity;

                String formattedItemPrice = String.format("%d", (int) itemPrice);
                String formattedQuantity = String.format("%d", quantity);
                String formattedTotalPrice = String.format("%d", (int) totalPrice);

                writer.println("| " + String.format("%-" + maxItemLength + "s", itemName) + " | " +
                        String.format("%" + maxHargaLength + "s", formattedItemPrice) + " | " +
                        String.format("%" + maxJumlahLength + "s", quantity) + " | " +
                        String.format("%" + maxTotalLength + "s", formattedTotalPrice) + " |");

            }

            writer.println("|" + "-".repeat(maxItemLength + 2) + "|" + "-".repeat(maxHargaLength + 2) + "|" + "-".repeat(maxJumlahLength + 2) + "|" + "-".repeat(maxTotalLength + 2) + "|");
            writer.println("\n" + purchasedItems.size() + " item, total belanja Rp." + totalAmount);
            writer.println("Uang Pelanggan: Rp." + paymentAmount);
            writer.println("Kembalian     : Rp." + changeAmount);

            // Menghitung diskon dan menyimpan kode voucher jika total belanjaan lebih dari 250 ribu
            if (totalAmount >= 250000) {

            writer.println("\nSelamat, anda mendapatkan voucher diskon!");
            writer.println("Kode Voucher: " + voucherCode);

            }
            writer.println("----------------------------------------");
        } catch (IOException e) {
            System.out.println("\n<<Terjadi kesalahan selama proses penyimpanan data!>>");
        }
    }

}
