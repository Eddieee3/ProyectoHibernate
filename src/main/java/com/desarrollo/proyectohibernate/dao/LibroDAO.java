package com.desarrollo.proyectohibernate.dao;

import com.desarrollo.proyectohibernate.database.JPAUtil;
import com.desarrollo.proyectohibernate.model.Libro;
import jakarta.persistence.EntityManager;

import java.util.List;

public class LibroDAO {

    // guardar un libro nuevo
    public void guardar(Libro libro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(libro);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // modificar un libro que ya existe
    public void actualizar(Libro libro) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(libro);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // eliminar un libro por su id
    public void eliminar(long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Libro libro = em.find(Libro.class, id);
            if (libro != null) {
                em.remove(libro);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    // buscar un libro por id
    public Libro buscarPorId(long id) {
        EntityManager em = JPAUtil.getEntityManager();
        Libro libro = em.find(Libro.class, id);
        em.close();
        return libro;
    }

    // traer todos los libros
    public List<Libro> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        List<Libro> lista = em.createQuery("SELECT l FROM Libro l ORDER BY l.id", Libro.class).getResultList();
        em.close();
        return lista;
    }
}
