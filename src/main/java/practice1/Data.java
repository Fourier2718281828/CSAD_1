package practice1;

enum DataStates {
    TICKING,
    TAKING,
    TOYING,
}

public class Data {
    private DataStates state = DataStates.TICKING;

    public DataStates getState() { return state; }

    public synchronized void Tic(){
        waitUntilTheState(DataStates.TICKING);
        System.out.print("Tic-");
        state = DataStates.TAKING;
        notify();
    }
    public synchronized void Tak(){
        waitUntilTheState(DataStates.TAKING);
        System.out.println("Tak");
        state = DataStates.TICKING;
        notify();
    }

    private void waitUntilTheState(DataStates state) {
        while(getState() != state) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

