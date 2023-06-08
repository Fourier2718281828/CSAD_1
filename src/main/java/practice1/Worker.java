package practice1;

public class Worker extends Thread{

    private int id;
    private Data data;

    public Worker(int id, Data d){
        this.id = id;
        data = d;
        this.start();
    }

	/*
			There's an alternative solution. Just not correcting
			anything in the Data class and replace the inside of run method
			with the following:

				super.run();
				synchronized (data) {
					for (int i = 0; i < 5; i++){
						if(data.getState() == DataStates.TICKING) data.Tic();
						else data.Tak();
					}
				}

			But that doesn't seem to be what we were supposed to do
		*/

    @Override
    public void run(){
        super.run();
        for (int i = 0; i < 5; i++){
            if(id == 1) data.Tic();
            else data.Tak();
        }
    }

}
