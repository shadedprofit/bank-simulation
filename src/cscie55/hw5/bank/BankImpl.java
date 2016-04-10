package cscie55.hw5.bank;

import java.util.HashMap;

public class BankImpl implements Bank {

    private HashMap<Integer, Account> accounts = new HashMap<Integer, Account>();

    public void addAccount(Account account) throws DuplicateAccountException {
        if (!accounts.containsKey(account.id())) {
            accounts.put(account.id(), account);
        } else {
            throw new DuplicateAccountException(account.id());
        }
    }

    public void deposit(int accountId, long amount) {
        Account a = accounts.get(accountId);
        a.deposit(amount);
    }

    public void transfer(int fromId, int toId, long amount) throws InsufficientFundsException {
        Account fromAccount = accounts.get(fromId);
        Account toAccount = accounts.get(toId);
        synchronized (fromAccount) {
            if (amount <= fromAccount.balance()) {
                try {
                    fromAccount.withdraw(amount);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InsufficientFundsException e) {
                    e.printStackTrace();
                }
            } else {
                throw new InsufficientFundsException(fromAccount, amount);
            }
        }

        synchronized (toAccount) {
            try {
                toAccount.deposit(amount);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public long totalBalances() {
        synchronized (accounts) {
            int totalbalance = 0;
            for (Account a : accounts.values()) {
                totalbalance += a.balance();
            }
            return totalbalance;
        }

    }

    public int numberOfAccounts() {
        return accounts.size();
    }
}