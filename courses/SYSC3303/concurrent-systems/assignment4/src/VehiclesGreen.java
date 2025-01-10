/**
 * Vehicles Green sub state
 */
public class VehiclesGreen extends VehiclesEnabled {

    private boolean isPedestrianWaiting;

    public VehiclesGreen(Context context, boolean signalDuringFlash) {
        super(true);
        isPedestrianWaiting = signalDuringFlash;
        System.out.println("VehicleGreen: light is green for cars");
        Thread greenThread = new Thread(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            context.timeout();
        });
        greenThread.start();
    }

    /**
     * Handle TIMEOUT event
     *
     * @param context current context
     */
    @Override
    public void timeout(Context context) {
        if (getPedestrianWaiting()) {
            context.setState(new VehiclesYellow(context));
        } else {
            context.setState(new VehiclesGreenInt(context));
        }
    }

    /**
     * Handle PEDESTRIAN_WAITING event
     * @param context current context
     */
    @Override
    public synchronized void pedestrianWaiting(Context context) { isPedestrianWaiting = true; }

    /**
     * Gets isPedestrianWaiting
     *
     * @return boolean, true if pedestrian signaled during VehiclesGreen state
     */
    public synchronized boolean getPedestrianWaiting() {
        return isPedestrianWaiting;
    }
}