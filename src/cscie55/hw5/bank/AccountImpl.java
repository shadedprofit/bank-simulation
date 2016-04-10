package cscie55.hw5.bank;

public class AccountImpl implements Account {

    private int id;
    private long balance;

    public AccountImpl(int id) {
        this.id = id;
        this.balance = 0;
    }

    public int id() {
        return id;
    }

    public long balance() {
        return balance;
    }

    public void deposit(long amount) throws IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Illegal Argument");
        }
        balance += amount;
    }

    public void withdraw(long amount) throws InsufficientFundsException, IllegalArgumentException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Illegal Argument");
        } else if (balance - amount < 0) {
            throw new InsufficientFundsException(this, amount);
        } else {
            balance -= amount;
        }
    }
}