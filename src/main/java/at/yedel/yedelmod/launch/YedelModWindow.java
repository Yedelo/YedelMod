package at.yedel.yedelmod.launch;



import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;



public class YedelModWindow {
    public static void main(String[] args) throws IOException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        JFrame mainFrame = new JFrame();
        int option = JOptionPane.showOptionDialog(
            mainFrame,
                "You launched YedelMod as a jar file! Do you want to copy the mod file?",
            "YedelMod " + YedelModConstants.MOD_VERSION,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                new String[] {"Select Directory", "Close"},
                "Select Directory"
        );
        if (option == JOptionPane.YES_OPTION) {
            File thisFile =
                new File(YedelModWindow.class.getProtectionDomain().getCodeSource().getLocation().getFile());
            JFileChooser fileSelector = new JFileChooser();
            fileSelector.setDialogTitle("Choose a directory...");
            fileSelector.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileSelector.setFileFilter(new FileNameExtensionFilter("JAR files (.jar)", "jar"));
            fileSelector.setSelectedFile(new File(thisFile.getName()));
            fileSelector.showSaveDialog(null);
            Path selectedPath = fileSelector.getSelectedFile().toPath();
            Files.copy(thisFile.toPath(), selectedPath, StandardCopyOption.REPLACE_EXISTING);
        }
        System.exit(0);
    }
}
