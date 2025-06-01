package swing.components;

import javax.swing.*;
import java.io.File;

/**
 * Utilizado para criar um JFileChooser personalizado.
 * Permite selecionar arquivos de forma mais intuitiva, com título e texto do botão de seleção personalizados.
 */
public class CustomFileChooser extends JFileChooser {
    public CustomFileChooser() {
        super();
        setDialogTitle("Selecione um arquivo");
        setApproveButtonText("Selecionar");
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        setAcceptAllFileFilterUsed(false);
    }

    public CustomFileChooser(String currentDirectoryPath) {
        this();
        setCurrentDirectory(new File(currentDirectoryPath));
    }
}
