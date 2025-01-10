/**
 * Pedestrian Walk sub state
 */
public class PedestriansWalk extends PedestriansEnabled {

    public PedestriansWalk(Context context){
        super(true);
        System.out.println("PedestriansWalk: Pedestrians walk");
        Thread walkThread = new Thread(() -> {
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            context.timeout();
        });
        walkThread.start();
    }

    /**
     * Handle TIMEOUT event
     * @param context current context
     */
    @Override
    public void timeout(Context context) {
        context.setState(new PedestriansFlash(context));
    }
}

