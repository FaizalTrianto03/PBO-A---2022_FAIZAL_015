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

    public static void saveTransactionData(String receiptCode, String customerName, String customerNoTelp, double Uang_bayar, Map<String, Integer> purchasedItems, Map<String, Double> menu) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String transactionDate = dtf.format(now);

        try (PrintWriter writer = new PrintWriter(new FileWriter("Riwayat_Transaksi.txt", true))) {
            writer.println("\n===>>> Kode Transaksi: " + receiptCode + " <<<===");
            writer.println("\nNama Pelanggan : " + customerName);
            writer.println("Tanggal Transaksi: " + transactionDate);
            writer.println("No HP Pelanggan  : " + customerNoTelp);
            writer.println("Daftar Pembelian :");
            double totalAmount = 0.0;

            for (Map.Entry<String, Integer> entry : purchasedItems.entrySet()) {
                String itemName = entry.getKey();
                int quantity = entry.getValue();
                double itemPrice = menu.get(itemName);
                double totalPrice = itemPrice * quantity;

                writer.println("- " + itemName + " (Harga: " + itemPrice + ", Jumlah: " + quantity + ", Total: " + totalPrice + ")");
                totalAmount += totalPrice;
            }
            writer.println("\n" + purchasedItems.size() + " item, total belanja Rp." + totalAmount );
            writer.println("Uang Pembeli: Rp." + Uang_bayar);
            writer.println("Kembalian   : Rp." + (Uang_bayar - totalAmount));
            writer.println("----------------------------------------");
        } catch (IOException e) {
            System.out.println("Error occurred while saving the transaction data.");
        }
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
            System.out.println("Failed to display transaction history: " + e.getMessage());
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
