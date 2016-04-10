package cscie55.hw5.bank;

import cscie55.hw5.bank.command.*;

import java.util.LinkedList;
import java.util.Queue;

public class CommandExecutionThread extends Thread {

    private static Queue<Command> commandQueue = new LinkedList<Command>();
    private static Bank bank;
    private static boolean executeCommandInsideMonitor;
    private boolean stop = false;

    public CommandExecutionThread(Bank bank, Queue<Command> commandQueue, boolean executeCommandInsideMonitor) {
        this.commandQueue = commandQueue;
        this.bank = bank;
        this.executeCommandInsideMonitor = executeCommandInsideMonitor;
    }

    public void run() {
        do {
            while (commandQueue.isEmpty()) {
                synchronized (commandQueue) {
                    try {
                        commandQueue.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Command command;
            synchronized (commandQueue) {
                command = commandQueue.remove();
                if (command.isStop()) {
                    stop = true;
                    break;
                }
                if (executeCommandInsideMonitor) {
                    try {
                        command.execute(bank);
                    } catch (InsufficientFundsException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!executeCommandInsideMonitor) {
                try {
                    command.execute(bank);
                } catch (InsufficientFundsException e) {
                    e.printStackTrace();
                }
            }
        } while (stop == false);
        return;
    }

}