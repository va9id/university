/**
 * Vehicles Yellow sub state
 */
public class VehiclesYellow extends VehiclesEnabled {

    public VehiclesYellow(Context context){
        super(false);
        System.out.println("VehicleYellow: light is yellow for cars");
        Thread yellowThread = new Thread(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            context.timeout();
        });
        yellowThread.start();
    }

    /**
     * Handle TIMEOUT event
     *
     * @param context current context
     */
    @Override
    public void timeout(Context context) {
        context.setState(new PedestriansWalk(context));
    }
}
