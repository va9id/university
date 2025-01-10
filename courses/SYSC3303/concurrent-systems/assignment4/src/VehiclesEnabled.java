/**
 * Vehicles Enabled super state
 */
public class VehiclesEnabled extends State{
    public VehiclesEnabled(boolean onEntry){
        if(onEntry) System.out.println("VehicleEnabled: don't walk");
    }
}
