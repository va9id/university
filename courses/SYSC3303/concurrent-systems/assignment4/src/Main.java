/**
 * Main class that invokes the state machine
 */
public class Main {
    public static void main(String[] args) {
        Context c = new Context();
        int i = 1;
        /* Cycles through full state machine 3 times
         * - 1st pass: triggers PEDESTRIAN_WAITING event during VehicleGreen state
         * - 1st pass: triggers PEDESTRIAN_WAITING event during PedestrianFlash state
         * - 2nd pass: Transitions from VehicleGreen state --> VehicleYellow state because of PEDESTRIAN_WAITING event
         *             that was triggered during prior PedestrianFlash state (pedestrian has been waiting to cross)
         * - 3rd pass: triggers PEDESTRIAN_WAITING event during VehicleGreenInt state
         *
         * Main method will finish execution after completing the 3rd pass of the state machine.
         * In order to run the main method forever, change the while loop condition to while(i < 5), this will result
         * in the state machine to remain in the VehicleGreenInt state.
         */
        while(i < 4) {
            State s = c.getState();

            if (s instanceof VehiclesGreen && i == 1) {
                c.pedestrianWaiting();
                i += 1;
            }
            else if (s instanceof PedestriansFlash && i == 2) {
                c.pedestrianWaiting();
                i += 1;
            }
            else if (s instanceof VehiclesGreenInt && i == 3) {
                c.pedestrianWaiting();
                i += 1;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
