package swing.converters;

import swing.interfaces.CSVConverter;
import swing.model.Author;
import swing.service.AuthorService;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Conversor para converter uma ‘string’ de autores separados por vírgula em uma lista de objetos Autor.
 */
public class ConverterListAuthors implements CSVConverter<Set<Author>> {
    /**
     * Converte uma ‘string’ de autores separados por ponto e vírgula num conjunto de objetos Author.
     *
     * @param valor a string contendo os nomes dos autores separados por ponto e vírgula.
     * @return um conjunto de objetos Author correspondentes aos nomes fornecidos.
     */
    @Override
    public Set<Author> converter(String valor) {
        if (valor == null || valor.trim().isEmpty()) return new LinkedHashSet<>();

        AuthorService authorService = new AuthorService();

        return Arrays.stream(valor.split(";"))
                .map(String::trim)
                .map(nome -> {
                    Author author = authorService.findByName(nome);
                    if (author == null) {
                        author = new Author();
                        author.setCommercialName(nome);
                        author.setFullName(nome);
                    }
                    return author;
                })
                .collect(Collectors.toSet());
    }

}
