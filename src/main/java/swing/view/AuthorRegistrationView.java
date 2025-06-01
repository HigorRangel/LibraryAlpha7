package swing.view;

import swing.components.CustomButton;
import swing.components.CustomTextField;
import swing.components.FieldGroup;
import swing.controller.AuthorRegistrationController;
import swing.model.Author;

import javax.swing.*;
import java.awt.*;

/**
 * Tela de cadastro/edição de Author.
 */
public class AuthorRegistrationView extends JDialog {

    final boolean isEdition;
    private final JPanel mainPanel;
    private final AuthorRegistrationController controller = new AuthorRegistrationController(this);
    Author author;
    FieldGroup<CustomTextField> fieldGroupId;
    private CustomTextField fieldTxtId;
    private CustomTextField fieldTxtCommercialName;
    private CustomTextField fieldTxtFullName;
    private CustomTextField fieldTxtMainGenre;

    public AuthorRegistrationView(JFrame parent) {
        this(parent, null);
    }

    public AuthorRegistrationView(JFrame parent, Author author) {
        super(parent, "Cadastro de Autor", true);
        isEdition = author != null;

        this.author = author;
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

        if (author == null) {
            return;
        }
        loadFormData(author);

    }


    /**
     *  Desenha o formulário de cadastro/edição de Author.
     */
    private void drawForm() {
        JPanel formPanel = getFormPanel();

        // Botões
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Botão de salvar
        JButton btnSave = new CustomButton("Salvar");
        btnSave.addActionListener(e -> {
            if (author == null) {
                author = new Author();
            }
            boolean successfully = controller.saveAuthor(getId(), getCommercialName(), getFullName(), getMainGenre());
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
     * Retorna o nome comercial do autor.
     *
     * @return Nome comercial do autor.
     */
    private String getCommercialName() {
        return (this.fieldTxtCommercialName != null) ? fieldTxtCommercialName.getText() : "";
    }


    /**
     * Retorna o nome completo do autor.
     *
     * @return Nome completo do autor.
     */
    private String getFullName() {
        return (this.fieldTxtFullName != null) ? fieldTxtFullName.getText() : "";
    }

    /**
     * Retorna o gênero principal do autor.
     *
     * @return Gênero principal do autor.
     */
    private String getMainGenre() {
        return (this.fieldTxtMainGenre != null) ? fieldTxtMainGenre.getText() : "";
    }

    /**
     * Exclui o registro do livro.
     */
    private void deleteRecord() {
        boolean response = controller.deleteRecord(this.author);
        if (response) {
            dispose();
        }
    }

    /**
     * Cria e retorna o painel do formulário.
     * @return JPanel contendo os campos do formulário.
     */
    private JPanel getFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        fieldGroupId = new FieldGroup<>(fieldTxtId, "ID");
        formPanel.add(fieldGroupId);

        if (author != null) {
            fieldTxtId.setText(String.valueOf(author.getId()));
            fieldTxtId.setEnabled(false);
            fieldGroupId.setVisible(true);

        } else {
            fieldTxtId.setEnabled(false);
            fieldGroupId.setVisible(false);
        }

        formPanel.add(fieldGroupId);

        FieldGroup<CustomTextField> fieldGroupCommercialName = new FieldGroup<>(fieldTxtCommercialName, "Nome Comercial");
        formPanel.add(fieldGroupCommercialName);

        FieldGroup<CustomTextField> fieldGroupFullName = new FieldGroup<>(fieldTxtFullName, "Nome Completo");
        formPanel.add(fieldGroupFullName);

        FieldGroup<CustomTextField> fieldGroupMainGenre = new FieldGroup<>(fieldTxtMainGenre, "Gênero Principal");
        formPanel.add(fieldGroupMainGenre);

        return formPanel;
    }


    /**
     * Carrega os componentes do formulário.
     */
    private void loadForm() {
        fieldTxtId = new CustomTextField();
        fieldTxtId.setEnabled(false);

        fieldTxtId.setHorizontalAlignment(SwingConstants.CENTER);


        fieldTxtCommercialName = new CustomTextField();
        fieldTxtCommercialName.setRequired(true);
        fieldTxtCommercialName.setMaxLength(100);

        fieldTxtFullName = new CustomTextField();
        fieldTxtFullName.setRequired(true);
        fieldTxtFullName.setMaxLength(100);

        fieldTxtMainGenre = new CustomTextField();
        fieldTxtMainGenre.setRequired(true);
        fieldTxtMainGenre.setMaxLength(50);
    }

    /**
     * Preenche os fields do formulário com os dados do LivroDTO.
     *
     * @param author Objeto LivroDTO contendo os dados do livro.
     */
    private void loadFormData(Author author) {
        fieldTxtCommercialName.setText(author.getCommercialName());
        fieldTxtFullName.setText(author.getFullName());
        fieldTxtMainGenre.setText(author.getMainGenre());
        fieldTxtId.setText(String.valueOf(author.getId()));
    }


    /**
     * Limpa os campos do formulário.
     */
    private void cleanDate() {
        fieldTxtId.setText("");
        fieldTxtCommercialName.setText("");

        fieldTxtId.setEnabled(false);
        if (fieldGroupId != null) {
            fieldGroupId.setVisible(false);
        }
    }


    /**
     * Retorna o ‘ID’ do livro.
     *
     * @return ‘ID’ do livro.
     */
    public String getId() {
        return fieldTxtId.getText();
    }


}
