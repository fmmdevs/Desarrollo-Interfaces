package devs.fmm.di03;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController {
    @FXML
    public TilePane numerosGenerados;
    @FXML
    public Button salirBtn;
    @FXML
    public Button generarBtn;
    @FXML
    public TextField input;
    @FXML
    public Text titulo;

    // validaciones regex
    Pattern pattern = Pattern.compile("^([1-9]?[0-9]|100)$");
    private Matcher matcher;

    // Label que vamos a incorporar a nuestro TilePane con cada uno de los números primos.
    private Label numero;

    @FXML
    public void initialize(){
        // En SceneBuilder no me dejaba escribir números con tilde, asi que no hago aquí
        titulo.setText("Calcula los números primos");
    }

    // Cada vez que se escribe un caracter en el input se ejecuta esta funcion
    public void onInputTyped(KeyEvent keyEvent) {
        // Creamos un matcher para validar la cadena de texto que haya en el TextField tras haber introducido un caracter
        matcher = pattern.matcher(input.getText());
        if (matcher.matches()) {
            // Si es válido, esta entre 0 y 100. Activamos el botón GENERAR
            generarBtn.setDisable(false);

            // Y no mostramos el TextField con el borde rojo
            input.getStyleClass().remove("cuadro-rojo");

        } else {
            // Si no es válido, deshabilitamos el botón GENERAR.
            generarBtn.setDisable(true);

            // Aplicamos al TextField la clase css .cuadro-rojo
            // Para evitar que añada la clase varias veces comprobamos si ya la tiene y si no, se la añadimos
            if (!input.getStyleClass().contains("cuadro-rojo")) {
                input.getStyleClass().add("cuadro-rojo");
            }
        }


    }

    public void handlerGenerar(ActionEvent event) {
        // Cada vez que pulsamos generar borramos todos los nodos hijos del tilePane que contiene los numeros primos generados
        numerosGenerados.getChildren().remove(0, numerosGenerados.getChildren().size());

        // Como solo se activa el botón GENERAR al ser un número entre 0 y 100, sabemos que esta paso de String a int
        // va a ser correcto
        int nPrimos = Integer.parseInt(input.getText());

        // Borramos el texto del TextField
        input.setText("");

        // Deshabilitamos el botón GENERAR
        generarBtn.setDisable(true);

        // Llamamos a la función que genera un ArrayList<Integer> con los numeros primos entre 0 y nPrimos
        ArrayList<Integer> primos = Primos.nPrimos(nPrimos);
        for (Integer primo : primos) {
            // Por cada uno de los numeros primos generados
            // Creamos un nuevo Label utilizando en el constructor el numero primo en formato String
            numero = new Label(primo.toString());
            // Le aplicamos la clase css .rectangulos
            numero.getStyleClass().add("rectangulos");
            // Añadimos el Label con el numero primo al TilePane
            numerosGenerados.getChildren().add(numero);
        }

    }


    public void handlerSalirBtn(ActionEvent event) {
        // Al pulsar salir cerramos el programa. Usamos el status 0 indicando que todo es correcto
        System.exit(0);
    }
}