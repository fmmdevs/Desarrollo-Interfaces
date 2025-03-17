package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MainViewController {

    private DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
    private ObservableList<Pelicula> listaPeliculas;


    @FXML
    private TableColumn<Pelicula, Integer> columnaAnio;

    @FXML
    private TableColumn<Pelicula, String> columnaDescripcion;

    @FXML
    private TableColumn<Pelicula, String> columnaDirector;

    @FXML
    private TableColumn<Pelicula, String> columnaGenero;

    @FXML
    private TableColumn<Pelicula, Integer> columnaId;

    @FXML
    private TableColumn<Pelicula, Float> columnaPuntuacion;

    @FXML
    private TableColumn<Pelicula, String> columnaTitulo;

    @FXML
    private ImageView img;

    @FXML
    private TableView<Pelicula> tablaPeliculas;

    @FXML
    private Text textAnio;

    @FXML
    private Text textDescripcion;

    @FXML
    private Text textDirector;

    @FXML
    private Text textGenero;

    @FXML
    private Text textId;

    @FXML
    private Text textPuntuacion;

    @FXML
    private Text textTitulo;



    @FXML
    void handlerEliminar(ActionEvent event) {

        Pelicula peliculaSeleccionada = tablaPeliculas.getSelectionModel().getSelectedItem();

        if (peliculaSeleccionada == null) {
            // Si no hemos seleccionado ninguna película y seleccionamos eliminar mostramos un alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error eliminando");
            alert.setContentText("Debe seleccionar una persona");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación eliminar película");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro de que quieres eliminar la película?");

            Optional<ButtonType> resultado = alert.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                listaPeliculas.remove(peliculaSeleccionada);
            }
        }
    }

    @FXML
    void handlerModificar(ActionEvent event) {
        // obtenemos la persona seleccionada, si no hay seleccionada -> alert
        Pelicula pelicula = tablaPeliculas.getSelectionModel().getSelectedItem();

        if (pelicula == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error editando");
            alert.setContentText("Debe seleccionar una pelicula");
            alert.showAndWait();
        } else {

            Scene scene = null;
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("PeliculaView.fxml"));

            try {
                scene = new Scene(fxmlLoader.load(), 760, 910);
            } catch (IOException e) {
                System.err.println("Error al intentar editar una película");
                System.out.println(e);
            }
            Stage stage = new Stage();
            stage.setTitle("Editar Pelicula");
            stage.setScene(scene);
            // Parte distinta de handlerNuevo
            // Accedemos al Controller de la Scene
            PeliculaViewController controller = fxmlLoader.getController();

            controller.setPelicula(pelicula);

            System.out.println(pelicula);
            stage.showAndWait();
        }

    }

    @FXML
    void handlerNueva(ActionEvent event) {
        Scene scene = null;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("PeliculaView.fxml"));

        try {
            scene = new Scene(fxmlLoader.load(), 760, 910);
        } catch (IOException e) {
            System.err.println("Error al intentar crear un nueva película");
            System.out.println(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Nueva película");
        PeliculaViewController controller = fxmlLoader.getController();
        // Para controlar los campos en el mismo método, y antes del initialize
        controller.setPelicula(null);
        stage.setScene(scene);
        stage.showAndWait();
    }


    public void handlerCerrar(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación cerrar");
        alert.setHeaderText(null);
        alert.setContentText("¿Estás seguro de que quieres salir de la aplicación");

        Optional<ButtonType> resultado = alert.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }


    public void initialize() {
        // estilos


        listaPeliculas = datosFilmoteca.getListaPeliculas();
        //System.out.println(listaPersonas);

        columnaId.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("id"));
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("title"));
        //columnaAnio.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("year"));
        //columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("description"));
        //columnaPuntuacion.setCellValueFactory(new PropertyValueFactory<Pelicula, Float>("rating"));

        // columnaAnio.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("poster"));


        tablaPeliculas.setItems(listaPeliculas);

        tablaPeliculas.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            // Cada vez que cambiamos el item seleccionado se ejecuta este listener
            System.out.println("hey");
            if (newValue != null) {
                // Si tengo algo seleccionado
                textAnio.setText(String.valueOf(newValue.getYear()));
                textDescripcion.setText(String.valueOf(newValue.getDescription()));

                //textDirector.setText(String.valueOf(newValue.get())); no hay atributo director en Pelicula

                textPuntuacion.setText(String.valueOf(newValue.getRating()));
                textId.setText(String.valueOf(newValue.getId()));
                textTitulo.setText(String.valueOf(newValue.getTitle()));

                img.setImage(new Image(newValue.getPoster()));
            }
        }));
    }


}
