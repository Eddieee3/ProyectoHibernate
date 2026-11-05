package com.desarrollo.proyectohibernate.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

/**
 * Crea una sola EntityManagerFactory para toda la aplicación.
 * La contraseña de la base NO va en persistence.xml: se lee de la
 * variable de entorno DB_PASSWORD.
 */
public class JPAUtil {

    private static EntityManagerFactory entityManagerFactory;

    private JPAUtil() {
    }

    public static synchronized void iniciar() {
        if (entityManagerFactory == null) {
            String password = System.getenv("DB_PASSWORD");
            if (password == null || password.isBlank()) {
                throw new RuntimeException("DB_PASSWORD inválido");
            }

            Map<String, Object> propiedades = new HashMap<String, Object>();
            propiedades.put("jakarta.persistence.jdbc.password", password);
            entityManagerFactory = Persistence
                    .createEntityManagerFactory("bibliotecaPU", propiedades);
        }
    }

    public static EntityManager getEntityManager() {
        iniciar();
        return entityManagerFactory.createEntityManager();
    }

    public static synchronized void cerrar() {
        if (entityManagerFactory != null && entityManagerFactory.isOpen()) {
            entityManagerFactory.close();
        }
    }
}
