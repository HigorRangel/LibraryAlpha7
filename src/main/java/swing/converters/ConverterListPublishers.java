package swing.converters;

import swing.interfaces.CSVConverter;
import swing.model.Publisher;
import swing.service.PublisherService;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Conversor para converter uma ‘string’ de editoras separados por vírgula numa lista de objetos Editora.
 */
public class ConverterListPublishers implements CSVConverter<Set<Publisher>> {

    /**
     * Converte uma string de editoras separadas por ponto e vírgula em um conjunto de objetos Publisher.
     *
     * @param value a string contendo os nomes das editoras separados por ponto e vírgula.
     * @return um conjunto de objetos Publisher correspondentes aos nomes fornecidos.
     */
    @Override
    public Set<Publisher> converter(String value) {
        if (value == null || value.trim().isEmpty()) return new LinkedHashSet<>();

        PublisherService publisherService = new PublisherService();

        return Arrays.stream(value.split(";"))
                .map(String::trim)
                .map(nome -> {
                    Publisher publisher = publisherService.findByName(nome);
                    if (publisher == null) {
                        publisher = new Publisher();
                        publisher.setName(nome);
                    }
                    return publisher;
                })
                .collect(Collectors.toSet());
    }

}
