import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for all the property name icons in the GUI
 */
public class PropertyNameIcon implements Icon, Serializable {
    private int width;
    private int height;
    private String iconText;
    private double angle;
    private int offset;

    /**
     * Constructor
     * @param text The text to be displayed in this icon
     * @param angle The angle to rotate this icon
     * @param offset The offset to move this icon
     */
    public PropertyNameIcon(String text, double angle, int offset) {
        this.iconText = text;
        this.width = BoardPanel.PANEL_WIDTH/11;
        this.height = BoardPanel.PANEL_HEIGHT/11;
        this.angle = angle;
        this.offset = offset;
    }

    /**
     * Paints the icon
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Font font = new Font("SansSerif", Font.PLAIN, 10);
        int coord = offset;
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics();
        int fontHeight =  metrics.getHeight();
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(angle, 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g.setFont(rotatedFont);

        //slightly adjust the font height
        int fontAdjustment = height/21;
        if (angle == Math.PI || angle == Math.PI/2) fontHeight = (fontHeight * -1) + fontAdjustment;
        else fontHeight -= fontAdjustment;

        int numChars = width/metrics.charWidth('A') + 2; //get the number of characters that will be painted on each line

        ArrayList<String> words = new ArrayList<>(Arrays.asList(iconText.split(" ")));
        for (int i=0; i<words.size(); i++) {
            String line;
            if (i<words.size()-1 && (words.get(i)+words.get(i+1)).length() < numChars) {
                line = words.get(i)+ " " + words.get(i+1);
                i++;
            }
            else if (words.get(i).length() <= numChars) {
                line = words.get(i);
            }
            else {
                line = words.get(i).substring(0, numChars) + "-";
                String restOfWord = words.get(i).substring(numChars);
                words.add(i+1, restOfWord);
            }
            double lineIndent;
            if (angle == 0 || angle == Math.PI/2) lineIndent = (width-metrics.stringWidth(line)) / 2.0f;
            else lineIndent = width - ((width-metrics.stringWidth(line)) / 2.0f);
            if (angle == 0 || angle==Math.PI) g.drawString(line, (int) lineIndent, coord);
            else g.drawString(line, coord, (int) lineIndent);
            coord += fontHeight;
        }
    }

    /**
     * @return The width of the icon
     */
    @Override
    public int getIconWidth() {
        return width;
    }

    /**
     * @return The height of the icon
     */
    @Override
    public int getIconHeight() {
        return height;
    }
}
