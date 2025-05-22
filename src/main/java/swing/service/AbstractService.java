package swing.service;

import swing.annotations.NomeExibicao;
import swing.model.AbstractModel;
import swing.model.dto.AbstractDTO;
import swing.util.HibernateExecutor;

import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

public abstract class AbstractService<T extends AbstractModel> implements IAbstractService<T> {

    Class<T> clazz;

    @SuppressWarnings("unchecked")
    public AbstractService() {
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
    public T insert(T entity) {
        beforeInsert(entity);
        //TODO - Remover comentários
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        Transaction tx = session.beginTransaction();
//        session.persist(entity);
//        tx.commit();
//        session.close();

//        Transaction tx = null;
//        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
//            tx = session.beginTransaction();
//            session.persist(entity);
//
//            tx.commit();
//        } catch (Exception e) {
//            if (tx != null) tx.rollback();
//            throw e;
//        }

        beforeInsert(entity);
        HibernateExecutor.executeTransaction(session -> {
            session.persist(entity);
            return entity;
        });
        afterInsert(entity);
        return entity;
    }

    @Override
    public T update(T entity) {
        beforeUpdate(entity, findById(entity.getId()));
        HibernateExecutor.executeTransaction(session -> {
            session.merge(entity);
            return entity;
        });
        afterUpdate(entity);
        return entity;
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
        if (entity.getCriadoEm() == null) {
            entity.setCriadoEm(new Date().toInstant());
        }
        if (entity.getUltimaModificacao() == null) {
            entity.setUltimaModificacao(new Date().toInstant());
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


    /**
     * Gets the list of column names from the class fields.
     *
     * @param clazz          the class to inspect
     * @param useDisplayName if true, uses the @NomeExibicao annotation value if present;
     *                       if false, always uses the field name
     * @return a list of column names
     */
    private static List<String> getColumnNames(Class<?> clazz, boolean useDisplayName) {
        List<String> columnNames = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            NomeExibicao nomeExibicao = field.getAnnotation(NomeExibicao.class);
            if(nomeExibicao == null || nomeExibicao.value().isEmpty()){
                continue;
            }

            if (useDisplayName) {
                columnNames.add(nomeExibicao.value());
            } else {
                columnNames.add(field.getName());
            }
        }
        return columnNames;
    }


    /**
     * Converts a list of objects to a 2D array of objects.
     *
     * @param list the list of objects to convert
     * @param <T>  the type of the objects in the list
     * @return a 2D array of objects
     */
    public static <T> DefaultTableModel objectsToTableModel(Class<T> clazz, List<T> list) {
        List<String> columnDisplayNames = getColumnNames(clazz, true);
        List<String> columnNames = getColumnNames(clazz, false);
        Object[][] data = new Object[list.size()][columnDisplayNames.size()];
        for (int i = 0; i < list.size(); i++) {
            T obj = list.get(i);
            for (int j = 0; j < columnNames.size(); j++) {
                try {
                    Field field = obj.getClass().getDeclaredField(columnNames.get(j));
                    field.setAccessible(true);
                    data[i][j] = field.get(obj);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    data[i][j] = null;
                }
            }
        }

        return new DefaultTableModel(data, columnDisplayNames.toArray());
    }

}
