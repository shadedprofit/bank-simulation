package cscie55.hw4.bank;

public interface Account
{
    int id();
    long balance();
    void deposit(long amount) throws IllegalArgumentException;
    void withdraw(long amount) throws InsufficientFundsException, IllegalArgumentException;
}
