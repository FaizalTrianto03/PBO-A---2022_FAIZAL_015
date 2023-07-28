import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.io.PrintWriter;

public class TransaksiJualan {

    public static Map<String, Integer> collectPurchasedItems(Map<String, Double> menu) {
        Map<String, Integer> purchasedItems = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        List<String> foodNames = new ArrayList<>();
        List<String> drinkNames = new ArrayList<>();

        // Mengisi foodNames dan drinkNames dengan data dari menu
        for (Map.Entry<String, Double> entry : menu.entrySet()) {
            String itemName = entry.getKey();
            if (itemName.startsWith("Makanan")) {
                foodNames.add(itemName);
            } else if (itemName.startsWith("Minuman")) {
                drinkNames.add(itemName);
            }
        }

        displayMenu(menu);

        String choice;
        do {
            System.out.print("\nEnter the code number to order (or 'q' to finish): ");
            choice = scanner.nextLine();
            if (choice.equals("q")) {
                break;
            }
            try {
                int codeNumber = Integer.parseInt(choice);
                int totalMenuCount = foodNames.size() + drinkNames.size();
                if (codeNumber >= 1 && codeNumber <= totalMenuCount) {
                    String itemName;
                    if (codeNumber <= foodNames.size()) {
                        itemName = foodNames.get(codeNumber - 1);
                    } else {
                        itemName = drinkNames.get(codeNumber - foodNames.size() - 1);
                    }
                    System.out.print("Enter the quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    if (purchasedItems.containsKey(itemName)) {
                        int existingQuantity = purchasedItems.get(itemName);
                        purchasedItems.put(itemName, existingQuantity + quantity);
                    } else {
                        purchasedItems.put(itemName, quantity);
                    }
                } else {
                    System.out.println("Invalid code number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
            }
        } while (true);

        return purchasedItems;
    }


    public static double performTransaction(Map<String, Integer> purchasedItems, Map<String, Double> menu) {
        double totalAmount = calculateTotalAmount(purchasedItems, menu);
        System.out.println("Total Amount: Rp." + totalAmount);
        return totalAmount;
    }

    public static double calculateTotalAmount(Map<String, Integer> purchasedItems, Map<String, Double> menu) {
        double totalAmount = 0.0;
        for (Map.Entry<String, Integer> entry : purchasedItems.entrySet()) {
            String itemName = entry.getKey();
            int quantity = entry.getValue();
            double price = menu.get(itemName);
            totalAmount += price * quantity;
        }
        return totalAmount;
    }

    public static void payment(double totalAmount, String customerName) {
        Scanner scanner = new Scanner(System.in);
        double paymentAmount = 0.0;

        while (paymentAmount < totalAmount) {
            System.out.print("Enter customer's payment amount: ");
            paymentAmount = scanner.nextDouble();
            scanner.nextLine();

            if (paymentAmount < totalAmount) {
                System.out.println("Insufficient payment amount. Please enter a valid payment amount.");
            }
        }

        double changeAmount = paymentAmount - totalAmount;
        System.out.println("Total Amount: Rp." + totalAmount);
        System.out.println("Payment Amount: Rp." + paymentAmount);
        System.out.println("Change Amount: Rp." + changeAmount);
        System.out.println("Thank you, " + customerName + " for your purchase!");
    }

    public static void saveTransactionData(String receiptCode,String voucherCode, String customerName, String customerNoTelp, double totalAmount, double paymentAmount, double changeAmount, Map<String, Integer> purchasedItems, Map<String, Double> menu) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd (HH:mm:ss)");
        LocalDateTime now = LocalDateTime.now();
        String transactionDate = dtf.format(now);
        int totalasli = 0;

        try (PrintWriter writer = new PrintWriter(new FileWriter("Riwayat_Transaksi.txt", true))) {
            writer.println("\n===>>> Kode Transaksi: " + receiptCode + " <<<===");
            writer.println("\nNama Pelanggan   : " + customerName);
            writer.println("Tanggal Transaksi: " + transactionDate);
            writer.println("No HP Pelanggan  : " + customerNoTelp);

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
                maxHargaLength = Math.max(maxHargaLength, String.valueOf(itemPrice).length());
                maxJumlahLength = Math.max(maxJumlahLength, String.valueOf(quantity).length());
                maxTotalLength = Math.max(maxTotalLength, String.valueOf(totalPrice).length());
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
                        String.format("%" + maxJumlahLength + "s", formattedQuantity) + " | " +
                        String.format("%" + maxTotalLength + "s", formattedTotalPrice) + " |");
                totalasli += totalPrice;
            }

            writer.println("|" + "-".repeat(maxItemLength + 2) + "|" + "-".repeat(maxHargaLength + 2) + "|" + "-".repeat(maxJumlahLength + 2) + "|" + "-".repeat(maxTotalLength + 2) + "|");
            writer.println("\n" + purchasedItems.size() + " item, total belanja Rp. " + totalAmount);
            writer.println("Uang Pelanggan: Rp. " + paymentAmount);
            writer.println("Kembalian     : Rp. " + changeAmount);


            // Menghitung diskon dan menyimpan kode voucher jika total belanjaan lebih dari 250 ribu
            if (totalasli >= 250000) {
                double diskon = (totalasli / 250000) * 0.025; // Diskon sebesar 2.5% dari total belanja

                // Simpan kode voucher dan besar diskon ke file "kodevoucher.txt"
                try (PrintWriter voucherWriter = new PrintWriter(new FileWriter("kodevoucher.txt", true))) {
                    voucherWriter.println(voucherCode + "," + diskon);
                    writer.println("\nSelamat, anda mendapatkan voucher diskon!");
                    writer.println("Kode Voucher: " + voucherCode);
                } catch (IOException e) {
                    System.out.println("Gagal menyimpan data kode voucher.");
                }
            }
            writer.println("----------------------------------------");
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan saat menyimpan data transaksi.");
        }
    }

    public static String generateVoucherCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder code = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            code.append(characters.charAt(index));
        }

        return code.toString();
    }


    public static String generateReceiptCode() {
        Random random = new Random();
        int code = random.nextInt(90000000) + 10000000; // Generate a random number between 10000000 and 99999999
        return String.valueOf(code);
    }

    public static void displayTransactionHistory(String filename, String receiptCode) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            boolean found = false; // Flag to check if receipt code is found
            System.out.println("\n");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("===>>> Kode Transaksi: " + receiptCode + " <<<===")) {
                    found = true;
                }
                if (found) {
                    if (!line.startsWith("No HP Pelanggan  : ")) { // Tidak menampilkan baris yang berisi nomor telepon
                        System.out.println(line);
                    }
                }
                if (line.startsWith("----------------------------------------")) {
                    found = false; // Reached the end of the transaction entry
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Gagal menampilkan riwayat transaksi : " + e.getMessage());
        }
    }

    public static void displayMenu(Map<String, Double> menu) {
        System.out.println("\nMakanan :");
        int foodIndex = 1;
        int maxFoodNameLength = 0;

        for (Map.Entry<String, Double> entry : menu.entrySet()) {
            String itemName = entry.getKey();
            double price = entry.getValue();
            if (!itemName.startsWith("Minuman")) {
                maxFoodNameLength = Math.max(maxFoodNameLength, itemName.length());
            }
        }

        for (Map.Entry<String, Double> entry : menu.entrySet()) {
            String itemName = entry.getKey();
            double price = entry.getValue();
            if (!itemName.startsWith("Minuman")) {
                String formattedItemName = String.format("%-" + (maxFoodNameLength + 2) + "s", itemName);
                System.out.printf("%2d. %s: Rp.%.1f\n", foodIndex, formattedItemName, price);
                foodIndex++;
            }
        }

        System.out.println("\nMinuman :");
        int drinkIndex = foodIndex; // Start indexing from foodIndex
        for (Map.Entry<String, Double> entry : menu.entrySet()) {
            String itemName = entry.getKey();
            double price = entry.getValue();
            if (itemName.startsWith("Minuman")) {
                System.out.printf("%2d. %-23s: Rp.%.1f\n", drinkIndex, itemName, price);
                drinkIndex++;
            }
        }
    }



    private static List<String> getFoodNames(Map<String, Double> menu) {
        List<String> foodNames = new ArrayList<>();
        for (Map.Entry<String, Double> entry : menu.entrySet()) {
            String itemName = entry.getKey();
            if (!itemName.startsWith("Minuman")) {
                foodNames.add(itemName);
            }
        }
        return foodNames;
    }

    private static List<String> getDrinkNames(Map<String, Double> menu) {
        List<String> drinkNames = new ArrayList<>();
        for (Map.Entry<String, Double> entry : menu.entrySet()) {
            String itemName = entry.getKey();
            if (itemName.startsWith("Minuman")) {
                drinkNames.add(itemName);
            }
        }
        return drinkNames;
    }

    public static Map<String, Double> loadMenu(String filename) {
        Map<String, Double> menu = new HashMap<>();
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String foodName = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    menu.put(foodName, price);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Failed to load menu: " + e.getMessage());
        }
        return menu;
    }
}
