package swing.service;

import swing.model.AbstractModel;
import swing.util.HibernateExecutor;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe abstrata que implementa os métodos básicos de um serviço.
 *
 * @param <T>
 */
public abstract class AbstractService<T extends AbstractModel> implements IAbstractService<T> {

    final Class<T> clazz;

    @SuppressWarnings("unchecked")
    protected AbstractService() {
        this.clazz = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }


    @Override
    public List<T> findAll() {
        beforeFindAll();
        List<T> results = HibernateExecutor.executeTransaction(session ->
                session.createQuery(String.format("FROM %s", clazz.getSimpleName()), clazz).list()
        );
        afterFindAll(results);
        return results;
    }

    @Override
    public T findById(Long id) {
        beforeFindById(id);
        T result = HibernateExecutor.executeTransaction(session -> session.get(clazz, id));
        afterFindById(result);
        return result;
    }

    @Override
    public List<T> findByIds(List<Long> ids) {
        List<T> results = new ArrayList<>();
        if (ids == null || ids.isEmpty()) {
            return results;
        }
        for (Long id : ids) {
            T entity = findById(id);
            if (entity != null) {
                results.add(entity);
            }
        }
        return results;
    }

    @Override
    public T insert(T entity) {
        beforeInsert(entity);
        beforeInsert(entity);
        HibernateExecutor.executeTransaction(session -> {
            session.persist(entity);
            return entity;
        });
        afterInsert(entity);
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T update(T entity) {
        return HibernateExecutor.executeTransaction(session -> {
            T persisted = (T) session.merge(entity);
            beforeUpdate(persisted, findById(entity.getId()));
            afterUpdate(persisted);
            return persisted;
        });
    }

    @Override
    public void delete(T entity) {
        beforeDelete(entity);
        HibernateExecutor.executeTransaction(session -> {
            session.remove(entity);
            return entity;
        });
        afterDelete(entity);
    }

    @Override
    public void beforeInsert(T entity) {
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(new Date().toInstant());
        }
        if (entity.getLastModifiedAt() == null) {
            entity.setLastModifiedAt(new Date().toInstant());
        }
        if (entity.getStatus() == null) {
            entity.setStatus(swing.enums.Status.ACTIVE);
        }
    }


    @Override
    public void afterInsert(T entity) {

    }

    @Override
    public void beforeUpdate(T newEntity, T oldEntity) {

    }

    @Override
    public void afterUpdate(T entity) {

    }

    @Override
    public void beforeDelete(T entity) {

    }

    @Override
    public void afterDelete(T entity) {

    }

    @Override
    public void beforeFindById(Long id) {

    }

    @Override
    public void afterFindById(T result) {

    }

    @Override
    public void beforeFindAll() {

    }

    @Override
    public void afterFindAll(List<T> result) {
    }
}
