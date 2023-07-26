
class SavingAccount {
    private String name;
    private double balance;
    private double interestRate;
    private int pin;
    private int Id;

    // Constructor
    public SavingAccount(String name, double balance, double interestRate, int pin, int Id) {
        this.name = name;
        this.balance = balance;
        this.interestRate = interestRate;
        this.pin = pin;
        this.Id = Id;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public int getPin() {
        return pin;
    }
    public int getId() {
        return Id;
    }

    public double getInterestRate() {
        return interestRate;
    }

    // Deposit method
    public void deposit(double cash) {
        balance += getInterestRate() * cash;
    }

    // Overloaded deposit method for loans
    public void deposit(double cash, boolean isLoan) {
        balance -= cash;
    }

    // Apply interest method
    public void applyInterest() {
        double interestAmount = getInterestRate() * balance;
        balance += interestAmount;
    }
}

