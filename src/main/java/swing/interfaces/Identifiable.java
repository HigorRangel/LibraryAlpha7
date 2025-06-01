package swing.interfaces;

import swing.enums.Status;

import java.time.Instant;

/**
 * ‘Interface’ que define os métodos para manipulação de entidades identificáveis.
 * Utilizada para Model e DTO que precisam de um identificador único, status e informações de data.
 */
public interface Identifiable {

    /**
     * Returns obtém o identificador único da entidade.
     *
     * @return o identificador único da entidade.
     */
    Long getId();

    /**
     * Define o identificador único da entidade.
     *
     * @param id o identificador único a ser definido.
     */
    void setId(Long id);

    /**
     * Obtém o status da entidade.
     *
     * @return o status da entidade.
     */
    Status getStatus();

    /**
     * Define o status da entidade.
     *
     * @param status o status a ser definido.
     */
    void setStatus(Status status);


    /**
     * Obtém a data e hora da última modificação da entidade.
     *
     * @return a data e hora da última modificação.
     */
    Instant getLastModifiedAt();

    /**
     * Define a data e hora da última modificação da entidade.
     *
     * @param ultimaModificacao a data e hora da última modificação a ser definida.
     */
    void setLastModifiedAt(Instant ultimaModificacao);

    /**
     * Obtém a data e hora em que a entidade foi criada.
     *
     * @return a data e hora em que a entidade foi criada.
     */
    Instant getCreatedAt();

    /**
     * Define a data e hora em que a entidade foi criada.
     *
     * @param createdAt a data e hora em que a entidade foi criada.
     */
    void setCreatedAt(Instant createdAt);

    /**
     * Obtém o nome de exibição da entidade para uso em campo de multisseleção ou tabelas.
     *
     * @return o nome de exibição da entidade.
     */
    String getDisplayName();


    /**
     * Obtém a descrição da entidade para uso em tabelas ou exibições detalhadas.
     *
     * @return a descrição da entidade.
     */
    String getDescription();

}
