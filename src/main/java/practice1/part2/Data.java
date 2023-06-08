package practice1.part2;

enum DataStates {
    TICKING,
    TAKING,
    TOYING,
}

public class Data {
    private DataStates state = DataStates.TICKING;

    public DataStates getState() { return state; }

    private void setState(DataStates state)
    {
        this.state = state;
        //System.out.println("\nSwitched to state " + this.state);
    }

    public synchronized void Tic(){
        waitUntilTheState(DataStates.TICKING);
        System.out.print("Tic-");
        setState(DataStates.TAKING);
        notifyAll();
    }
    public synchronized void Tak(){
        waitUntilTheState(DataStates.TAKING);
        System.out.print("Tak-");
        setState(DataStates.TOYING);
        notifyAll();
    }

    public synchronized void Toy(){
        waitUntilTheState(DataStates.TOYING);
        System.out.println("Toy");
        setState(DataStates.TICKING);
        notifyAll();
    }

    private synchronized void waitUntilTheState(DataStates state) {
        while(getState() != state) {
            //System.out.println("\nWaiting for " + state);
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

