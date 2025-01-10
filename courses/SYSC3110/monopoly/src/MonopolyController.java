import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Controller for the MVC
 * @author Connor Marcus
 */
public class MonopolyController implements ActionListener, Serializable {
    private MonopolyModel model;

    /**
     * Constructor for the class
     * @param model The MonopolyModel that this class acts as the controller for.
     */
    public MonopolyController(MonopolyModel model) {
        this.model = model;
    }

    /**
     * Overrides the actionPerformed method from the ActionListener interface.
     * @param e The ActionEvent that triggered this method.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            if (((JButton)e.getSource()).getText().equals(MonopolyView.ROLL)) {
                ((JButton)e.getSource()).setEnabled(false);
                this.model.takeTurn();
            }
            else if (((JButton)e.getSource()).getText().equals(MonopolyView.PASS)) {
                ((JButton)e.getSource()).setEnabled(false);
                this.model.passTurn();
            }
            else if (((JButton)e.getSource()).getText().equals(MonopolyView.BUY)) {
                ArrayList<PropertyStreet> properties = new ArrayList<>();
                Player turnPlayer = this.model.getPlayerList().get(model.getTurn());
                turnPlayer.getPropertyGroups(model.getBoard()).forEach((colour) -> {
                    model.getBoard().getPropertyGroup(colour).forEach((property) -> {
                        if (property.getNumHouses() < 5) {
                            properties.add(property);
                        }
                    });
                });
                JList list = new JList(properties.toArray());
                int choice = JOptionPane.showOptionDialog(null, new JScrollPane(list), MonopolyView.BUY, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{"Buy", "Cancel"}, 0);
                if (choice == 0 && list.getSelectedValue() != null) {
                    model.buyHouse((PropertyStreet) list.getSelectedValue());
                }
            }
        }

    }
}
