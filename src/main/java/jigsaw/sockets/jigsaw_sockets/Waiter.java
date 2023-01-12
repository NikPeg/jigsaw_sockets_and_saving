package jigsaw.sockets.jigsaw_sockets;

import javafx.application.Platform;

public class Waiter implements Runnable {
//    private UserLoginController controller;
    private final Validation validation;
    private final Action action;

    public Waiter(Validation validation, Action action) {
//        this.controller = controller;
        this.validation = validation;
        this.action = action;
    }

    @Override
    public void run() {
        while (!(validation.checkSomething())) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Problems in current thread... Please restart.");
            }
        }
        Platform.runLater(action::doSomething);
    }
}
