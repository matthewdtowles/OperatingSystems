package prodconsync;

/**
 * Semaphore
 * @author matthew.towles
 */
class Semaphore {
    // identifier
    private int sem;

    @Override
    public synchronized void wait() {
        while (sem <= 0) {
            try {
                swait();
            } catch (Exception e) { System.exit(0); }
        }
        sem--;
    }

    public synchronized void signal() {
        sem++;
        notify();
    }

    // constructor
    public Semaphore (int intval) {
        sem = intval;
    }
}