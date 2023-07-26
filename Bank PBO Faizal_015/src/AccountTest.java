import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AccountTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Selamat datang di Aplikasi Perbankan");

        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Menu:");
            System.out.println("1. Masuk ke Akun");
            System.out.println("2. Daftar Akun");
            System.out.println("0. Keluar");
            System.out.print("Pilihan Anda: ");
            int menuChoice = scanner.nextInt();

            switch (menuChoice) {
                case 1:
                    System.out.print("Masukkan ID akun: ");
                    int id = scanner.nextInt();

                    System.out.print("Masukkan PIN akun anda: ");
                    int pin = scanner.nextInt();

                    List<SavingAccount> accounts = loadData("nasabah.txt");
                    int accountIndex = findAccountIndex(accounts, id, pin);

                    if (accountIndex != -1) {
                        SavingAccount account = accounts.get(accountIndex);
                        System.out.println("\nSelamat datang, " + account.getName() + "!");
                        showAccountInfo(accounts, accountIndex);


                        System.out.print("\nApakah Anda ingin melakukan deposit atau pinjaman? (D/P): ");
                        String choice = scanner.next();

                        if (choice.equalsIgnoreCase("D")) {
                            System.out.print("Masukkan jumlah deposit: ");
                            double jumlahDeposit = scanner.nextDouble();
                            account.deposit(jumlahDeposit);
                        } else if (choice.equalsIgnoreCase("P")) {
                            System.out.print("Masukkan jumlah pinjaman: ");
                            double jumlahPinjaman = scanner.nextDouble();
                            account.deposit(jumlahPinjaman, true);
                        } else {
                            System.out.println("Pilihan tidak valid.");
                        }

                        account.applyInterest();
                        showAccountInfo(accounts, accountIndex);

                        // Perbarui data akun dalam List
                        accounts.set(accountIndex, account);
                        saveData("nasabah.txt", accounts);
                    } else {
                        System.out.println("Akun tidak ditemukan. Silakan coba lagi.");
                    }

                    break;
                case 2:
                    boolean isPinValid = false;

                    while (!isPinValid) {
                        System.out.print("Masukkan nama pemilik akun: ");
                        scanner.nextLine(); // Consumes the newline character
                        String newName = scanner.nextLine();

                        System.out.print("Masukkan PIN akun: ");
                        int newPin = scanner.nextInt();

                        // Validasi panjang PIN
                        if (String.valueOf(newPin).length() != 6) {
                            System.out.println("PIN harus terdiri dari 6 digit. Silakan coba lagi.");
                        } else {
                            isPinValid = true;

                            System.out.print("Masukkan saldo awal akun: ");
                            double newBalance = scanner.nextDouble();

                            System.out.print("Masukkan tingkat bunga akun: ");
                            double newInterestRate = scanner.nextDouble();

                            int newId = generateRandomId();

                            SavingAccount newAccount = new SavingAccount(newName, newBalance, newInterestRate, newPin, newId);
                            List<SavingAccount> newAccounts = loadData("nasabah.txt");
                            newAccounts.add(newAccount);
                            saveData("nasabah.txt", newAccounts);

                            System.out.println("\nAkun berhasil didaftarkan!");
                            showAccountInfo(newAccount);
                        }
                    }

                    break;

                case 0:
                    isRunning = false;
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                    break;
            }

            System.out.println();
        }

        System.out.println("Terima kasih telah menggunakan Aplikasi Perbankan.");
        scanner.close();
    }

    public static List<SavingAccount> loadData(String filename) {
        List<SavingAccount> savingAccounts = new ArrayList<>();

        try (Scanner fileScanner = new Scanner(new File(filename))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                String name = parts[0];
                double balance = Double.parseDouble(parts[1]);
                double interestRate = Double.parseDouble(parts[2]);
                int pin = Integer.parseInt(parts[3]);
                int id = Integer.parseInt(parts[4]);

                SavingAccount account = new SavingAccount(name, balance, interestRate, pin, id);
                savingAccounts.add(account);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error occurred while loading the data.");
        }

        return savingAccounts;
    }

    public static void saveData(String filename, List<SavingAccount> savingAccounts) {
        try (PrintWriter writer = new PrintWriter(new File(filename))) {
            for (SavingAccount savingAccount : savingAccounts) {
                writer.println(
                        savingAccount.getName() + "," + savingAccount.getBalance() + ","
                                + savingAccount.getInterestRate() + "," + savingAccount.getPin() + "," + savingAccount.getId()
                );
            }
            System.out.println("Data saved successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Error occurred while saving the data.");
        }
    }

    public static int findAccountIndex(List<SavingAccount> accounts, int id, int pin) {
        for (int i = 0; i < accounts.size(); i++) {
            SavingAccount account = accounts.get(i);
            if (account.getId() == id && account.getPin() == pin) {
                return i;
            }
        }
        return -1;
    }

    public static void showAccountInfo(List<SavingAccount> accounts, int index) {
        if (index >= 0 && index < accounts.size()) {
            SavingAccount account = accounts.get(index);
            System.out.println("ID Akun Pengguna : " + account.getId());
            System.out.println("Nama pemilik akun: " + account.getName());
            System.out.println("Saldo akun: " + account.getBalance());
        } else {
            System.out.println("Indeks akun tidak valid.");
        }
    }

    public static void showAccountInfo(SavingAccount account) {
        System.out.println("ID Akun Pengguna : " + account.getId());
        System.out.println("Nama pemilik akun: " + account.getName());
        System.out.println("Saldo akun: " + account.getBalance());
    }


    public static int generateRandomId() {
        Random random = new Random();
        return random.nextInt(900000) + 100000; // Generates a 6-digit random number
    }
}
