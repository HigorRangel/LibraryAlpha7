package swing.view;

import swing.components.CustomButton;
import swing.components.CustomFileChooser;
import swing.components.CustomTextArea;
import swing.components.CustomTextField;
import swing.controller.ImportBookController;
import swing.enums.LogErrorType;
import swing.util.AsyncExecutor;
import swing.util.DateUtils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;

/**
 * Classe responsável por exibir a tela de importação de livros.
 * Esta tela permite ao usuário selecionar um arquivo CSV contendo informações de livros
 */
public class ImportBooksView extends JDialog {

    final ImportBookController controller = new ImportBookController(this);
    private final JPanel mainPanel;
    private CustomFileChooser fileChooser;
    private CustomTextArea areaLog;


    public ImportBooksView(JFrame parent) {
        super(parent, "Importar arquivos", true);

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loadForm();
        loadAreaLog();
        cleanData();
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(700, 400));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);

        JTextArea logArea = new CustomTextArea();
        logArea.setEditable(false);
    }

    /**
     * Método para carregar a área de log onde as mensagens de importação serão exibidas.
     */
    private void loadAreaLog() {
        areaLog = new CustomTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setFont(new Font("Monospaced", Font.PLAIN, 11));

        JScrollPane scrollPane = new JScrollPane(areaLog);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Método para limpar os dados do formulário, incluindo o seletor de arquivos e a área de log.
     */
    private void cleanData() {
        if (fileChooser != null) {
            fileChooser.setSelectedFile(null);
        }
        if (areaLog != null) {
            areaLog.setText("");
        }
    }

    /**
     * Método para iniciar o formulário.
     */
    private void loadForm() {
        CustomButton selectButton = new CustomButton("...");

        selectButton.setPreferredSize(new Dimension(50, 30));

        JPanel painel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        CustomTextField fieldFile = new CustomTextField();
        fieldFile.setEditable(false);
        fieldFile.setEnabled(false);

        selectButton.addActionListener(e -> {
            JFileChooser selector = new JFileChooser();
            selector.setDialogTitle("Escolha um arquivo");
            selector.setFileSelectionMode(JFileChooser.FILES_ONLY);
            selector.setMultiSelectionEnabled(false);
            selector.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

            int result = selector.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = selector.getSelectedFile();
                fieldFile.setText(file.getAbsolutePath());

                AsyncExecutor.runAsync(
                        () -> controller.importBooks(file),
                        () -> addLog(null, "✅ Importação finalizada.")
                );
            }
        });

        // Campo de texto - 90%
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        painel.add(fieldFile, gbc);

        // Botão - 10%
        gbc.gridx = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(selectButton, gbc);

        mainPanel.add(painel, BorderLayout.NORTH);
    }

    /**
     * Método para adicionar uma message de error ao log.
     *
     * @param message a message de error a ser adicionada ao ‘log’.
     */
    public void addLog(LogErrorType logErrorType, String message) {
        String finalMessage;
        if (logErrorType != null) {
            String formattedPublicationDate = DateUtils.dateToText(LocalDateTime.now(), false);
            String errorType = logErrorType.getDescription();
            finalMessage = String.format("[%s] [%s]: %s", formattedPublicationDate, errorType, message);
        } else {
            finalMessage = message;
        }

        if (areaLog != null) {
            SwingUtilities.invokeLater(() -> {
                areaLog.append(finalMessage + "\n");
                areaLog.append((logErrorType == null ? "" : "---------------------------------------------------\n"));
                areaLog.setCaretPosition(areaLog.getDocument().getLength());
            });
        }
    }

}
