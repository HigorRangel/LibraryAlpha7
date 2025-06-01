package swing.controller;

import swing.model.AbstractModel;
import swing.service.AbstractService;

import java.awt.*;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Controller base para todas as classes de controller que extender.
 *
 * @param <T>
 */
public class AbstractController<T extends Component, M extends AbstractModel> implements IAbstractController<M> {

    protected final AbstractService<M> service;
    protected Class<M> clazz;
    protected T view;


    @SuppressWarnings("unchecked")
    protected AbstractController(T view, AbstractService<M> service) {
        this.clazz = (Class<M>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.service = service;
        this.view = view;
    }

    @Override
    public M insert(M entity) {
        return this.service.insert(entity);
    }

    @Override
    public M update(M entity) {
        return this.service.update(entity);
    }

    @Override
    public void delete(M entity) {
        this.service.delete(entity);
    }

    @Override
    public List<M> findAll() {
        return this.service.findAll();
    }

    @Override
    public M findById(Long id) {
        return this.service.findById(id);
    }

    @Override
    public List<M> findByIds(List<Long> ids) {
        return this.service.findByIds(ids);
    }
}
