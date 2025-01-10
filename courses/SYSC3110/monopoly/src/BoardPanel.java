import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 * BoardPanel class which handles the game board.
 *
 * @author Connor Marcus
 */
public class BoardPanel extends JPanel implements Serializable {
    static final int PANEL_WIDTH = 693;
    static final int PANEL_HEIGHT = 693;
    private MonopolyBoard board;
    private Map<String, JLabel> playerLabelMap;
    private JLabel[] spaces;
    private JLabel[] nameLabels;
    private JLabel[] houses;
    private JLabel dice1;
    private JLabel dice2;


    /**
     * Constructor of the class
     * @param board The board this panel is based on
     * @param players The players that will be displayed on this panel
     */
    public BoardPanel(MonopolyBoard board, List<Player> players) {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        this.setBackground(new Color(211, 236, 211));
        this.board = board;
        this.playerLabelMap = new HashMap<>();
        this.spaces = new JLabel[board.getNumProperties()];
        this.nameLabels = new JLabel[board.getNumProperties()];
        this.houses = new JLabel[board.getNumProperties()];
        this.dice1 = new JLabel();
        this.dice2 = new JLabel();
        initializeSpaces();
        players.forEach(this::addPlayerLabel);
        addLabels();
    }

    /**
     * Initializes JPanel game board.
     */
    private void initializeSpaces() {
        for (int i=0; i<board.getNumProperties(); i++) {
            spaces[i] = new JLabel();
            nameLabels[i] = new JLabel();
        }

        Set<Integer> customizableProperties = getCustomizableProperties();
        int x = 0;
        int y = 0;
        int WIDTH = PANEL_WIDTH / 11;
        int HEIGHT = PANEL_HEIGHT / 11;

        //Initialize top and bottom spaces
        for (int i=20; i<31; i++) {
            if (customizableProperties.contains(i)) {
                int offset = board.getProperty(i) instanceof PropertyRailroad ? HEIGHT-10 : HEIGHT-25;
                createNameLabel(offset, Math.PI, i, x, y);
            }
            if (customizableProperties.contains(30-i)) {
                int offset = board.getProperty(30-i) instanceof PropertyRailroad ? 10 : 25;
                createNameLabel(offset, 0, 30-i, x, y+PANEL_HEIGHT-HEIGHT);
            }
            try {
                //All the top images are rotated 180 degrees
                BufferedImage image1 = rotateImage(ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource(String.format("images/%s.png", board.getProperty(i).getName())))), Math.PI);
                spaces[i].setIcon(new ImageIcon(image1));
                spaces[i].setBounds(x, y, spaces[i].getPreferredSize().width, spaces[i].getPreferredSize().height);
                this.add(spaces[i]);

                //All the bottom images do not need to be rotated
                BufferedImage image2 = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource(String.format("images/%s.png", board.getProperty(30-i).getName()))));
                spaces[30-i].setIcon(new ImageIcon(image2));
                spaces[30-i].setBounds(x, y+PANEL_HEIGHT-HEIGHT, spaces[30-i].getPreferredSize().width, spaces[30-i].getPreferredSize().height);
                this.add(spaces[30-i]);
            }
            catch (Exception e) {
                System.err.println("Error loading image! " + e.getMessage());
                System.exit(1);
            }
            x += WIDTH;
        }

        x -= WIDTH;
        y += HEIGHT;

        //Initialize right and left spaces
        for (int i=31; i<40; i++) {
            if (customizableProperties.contains(i)) {
                int offset = board.getProperty(i) instanceof PropertyRailroad ? 10 : 25;
                createNameLabel(offset, 1.5*Math.PI, i, x, y);
            }
            if (customizableProperties.contains(50-i)) {
                int offset = board.getProperty(50-i) instanceof PropertyRailroad ? WIDTH-10 : WIDTH-25;
                createNameLabel(offset,Math.PI/2, 50-i, x-PANEL_WIDTH+WIDTH, y);
            }
            try {
                //All the right images are rotated 270 degrees
                BufferedImage image1 = rotateImage(ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource(String.format("images/%s.png", board.getProperty(i).getName())))), 1.5*Math.PI);
                spaces[i].setIcon(new ImageIcon(image1));
                spaces[i].setBounds(x, y, spaces[i].getPreferredSize().width, spaces[i].getPreferredSize().height);
                this.add(spaces[i]);

                //All the left images are rotated by 90 degrees
                BufferedImage image2 = rotateImage(ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource(String.format("images/%s.png", board.getProperty(50-i).getName())))), Math.PI/2);
                spaces[50-i].setIcon(new ImageIcon(image2));
                spaces[50-i].setBounds(x-PANEL_WIDTH+WIDTH, y, spaces[50-i].getPreferredSize().width, spaces[50-i].getPreferredSize().height);
                this.add(spaces[50-i]);
            }
            catch (Exception e) {
                System.err.println("Error loading image!");
                System.exit(1);
            }
            y += WIDTH;
        }
    }

    /**
     * Helper method to create the name JLabels
     * @param offset The offset of the name label within the space
     * @param i The index of the nameLabel
     * @param x The x position of the space
     * @param y The y position of the space
     */
    private void createNameLabel(int offset, double angle, int i, int x, int y) {
        nameLabels[i] = new JLabel(new PropertyNameIcon(board.getProperty(i).getName(), angle, offset));
        nameLabels[i].setBounds(x, y, nameLabels[i].getPreferredSize().width, nameLabels[i].getPreferredSize().height);
        this.add(nameLabels[i]);
    }

    /**
     * Gets a set of all the properties whose names can be customized (street properties and railroads)
     * @return a set of all the indices on the board whose names can be customized by the user
     */
    private Set<Integer> getCustomizableProperties() {
        Set<Integer> customizableProperties = new HashSet<>();
        for (int i=0; i<this.board.getNumProperties(); i++) {
            Property p = this.board.getProperty(i);
            if (p instanceof PropertyStreet || p instanceof PropertyRailroad) customizableProperties.add(i);
        }
        return customizableProperties;
    }

    /**
     * Adds the dice and Monopoly labels to the JPanel
     */
    private void addLabels() {
        //Adds dice logos to board
        JLabel logo = new JLabel();
        logo.setIcon(new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("images/logo.png"))));

        logo.setBounds(PANEL_WIDTH /5, PANEL_HEIGHT /5, 395, 395);
        this.add(logo);

        //Sets bounds and adds dice Jlabels
        this.dice1.setBounds(PANEL_WIDTH /2+100, PANEL_HEIGHT /2+100, 55, 55);
        this.add(this.dice1);

        this.dice2.setBounds(PANEL_WIDTH /2+150, PANEL_WIDTH /2+150, 55, 55);
        this.add(this.dice2);
    }

    /**
     * Moves the Player icons position on the board.
     *
     * @param player the player object being moved.
     */
    public void updatePlayerLabelPosition(Player player) {
        JLabel playerLabel = this.playerLabelMap.get(player.getIdentifier());
        if (playerLabel == null) {
            throw new IllegalArgumentException("The Player object that was passed does not correspond with a player label!");
        }
        else {
            int xPos = spaces[player.getPosition()].getX();
            int yPos = spaces[player.getPosition()].getY();
            playerLabel.setBounds(xPos, yPos, playerLabel.getPreferredSize().width, playerLabel.getPreferredSize().height);
        }
    }

    /**
     * Adds player label to Map of playerLabels.
     *
     * @param player the Player object getting added.
     */
    private void addPlayerLabel(Player player) {
        if (player != null) {
            JLabel playerLabel = new JLabel();
            playerLabel.setIcon(new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource(player.getPlayerImageFile()))));
            this.playerLabelMap.put(player.getIdentifier(), playerLabel);
            this.add(playerLabel);
            this.setComponentZOrder(playerLabel, 0);
            updatePlayerLabelPosition(player);
        }
        else {
            throw new IllegalArgumentException("Cannot pass null Player!");
        }
    }

    /**
     * Removes player label from list of playerLabels.
     *
     * @param player the Player object getting removed.
     */
    public void removePlayerLabel(Player player) {
        JLabel playerLabel = this.playerLabelMap.remove(player.getIdentifier());
        if (playerLabel != null) {
            this.remove(playerLabel);
            this.repaint();
        }
        else {
            throw new IllegalArgumentException("The Player object that was passed does not correspond with a player label!");
        }
    }

    /**
     * Updates the dice Jlabel image icon.
     *
     * @param d1 int roll of dice 1
     * @param d2 int roll of dice 2
     */
    public void updateDice(int d1, int d2) {
        this.dice1.setIcon(new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("images/" + d1 + ".png"))));
        this.dice2.setIcon(new ImageIcon(Objects.requireNonNull(this.getClass().getClassLoader().getResource("images/" + d2 + ".png"))));
    }

    /**
     * Adds a house/hotel image on a property
     * @param property The property where the image is being added
     */
    public void addHouse(PropertyStreet property) {
        int index = this.board.getPropertyIndex(property);
        int numHouses = property.getNumHouses();
        int x = spaces[index].getX();
        int y = spaces[index].getY();
        double rotationAngle;

        if (index <= 10) rotationAngle = 0;
        else if (index<20) rotationAngle = Math.PI/2;
        else if (index <= 30) rotationAngle = Math.PI;
        else rotationAngle = 1.5*Math.PI;

        try {
            BufferedImage houseImage = rotateImage(ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResource(String.format("images/house%s.png", numHouses)))), rotationAngle);
            if (houses[index] == null) {
                houses[index] = new JLabel();
                houses[index].setIcon(new ImageIcon(houseImage));
                houses[index].setBounds(x, y, houses[index].getPreferredSize().width, houses[index].getPreferredSize().height);
                this.add(houses[index]);
                this.setComponentZOrder(houses[index], 4); //makes sure houses are above spaces but below player labels
                this.repaint();
            }
            else {
                houses[index].setIcon(new ImageIcon(houseImage));
            }
        }
        catch (IOException e) {
            System.err.println("Could not read file!");
            System.exit(1);
        }

    }

    /**
     * @param image The image to be rotated
     * @param rotationAngle The angle of rotation
     * @return A rotated version of the image
     */
    private BufferedImage rotateImage(BufferedImage image, double rotationAngle) {
        AffineTransform transform = new AffineTransform();
        transform.rotate(rotationAngle, image.getWidth() / 2.0f, image.getHeight() / 2.0f);
        AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return transformOp.filter(image, null);
    }

    /**
     * Updates the property names if customization is used
     * @param index The index of the property to update
     */
    public void updatePropertyName(int index) {
        Property p = this.board.getProperty(index);
        double angle = 0;
        int offset = (p instanceof PropertyRailroad) ? 10 : 25;
        if (index > 10 && index < 20)  {
            angle = Math.PI/2;
            offset = (p instanceof PropertyRailroad) ? PANEL_WIDTH/11 - 10 : PANEL_WIDTH/11 - 25;
        }
        else if (index >= 20 && index <= 30)  {
            angle = Math.PI;
            offset = (p instanceof PropertyRailroad) ? PANEL_HEIGHT/11 - 10 : PANEL_HEIGHT/11 - 25;

        }
        else if (index > 30) {
            angle = 1.5 * Math.PI;
        }
        this.nameLabels[index].setIcon(new PropertyNameIcon(p.getName(), angle, offset));
    }

}
