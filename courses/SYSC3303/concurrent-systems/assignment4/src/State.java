/**
 * State class of state pattern
 */
public abstract class State {

    /**
     * TIMEOUT event
     *
     * @param context current context
     * @throws Exception method is called from an invalid state
     */
    public void timeout(Context context) throws Exception {
        throw new Exception("timeout() cannot be called from " + this.getClass().getName());
    }

    /**
     * PEDESTRIAN_WAITING event
     *
     * @param context current context
     * @throws Exception method is called from an invalid state
     */
    public void pedestrianWaiting(Context context) throws Exception {
        throw new Exception("pedestrianWaiting() cannot be called from " + this.getClass().getName());
    }
}
