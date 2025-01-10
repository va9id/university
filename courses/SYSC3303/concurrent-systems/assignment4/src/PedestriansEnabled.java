/**
 * Pedestrian Enabled super state
 */
public class PedestriansEnabled extends State{

    public PedestriansEnabled(boolean onEntry) {
        if (onEntry) System.out.println("PedestriansEnabled: light is red for cars");;
    }

}

