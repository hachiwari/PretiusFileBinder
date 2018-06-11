package pl.hachiwari.PretiusFileBinder;

import pl.hachiwari.PretiusFileBinder.module.FileBinder;

class App implements Runnable {

    private Thread thread;
    private boolean running;

    private final FileBinder fileBinder;

    private App() {
        fileBinder = new FileBinder();
    }


    public void run() {
        while(running) {
            if (!fileBinder.checkNewFiles()) {
                fileBinder.sort();
            }
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stop();
    }

    private synchronized void start() {
        if (running) {
            return;
        }

        running = true;
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        if (!running) {
            return;
        }

        running = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Run app
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }
}
