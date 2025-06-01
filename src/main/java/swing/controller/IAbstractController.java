package swing.controller;

import swing.model.AbstractModel;

import java.util.List;

/**
 * Interface para controladores abstratos.
 */
public interface IAbstractController<T extends AbstractModel> {

    /**
     * Insere um novo registro no banco de dados.
     *
     * @param entity o objeto a ser inserido
     * @return o objeto inserido, possivelmente com um ‘ID’ gerado pelo banco de dados
     */
    T insert(T entity);


    /**
     * Atualiza um registro existente no banco de dados.
     *
     * @param entity o objeto a ser atualizado
     * @return o objeto atualizado
     */
    T update(T entity);

    /**
     * Exclui um registro do banco de dados.
     *
     * @param entity o objeto a ser excluído
     */
    void delete(T entity);

    /**
     * Busca todos os registros do banco de dados.
     *
     * @return uma lista com todos os objetos do tipo T
     */
    List<T> findAll();

    /**
     * Busca um registro pelo ‘ID’.
     *
     * @param id o ID do objeto a ser buscado
     * @return o objeto encontrado ou null se não houver nenhum objeto com o ID especificado
     */
    T findById(Long id);

    /**
     * Busca registros por uma lista de ‘IDs’.
     *
     * @param ids a lista de ‘IDs’ dos objetos a serem buscados
     * @return um conjunto com os objetos encontrados
     */
    List<T> findByIds(List<Long> ids);

}
