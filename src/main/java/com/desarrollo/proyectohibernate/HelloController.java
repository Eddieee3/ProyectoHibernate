package com.desarrollo.proyectohibernate;

import com.desarrollo.proyectohibernate.dao.LibroDAO;
import com.desarrollo.proyectohibernate.model.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class HelloController {

    // campos del formulario
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAutor;
    @FXML
    private TextField txtAnio;
    @FXML
    private TextField txtPrecio;
    @FXML
    private Label lblMensaje;

    // tabla y columnas
    @FXML
    private TableView<Libro> tablaLibros;
    @FXML
    private TableColumn<Libro, Long> colId;
    @FXML
    private TableColumn<Libro, String> colTitulo;
    @FXML
    private TableColumn<Libro, String> colAutor;
    @FXML
    private TableColumn<Libro, Integer> colAnio;
    @FXML
    private TableColumn<Libro, BigDecimal> colPrecio;

    LibroDAO libroDAO = new LibroDAO();
    Libro libroSeleccionado = null;

    @FXML
    public void initialize() {
        // se enlazan las columnas con los atributos de la clase Libro
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anioPublicacion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));

        // cuando se da click en una fila se pasan los datos a los campos
        tablaLibros.setOnMouseClicked(event -> {
            libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
            if (libroSeleccionado != null) {
                txtTitulo.setText(libroSeleccionado.getTitulo());
                txtAutor.setText(libroSeleccionado.getAutor());
                txtAnio.setText(String.valueOf(libroSeleccionado.getAnioPublicacion()));
                txtPrecio.setText(String.valueOf(libroSeleccionado.getPrecio()));
            }
        });

        cargarTabla();
    }

    @FXML
    private void guardarLibro() {
        if (!validarCampos()) {
            return;
        }

        String titulo = txtTitulo.getText();
        String autor = txtAutor.getText();
        int anio = Integer.parseInt(txtAnio.getText());
        BigDecimal precio = new BigDecimal(txtPrecio.getText());

        try {
            Libro libro = new Libro(
                    titulo, autor, anio, precio
            );
            libroDAO.guardar(libro);

            lblMensaje.setText("Libro guardado correctamente");
            limpiarCampos();
            cargarTabla();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo guardar el libro: " + e.getMessage());
        }
    }

    @FXML
    public void modificarLibro() {
        if (libroSeleccionado == null) {
            mostrarAlerta("Aviso", "Primero seleccione un libro de la tabla");
            return;
        }
        if (!validarCampos()) {
            return;
        }

        try {
            libroSeleccionado.setTitulo(txtTitulo.getText());
            libroSeleccionado.setAutor(txtAutor.getText());
            libroSeleccionado.setAnioPublicacion(Integer.parseInt(txtAnio.getText()));
            libroSeleccionado.setPrecio(new BigDecimal(txtPrecio.getText()));

            libroDAO.actualizar(libroSeleccionado);

            lblMensaje.setText("Libro modificado correctamente");
            limpiarCampos();
            cargarTabla();
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo modificar el libro: " + e.getMessage());
        }
    }

    @FXML
    public void eliminarLibro() {
        if (libroSeleccionado == null) {
            mostrarAlerta("Aviso", "Primero seleccione un libro de la tabla");
            return;
        }

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Eliminar");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Seguro que desea eliminar el libro " + libroSeleccionado.getTitulo() + "?");
        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                libroDAO.eliminar(libroSeleccionado.getId());
                lblMensaje.setText("Libro eliminado");
                limpiarCampos();
                cargarTabla();
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo eliminar el libro: " + e.getMessage());
            }
        }
    }

    @FXML
    public void limpiarCampos() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtAnio.setText("");
        txtPrecio.setText("");
        libroSeleccionado = null;
        tablaLibros.getSelectionModel().clearSelection();
    }

    // metodo para llenar la tabla con los libros de la base de datos
    public void cargarTabla() {
        try {
            List<Libro> lista = libroDAO.listarTodos();
            ObservableList<Libro> datos = FXCollections.observableArrayList(lista);
            tablaLibros.setItems(datos);
        } catch (Exception e) {
            lblMensaje.setText("Error al conectar con la base de datos");
            System.out.println("Error: " + e.getMessage());
        }
    }

    public boolean validarCampos() {
        if (txtTitulo.getText().isEmpty() || txtAutor.getText().isEmpty()
                || txtAnio.getText().isEmpty() || txtPrecio.getText().isEmpty()) {
            mostrarAlerta("Aviso", "Debe llenar todos los campos");
            return false;
        }

        try {
            Integer.parseInt(txtAnio.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Aviso", "El año debe ser un numero");
            return false;
        }

        try {
            new BigDecimal(txtPrecio.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Aviso", "El precio debe ser un numero");
            return false;
        }

        return true;
    }

    public void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
