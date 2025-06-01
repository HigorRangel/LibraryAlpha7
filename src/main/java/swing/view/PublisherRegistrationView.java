package swing.view;

import swing.components.CustomButton;
import swing.components.CustomTextField;
import swing.components.FieldGroup;
import swing.controller.PublisherRegistrationController;
import swing.model.Publisher;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de cadastro/edição de Publisher.
 */
public class PublisherRegistrationView extends JDialog {

    final boolean isEdition;
    private final JPanel mainPanel;
    private final PublisherRegistrationController controller = new PublisherRegistrationController(this);
    Publisher publisher;
    FieldGroup<CustomTextField> fieldGroupId;
    private CustomTextField fieldTxtId;
    private CustomTextField fieldTxtName;

    public PublisherRegistrationView(JFrame parent, Publisher publisher) {
        super(parent, "Cadastro de Editora", true);
        isEdition = publisher != null;

        this.publisher = publisher;
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        loadForm();
        cleanDate();
        drawForm();
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(400, 0));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);

        if (publisher == null) {
            return;
        }
        loadFormData(publisher);

    }


    /**
     * Desenha o formulário de cadastro/edição de Publisher.
     */
    private void drawForm() {
        JPanel formPanel = getFormPanel();

        // Botões
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Botão de salvar
        JButton btnSave = new CustomButton("Salvar");
        btnSave.addActionListener(e -> {
            if (publisher == null) {
                publisher = new Publisher();
            }
            boolean successfully = controller.savePublisher(getId(), getPublisherName());
            if (successfully) {
                dispose();
            }
        });

        // Botão de excluir
        JButton btnDelete = new CustomButton("Excluir");
        btnDelete.addActionListener(e -> deleteRecord());

        // Botão de cancelar
        JButton btnCancel = new CustomButton("Cancelar");
        btnCancel.addActionListener(e -> {
            cleanDate();
            dispose();
        });

        buttonsPanel.add(btnSave);

        if (isEdition) {
            buttonsPanel.add(btnDelete);
        }

        buttonsPanel.add(btnCancel);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
    }

    /**
     * Retorna o nome comercial da editora.
     *
     * @return Nome comercial da editora.
     */
    private String getPublisherName() {
        return (this.fieldTxtName != null) ? fieldTxtName.getText() : "";
    }


    /**
     * Exclui o registro da editora.
     */
    private void deleteRecord() {
        boolean response = controller.deleteRecord(this.publisher);
        if (response) {
            dispose();
        }
    }

    /**
     * Cria o painel do formulário de cadastro/edição de Publisher.
     * @return JPanel contendo os campos do formulário.
     */
    private JPanel getFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fieldGroupId = new FieldGroup<>(fieldTxtId, "ID");
        formPanel.add(fieldGroupId);

        if (publisher != null) {
            fieldTxtId.setText(String.valueOf(publisher.getId()));
            fieldTxtId.setEnabled(false);
            fieldGroupId.setVisible(true);
        } else {
            fieldTxtId.setEnabled(false);
            fieldGroupId.setVisible(false);
        }

        formPanel.add(fieldGroupId);

        FieldGroup<CustomTextField> fieldGroupCommercialName = new FieldGroup<>(fieldTxtName, "Nome");
        formPanel.add(fieldGroupCommercialName);

        return formPanel;
    }


    /**
     * Carrega os componentes do formulário.
     */
    private void loadForm() {
        fieldTxtId = new CustomTextField();
        fieldTxtId.setEnabled(false);

        fieldTxtId.setHorizontalAlignment(SwingConstants.CENTER);


        fieldTxtName = new CustomTextField();
        fieldTxtName.setRequired(true);
        fieldTxtName.setMaxLength(100);
    }

    /**
     * Preenche os fields do formulário com os dados da editoraDTO.
     *
     * @param publisher Objeta editoraDTO contendo os dados da editora.
     */
    private void loadFormData(Publisher publisher) {
        fieldTxtName.setText(publisher.getName());
        fieldTxtId.setText(String.valueOf(publisher.getId()));
    }


    /**
     * Limpa os campos do formulário.
     */
    private void cleanDate() {
        fieldTxtId.setText("");
        fieldTxtName.setText("");

        fieldTxtId.setEnabled(false);
        if (fieldGroupId != null) {
            fieldGroupId.setVisible(false);
        }
    }


    /**
     * Retorna o ID da editora.
     *
     * @return ID da editora.
     */
    public String getId() {
        return fieldTxtId.getText();
    }


}
