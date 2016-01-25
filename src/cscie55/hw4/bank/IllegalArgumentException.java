package cscie55.hw4.bank;

public class IllegalArgumentException extends Exception {
    public IllegalArgumentException(Account account, long amount) {
        super(String.format("Illegal argument: %s for account: %d", account, amount));
    }
}