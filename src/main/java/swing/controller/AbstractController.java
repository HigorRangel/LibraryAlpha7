package swing.controller;

import swing.service.LivroService;

import java.awt.*;

public abstract class AbstractController<T extends Component> implements IAbstractController {

    protected T view;

    LivroService livroService = new LivroService();


    public AbstractController(T view) {
        this.view = view;
    }

    public AbstractController(){

    }

}
