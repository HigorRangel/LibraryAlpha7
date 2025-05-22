package swing;

import org.hibernate.SessionFactory;
import swing.model.Autor;
import swing.model.Livro;
import swing.model.dto.LivroDTO;
import swing.service.AutorService;
import swing.service.LivroService;
import swing.util.HibernateUtils;
import swing.view.MainView;

import javax.swing.*;
import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();

        //insertInitialData();

        LivroService livroService = new LivroService();
        List<Livro> livros = livroService.findAll();
        livros.forEach(livro -> {
            LivroDTO livroDTO = new LivroDTO(livro);
            System.out.println("Livro: " + livroDTO.getTitulo());
        });


        SwingUtilities.invokeLater(MainView::new);
    }

    private static void insertInitialData() {
        Autor autor1 = new Autor();
        autor1.setNomeCompleto("John Ronald Reuel Tolkien");
        autor1.setNomeComercial( "J.R.R. Tolkien");
        autor1.setPrincipalGenero( "Fantasia");

        Autor autor2 = new Autor();
        autor2.setNomeComercial( "J.K. Rowling");
        autor2.setNomeCompleto( "Joanne Rowling");
        autor2.setPrincipalGenero( "Fantasia");

        Autor autor3 = new Autor();
        autor3.setNomeComercial( "George R.R. Martin");
        autor3.setNomeCompleto( "George Raymond Richard Martin");
        autor3.setPrincipalGenero( "Fantasia");

        AutorService autorService = new AutorService();
        autorService.insert(autor1);
        autorService.insert(autor2);
        autorService.insert(autor3);

        Livro livro1 = new Livro();
        livro1.setTitulo("O Senhor dos Anéis");
        livro1.setEditora( "HarperCollins");
        livro1.setISBN( "978-0-261-10221-7");
        livro1.setDataPublicacao(LocalDate.of(1954, 7, 29));
        livro1.setAutores(new ArrayList<>(Collections.singletonList(autor1)));

        Livro livro2 = new Livro();
        livro2.setTitulo("Harry Potter e a Pedra Filosofal");
        livro2.setEditora( "Bloomsbury");
        livro2.setISBN( "978-0-7475-3271-9");
        livro2.setDataPublicacao(LocalDate.of(1997, 6, 26));
        livro2.setAutores(new ArrayList<>(Collections.singletonList(autor2)));

        Livro livro3 = new Livro();
        livro3.setTitulo("O Hobbit");
        livro3.setEditora( "HarperCollins");
        livro3.setISBN( "978-0-261-10221-7");
        livro3.setDataPublicacao(LocalDate.of(1937, 9, 21));
        livro3.setAutores(new ArrayList<>(Collections.singletonList(autor1)));


        LivroService livroService = new LivroService();
        livroService.insert(livro1);
        livroService.insert(livro2);
        livroService.insert(livro3);
    }
}