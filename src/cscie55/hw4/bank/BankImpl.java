package cscie55.hw4.bank;

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

    public void transferWithoutLocking(int fromId, int toId, long amount) throws InsufficientFundsException {
        Account fromAccount = accounts.get(fromId);
        Account toAccount = accounts.get(toId);
        if (amount <= fromAccount.balance()) {
            try {
                fromAccount.withdraw(amount);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InsufficientFundsException e) {
                e.printStackTrace();
            }

            try {
                toAccount.deposit(amount);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        } else {
            throw new InsufficientFundsException(fromAccount, amount);
        }
    }

    public void transferLockingBank(int fromId, int toId, long amount) throws InsufficientFundsException {
        synchronized (this) {
            Account fromAccount = accounts.get(fromId);
            Account toAccount = accounts.get(toId);
            if (amount <= fromAccount.balance()) {
                try {
                    fromAccount.withdraw(amount);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InsufficientFundsException e) {
                    e.printStackTrace();
                }

                try {
                    toAccount.deposit(amount);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            } else {
                throw new InsufficientFundsException(fromAccount, amount);
            }
        }
    }

    public void transferLockingAccounts(int fromId, int toId, long amount) throws InsufficientFundsException {
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
        int totalbalance = 0;
        for (Account a : accounts.values()) {
            totalbalance += a.balance();
        }
        return totalbalance;
    }

    public int numberOfAccounts() {
        return accounts.size();
    }
}