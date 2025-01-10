/**
 * Pedestrian Flash sub state
 */
public class PedestriansFlash extends PedestriansEnabled{

    private int pedestrianFlashCtr;
    private boolean pedestrianSignaledDuringFlash;

    public PedestriansFlash(Context context) {
        super(false);
        pedestrianFlashCtr = 7;
        pedestrianSignaledDuringFlash = false;
        System.out.println("PedestriansFlash: pedestrians don't walk, signal is flashing");
        Thread flashThread = new Thread(() -> {
            while(pedestrianFlashCtr > 0) {
                if ((pedestrianFlashCtr & 1)==0) {
                    System.out.println("\tFLASH ON");
                } else {
                    System.out.println("\tFLASH OFF");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pedestrianFlashCtr -= 1;
            }
            context.timeout();
        });
        flashThread.start();
    }

    /**
     * Handle TIMEOUT event
     *
     * @param context current context
     */
    @Override
    public void timeout(Context context) {
        context.setState(new VehiclesGreen(context, getPedestrianSignaledDuringFlash()));
    }

    /**
     * Handle PEDESTRIAN_WAITING event
     *
     * @param context current context
     */
    @Override
    public synchronized void pedestrianWaiting(Context context) {
        pedestrianSignaledDuringFlash = true;
    }

    /**
     * Gets pedestrianSignaledDuringFlash
     *
     * @return boolean, true if pedestrian signaled during PedestrianFlash state
     */
    public synchronized boolean getPedestrianSignaledDuringFlash() {
        return pedestrianSignaledDuringFlash;
    }


}
