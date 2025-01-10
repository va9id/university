/**
 * Context class of state pattern
 */
public class Context {
    private State state;

    public Context() {
        state = new VehiclesGreen(this, false);
    }

    /**
     * Invokes state method that handles TIMEOUT event
     */
    public void timeout() {
        try {
            System.out.println("\tTIMEOUT event received");
            state.timeout(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Invokes state method that handles PEDESTRIAN_WAITING event
     */
    public void pedestrianWaiting() {
        try {
            System.out.println("\tPEDESTRIAN_WAITING event received");
            state.pedestrianWaiting(this);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Sets state of the context
     *
     * @param s next state
     */
    public void setState(State s) {
        state = s;
    }

    /**
     * Gets current state
     *
     * @return current state
     */
    public synchronized State getState() { return state; }
}
