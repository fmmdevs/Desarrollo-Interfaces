package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PeliculaViewController {

    public ImageView imgPoster;
    public Slider sliderRating;
    public Label ratingValue;
    public Text textTitulo;
    DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
    ObservableList<Pelicula> listaPeliculas = datosFilmoteca.getListaPeliculas();

    private Pelicula pelicula;

    @FXML
    private TextField campoAnio;

    @FXML
    private TextArea campoDescripcion;

    @FXML
    private TextField campoId;

    @FXML
    private TextField campoPoster;


    @FXML
    private TextField campoTitulo;

    @FXML
    void cancelarHandler(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void handlerAceptar(ActionEvent event) {

        // Todos los campos son obligatorios
        if (campoDescripcion.getText().isEmpty() || campoPoster.getText().isEmpty() || campoAnio.getText().isEmpty() ||
                campoTitulo.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campos obligatorios");
            alert.setContentText("Todos los campos son obligatorios");
            alert.showAndWait();
        } else if (imgPoster.getImage() == null || imgPoster.getImage().getWidth() == 0 || imgPoster.getImage().getWidth() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Imagen no previsualizada");
            alert.setContentText("La imagen deber previsualizarse correctamente");
            alert.showAndWait();
        } else {

            String descripcion = campoDescripcion.getText();
            int id = Integer.parseInt(campoId.getText());
            String urlPoster = campoPoster.getText();
            float rating = Float.valueOf(ratingValue.getText().replace(',', '.'));
            int anio = Integer.parseInt(campoAnio.getText());
            String titulo = campoTitulo.getText();

            if (pelicula == null) {
                // Si la pelicula es null es por que estamos creando una nueva pelicula

                Pelicula nuevaPelicula = new Pelicula(id, titulo, anio, descripcion, rating, urlPoster);
                listaPeliculas.add(nuevaPelicula);
                // no tenemos referencia a la ventana para cerrarla.
                // 1. Obtenemos el Nodo que ha lanzado el evento, en nuestro caso un Button, que a su vez es
                // un Node. Nos interesa más tratarlo como Node, así este codigo nos sirve para cualquier elemento.
                // 2. Obtenemos la Scene donde se encuentra este Node
                // 3. Obtenemos el objeto Window que podemos castear directamente a Stage.

            } else {
                // Si no es null estamos modificando una pelicula
                pelicula.setTitle(titulo);
                pelicula.setYear(anio);
                pelicula.setRating(rating);
                pelicula.setPoster(urlPoster);
                pelicula.setDescription(descripcion);

            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }

    }


    public void initialize() {
        // Dejamos el campo deshabilitado, ya que la ID no se puede modificar.
        campoId.setDisable(true);

        sliderRating.setMax(10.0);
        // Asignamos valor inicial
        //ratingValue.setText("%.2f".formatted(sliderRating.getValue()));
        // listener para mostrar el valor actual del slider
        sliderRating.valueProperty().addListener(((observable, oldValue, newValue) -> {
            ratingValue.setText("%.2f".formatted(newValue));
        }));

        // Year solo puede contener números
        campoAnio.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                campoAnio.setText(oldValue);
                // Warning en el campo?
            }
        });


    }

    // Previsualizar imagen
    public void handlerPrevisualizar(ActionEvent actionEvent) {
        try {
            Image poster = new Image(campoPoster.getText());
            System.out.println(poster.getHeight());
            System.out.println(poster.getWidth());
            // Obligamos a que salte una excepcion si no hemos obtenido una imagen de la url
            if (poster.getHeight() == 0.0 || poster.getWidth() == 0.0) {
                imgPoster.setImage(null);
                throw new Exception();
            }
            imgPoster.setImage(poster);
        } catch (Exception e) {

            // Al saltar la excepcion mostrarmos un mensaje indicando que no hemos podido obtener la imagen
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No se ha podido obtener la imagen, compruebe la url");
            alert.setTitle("Error URL imagen");
            alert.show();
        }
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;

        System.out.println(pelicula);
        if (pelicula == null) {
            textTitulo.setText("Nueva película");
            // Para asignar un id cada vez que creamos película, en la lista de películas buscamos la ultima película,
            // obtenemos su id y le sumamos 1.
            int ultimaId = listaPeliculas.get(listaPeliculas.size() - 1).getId();
            System.out.println(ultimaId);
            campoId.setText(String.valueOf(ultimaId + 1));
            ratingValue.setText("0,0");

        } else {
            textTitulo.setText("Modificando película");
            campoId.setText(String.valueOf(pelicula.getId()));
            campoTitulo.setText(pelicula.getTitle());
            campoAnio.setText(String.valueOf(pelicula.getYear()));
            campoDescripcion.setText(pelicula.getDescription());
            campoPoster.setText(pelicula.getPoster());
            sliderRating.setValue(pelicula.getRating());
        }
    }
}
