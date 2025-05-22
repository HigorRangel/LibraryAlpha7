package swing.view;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import swing.components.CustomButton;
import swing.components.CustomTextField;
import swing.controller.LivroController;
import swing.model.Autor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Locale;

public class LivroView extends JPanel {

    private JTable table;
    private LivroController livroController = new LivroController(this);


    public LivroView() {
        initComponents();
    }

    private void initComponents() {

        setLayout(new BorderLayout());

        this.setPreferredSize(new Dimension(800, 600));
        this.setMinimumSize(new Dimension(800, 600));
        this.setMaximumSize(new Dimension(800, 600));
        this.setSize(new Dimension(800, 600));
        iniciarFormulario();
        iniciarTabela();
//
//        JTable tabelaLivros = new JTable();
//        JScrollPane painelScroll = new JScrollPane(tabelaLivros);
//
//
//        JButton btnNovo = new JButton("Novo");
//        JButton btnEditar = new JButton("Editar");
//        JButton btnExcluir = new JButton("Excluir");
//
//        JPanel botoes = new JPanel();
//        botoes.add(btnNovo);
//        botoes.add(btnEditar);
//        botoes.add(btnExcluir);
//
//        add(painelScroll, BorderLayout.CENTER);
//        add(botoes, BorderLayout.SOUTH);
    }

    /**
     * Método para inicializar a tabela de livros
     */
    private void iniciarTabela() {
        table = new JTable();
        livroController.carregarLivros();
    }

    public void atualizarTabelaLivros(DefaultTableModel model) {
        table.setModel(model);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        scrollPane.setPreferredSize(new Dimension(800, 500));
        add(scrollPane);
        revalidate();
        repaint();
    }

    private void iniciarFormulario() {
        JPanel painelFormulario = new JPanel(new GridBagLayout());
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;

        Insets campoInsets = new Insets(5, 5, 5, 5);
        Insets rotuloInsets = new Insets(5, 5, 0, 5);

        //Campo Search
        //Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 12;
        gbc.weightx = 0.0;
        gbc.insets = rotuloInsets;
        painelFormulario.add(new JLabel("Busca"), gbc);

        //Campo
        gbc.gridy = 1;
        gbc.gridwidth = 11;
        gbc.weightx = 1.0;
        CustomTextField campoBusca = new CustomTextField();
        painelFormulario.add(campoBusca, gbc);

        //Botão de pesquisar
        gbc.gridx = 11;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        CustomButton btnPesquisar = new CustomButton("P");
        painelFormulario.add(btnPesquisar, gbc);

        add(painelFormulario, BorderLayout.NORTH);
    }

//    private void iniciarFormulario() {
//        JPanel painelFormulario = new JPanel(new GridBagLayout());
//        painelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.anchor = GridBagConstraints.NORTHWEST;
//        gbc.weightx = 1.0;
////        gbc.insets = new Insets(5, 5, 5, 5);
//
//        Insets campoInsets = new Insets(5, 5, 5, 5);
//        Insets rotuloInsets = new Insets(5, 5, 0, 5);
//
//        //Campo Titulo
//        //Label
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.gridwidth = 6;
//        gbc.insets = rotuloInsets;
//        painelFormulario.add(new JLabel("Título"), gbc);
//
//        //Field
//        gbc.gridy = 1;
//        JTextField campoTitulo = new JTextField();
//        campoTitulo.setEnabled(false);
//        gbc.insets = campoInsets;
//        painelFormulario.add(campoTitulo, gbc);
//
//        //Campo Editora
//        //Label
//        gbc.gridx = 6;
//        gbc.gridy = 0;
//        gbc.gridwidth = 3;
//        gbc.insets = rotuloInsets;
//        painelFormulario.add(new JLabel("Editora"), gbc);
//
//        //Field
//        gbc.gridy = 1;
//        JTextField campoEditora = new JTextField();
//        campoEditora.setEnabled(false);
//        gbc.insets = campoInsets;
//        painelFormulario.add(campoEditora, gbc);
//
//        //Campo ISBN
//        //Label
//        gbc.gridx = 9;
//        gbc.gridy = 0;
//        gbc.gridwidth = 3;
//        gbc.insets = rotuloInsets;
//        painelFormulario.add(new JLabel("ISBN"), gbc);
//
//        //Field
//        gbc.gridy = 1;
//        JTextField campoISBN = new JTextField();
//        campoISBN.setEnabled(false);
//        gbc.insets = campoInsets;
//        painelFormulario.add(campoISBN, gbc);
//
//        //Campo Autores
//        //Label
//        gbc.gridx = 0;
//        gbc.gridy = 2;
//        gbc.gridwidth = 4;
//        gbc.insets = rotuloInsets;
//        painelFormulario.add(new JLabel("Autores"), gbc);
//
//        //Field
//        gbc.gridy = 3;
//        JTextField campoAutores = new JTextField();
//        campoAutores.setEnabled(false);
//        gbc.insets = campoInsets;
//        painelFormulario.add(campoAutores, gbc);
//
//        //Campo Data de Publicação
//        //Label
//        gbc.gridx = 4;
//        gbc.gridy = 2;
//        gbc.gridwidth = 3;
//        gbc.insets = rotuloInsets;
//        painelFormulario.add(new JLabel("Data de Publicação"), gbc);
//
//        //Field
//        gbc.gridy = 3;
////        DatePickerSettings settings = new DatePickerSettings();
////        settings.setFormatForDatesCommonEra("dd/MM/yyyy");
////        setLocale(new Locale("pt", "BR"));
////        DatePicker datePicker = new DatePicker(settings);
////        painelFormulario.add(datePicker, gbc);
//
//        JTextField campoDataPublicacao = new JTextField();
//        campoDataPublicacao.setEnabled(false);
//        painelFormulario.add(campoDataPublicacao, gbc);
//
//        add(painelFormulario, BorderLayout.NORTH);
//
//    }
}
