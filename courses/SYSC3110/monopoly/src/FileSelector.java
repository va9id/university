import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileSelector implements Runnable {
    static final int OPEN = 0;
    static final int SAVE = 1;
    private JFileChooser chooser;
    private int returnVal;
    private int option;

    public FileSelector(int option, FileNameExtensionFilter filter) {
        this.option = option;
        chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setFileFilter(filter);
    }

    @Override
    public void run() {
        if (option == OPEN) returnVal = chooser.showOpenDialog(null);
        else if (option == SAVE) returnVal = chooser.showSaveDialog(null);
    }

    public boolean fileSelected() {
        return returnVal == JFileChooser.APPROVE_OPTION;
    }

    public String getAbsoluteFilePath() {
        if (fileSelected()) return chooser.getSelectedFile().getAbsolutePath();
        return null;
    }
}
