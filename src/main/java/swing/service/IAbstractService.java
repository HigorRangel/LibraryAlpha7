package swing.service;

import swing.model.AbstractModel;

import java.util.List;

public interface IAbstractService<T extends AbstractModel> {

    /**
     * Buscar todos os registros
     *
     * @return Conjunto de registros encontrados.
     */
    List<T> findAll();


    /**
     * Buscar registro por ID
     *
     * @param id ‘ID’ do registro a ser buscado.
     * @return Registro encontrado.
     */
    T findById(Long id);


    /**
     * Inserir novo registro
     *
     * @param entity Registro a ser inserido.
     * @return Registro inserido.
     */
    T insert(T entity);

    /**
     * Atualizar registro
     *
     * @param entity Registro a ser atualizado.
     * @return Registro atualizado.
     */
    T update(T entity);

    /**
     * Deletar registro
     *
     * @param entity Registro a ser deletado.
     */
    void delete(T entity);


    /**
     * Executa ações antes de inserir um registro.
     *
     * @param entity a entidade a ser inserida
     */
    void beforeInsert(T entity);


    /**
     * Executa ações após inserir um registro.
     *
     * @param entity a entidade inserida
     */
    void afterInsert(T entity);

    /**
     * Executa ações antes de atualizar um registro.
     *
     * @param newEntity a nova entidade
     * @param oldEntity a entidade antiga
     */
    void beforeUpdate(T newEntity, T oldEntity);

    /**
     * Executa ações após atualizar um registro.
     *
     * @param entity a entidade atualizada
     */
    void afterUpdate(T entity);


    /**
     * Executa ações antes de deletar um registro.
     *
     * @param entity a entidade a ser deletada
     */
    void beforeDelete(T entity);

    /**
     * Executa ações após deletar um registro.
     *
     * @param entity a entidade deletada
     */
    void afterDelete(T entity);


    /**
     * Executa ações antes de buscar um registro por ID.
     *
     * @param id o ID do registro a ser buscado
     */
    void beforeFindById(Long id);

    /**
     * Executa ações após buscar um registro por ID.
     *
     * @param result o registro encontrado
     */
    void afterFindById(T result);

    /**
     * Executa ações antes de buscar todos os registros.
     */
    void beforeFindAll();

    /**
     * Executa ações após buscar todos os registros.
     *
     * @param result o conjunto de registros encontrados
     */
    void afterFindAll(List<T> result);

    /**
     * Buscar registros por IDs.
     *
     * @param ids Conjunto de ‘IDs’ dos registros a serem buscados.
     * @return Conjunto de registros encontrados.
     */
    List<T> findByIds(List<Long> ids);

}
