package com.desarrollo.proyectohibernate.controller;

import com.desarrollo.proyectohibernate.dao.LibroDAO;
import com.desarrollo.proyectohibernate.model.Libro;

import java.util.List;

public class LibroController {

    LibroDAO libroDAO = new LibroDAO();

    public void registrar(Libro libro) {
        libroDAO.guardar(libro);
    }

    public boolean modificar(Libro libro) {
        return libroDAO.modificar(libro);
    }

    public boolean eliminar(long id) {
        return libroDAO.eliminar(id);
    }

    public List<Libro> listar() {
        return libroDAO.listarTodos();
    }
}
