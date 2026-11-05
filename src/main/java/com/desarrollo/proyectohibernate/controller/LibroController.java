package com.desarrollo.proyectohibernate.controller;

import com.desarrollo.proyectohibernate.dao.LibroDAO;
import com.desarrollo.proyectohibernate.model.Libro;

import java.util.List;

public class LibroController {

    LibroDAO libroDAO = new LibroDAO();

    public void registrar(Libro libro) {
        libroDAO.guardar(libro);
    }

    public void modificar(Libro libro) {
        libroDAO.actualizar(libro);
    }

    public void eliminar(long id) {
        libroDAO.eliminar(id);
    }

    public List<Libro> listar() {
        return libroDAO.listarTodos();
    }
}
