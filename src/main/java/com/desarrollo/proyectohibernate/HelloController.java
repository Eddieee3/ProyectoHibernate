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

    // botones
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;

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
            actualizarBotones();
        });

        cargarTabla();
        actualizarBotones();
    }

    @FXML
    private void guardarLibro() {
        if (!validarCampos()) {
            return;
        }

        try {
            Libro libro = obtenerLibroFormulario();
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
            Libro libro = obtenerLibroFormulario();
            libro.setId(libroSeleccionado.getId());

            boolean modificado = libroDAO.modificar(libro);

            if (modificado) {
                lblMensaje.setText("Libro modificado correctamente");
            } else {
                mostrarAlerta("Aviso", "El libro ya no existe en la base de datos");
            }
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
                boolean eliminado = libroDAO.eliminar(libroSeleccionado.getId());

                if (eliminado) {
                    lblMensaje.setText("Libro eliminado");
                } else {
                    mostrarAlerta("Aviso", "El libro ya no existe en la base de datos");
                }
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
        actualizarBotones();
    }

    // si hay un libro seleccionado se puede modificar o eliminar,
    // si no hay ninguno solo se puede guardar uno nuevo
    private void actualizarBotones() {
        boolean haySeleccion = libroSeleccionado != null;
        btnGuardar.setDisable(haySeleccion);
        btnModificar.setDisable(!haySeleccion);
        btnEliminar.setDisable(!haySeleccion);
    }

    // crea un libro con lo que esta escrito en los campos
    private Libro obtenerLibroFormulario() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        int anio = Integer.parseInt(txtAnio.getText().trim());
        BigDecimal precio = new BigDecimal(txtPrecio.getText().trim());

        Libro libro = new Libro(titulo, autor, anio, precio);
        return libro;
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
