package cscie55.hw5.bank;

import cscie55.hw5.bank.command.*;

import java.util.LinkedList;
import java.util.Queue;

public class BankServerImpl implements BankServer {

    private Queue<Command> commandQueue = new LinkedList<Command>();
    // change size later if necessary
    private CommandExecutionThread[] commandExecutionThreads;
    private Bank bank;

    public BankServerImpl(Bank bank, int threads, boolean executeCommandInsideMonitor) {
        // create and start command execution threads
        this.commandExecutionThreads = new CommandExecutionThread[threads];
        this.bank = bank;
        for (int i = 0; i < commandExecutionThreads.length; i++) {
            CommandExecutionThread t = new CommandExecutionThread(bank, commandQueue, executeCommandInsideMonitor);
            commandExecutionThreads[i] = t;
            t.start();
        }

    }

    public void execute(Command newCommand) {
        synchronized (commandQueue) {
            commandQueue.add(newCommand);
            commandQueue.notifyAll();
        }
    }

    public void stop() throws InterruptedException {
        for (CommandExecutionThread t : commandExecutionThreads) {
            execute(new CommandStop());
        }

        for (CommandExecutionThread t : commandExecutionThreads) {
            t.join();
        }
        return;

    }

    public long totalBalances() {
        return bank.totalBalances();
    }
}