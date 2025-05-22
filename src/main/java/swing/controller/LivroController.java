package swing.controller;

import swing.model.Livro;
import swing.model.dto.LivroDTO;
import swing.service.LivroService;
import swing.view.LivroView;
import swing.view.MainView;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class LivroController extends AbstractController<LivroView> {

    private final LivroService livroService = new LivroService();

    public LivroController(LivroView livroView) {
        super(livroView);
    }

    public void carregarLivros() {
        List<Livro> livros = livroService.findAll();
        List<LivroDTO> livrosDTO = livros.stream()
                .map(LivroDTO::new)
                .collect(Collectors.toList());

        DefaultTableModel dataModel = LivroService.objectsToTableModel(LivroDTO.class, livrosDTO);
        view.atualizarTabelaLivros(dataModel);
    }
}
