package devs.fmm;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {

    // Label para mostrar los números y errores
    private Label display = new Label("0");

    // String donde se almacena la operación que estamos realizando
    private String operacion = "";

    // El primer operando
    private double númeroAnterior;

    // El botón que se pulsó la ven anterior a la actual
    private Button anteriorBoton = new Button("");

    // Vamos a formatear los decimales con símbolos del Locale.US para que nos sea más sencillo operar con Strings que
    // son decimales.
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    // Para formatear los números decimales. Al usar los símbolos de Locale.US usa como separador de los decimales
    // un "." en lugar de una ",".
    // También, al usar # en el patrón, si tenemos ceros por la derecha que no aportan valor los elimina.
    DecimalFormat formato = new DecimalFormat("#.###############", symbols);

    // Para mostrar el historial de operaciones uso un TextArea
    TextArea historial = new TextArea("");


    private void actualizarDisplay(ActionEvent actionEvent) {


        // Obtenemos el elemento que ha originado el evento, en nuestro caso sabemos que todos los ActionEvent que llaman
        // a esta función son Button
        Button btn = (Button) actionEvent.getSource();


        // Si es el Button cuyo texto es "Limpiar historial" reseteamos el TextArea con el historial de operaciones.
        if (btn.getText().equals("Limpiar historial")) {
            historial.setText("");
        }

        // Uso un try-catch para manejar las excepciones que se producen si pulsamos dos operaciones seguidas y muestro
        // un mensaje por consola, el usuario en este caso no percibe ningún error, la calculadora no cambia el display y
        // se puede seguir con la operacion que iba a hacerse.
        // Por ejemplo salta al hacer 6 x =
        try {


           if (btn.getText().equals("DEL") || (btn.getText().equals("=") && !Character.isDigit(anteriorBoton.getText().charAt(0)))) {
               // Si pulsamos el botón rojo DEL o si hemos el botón anterior era una operacion y este es un igual
               // reseteamos la calculadora.
                display.setText("0");
                númeroAnterior = 0;
                operacion = "";
                anteriorBoton.setText("");

            } else if ((anteriorBoton.getText().equals("-") || anteriorBoton.getText().equals("+")
                   || anteriorBoton.getText().equals("/") || anteriorBoton.getText().equals("x") || display.getText().charAt(0)=='E') && Character.isDigit(btn.getText().charAt(0))) {
               // Si el botón anterior era una operacion o en el display hay un error y el botón pulsado es un número
               // actualizamos el display con el número
               display.setText(btn.getText());

            } else if (anteriorBoton.getText().equals("=") && Character.isDigit(btn.getText().charAt(0))) {
                // Si el botón anterior era "=" y este botón es un número, resetamos operación y display.
                display.setText(btn.getText());
                operacion = "";

            } else if (Character.isDigit(btn.getText().charAt(0)) && display.getText().length() <= 17) {
                // Si el botón es un número y en el display hay 15 caracteres o menos
                if (display.getText().equals("0") || display.getText().isEmpty()) {
                    // Si en el display había un cero mostramos el número del botón
                    display.setText(btn.getText());

                } else {
                    // Si no era un cero concatenamos lo que hubiese con el número del botón
                    display.setText(display.getText() + btn.getText());
                }
            } else if (Character.isDigit(btn.getText().charAt(0)) && display.getText().length() > 17) {
               // Si hay más de 15 caracteres mostramos un error en el display.
                display.setText("Error: Max cifras=17.");
            }


            if ((btn.getText().equals("+") || btn.getText().equals("-") || btn.getText().equals("/") || btn.getText().equals("x"))
                    && (!anteriorBoton.getText().equals("+") && !anteriorBoton.getText().equals("-") && !anteriorBoton.getText().equals("/") && !anteriorBoton.getText().equals("x"))) {
                // Si el botón es una operacion y el anterior no era una operación, almacenamos el número y la operacion
                // y borramos el display.
                operacion = btn.getText();
                númeroAnterior = Double.parseDouble(display.getText());
                display.setText("");
            }



            if (btn.getText().equals("=") && Character.isDigit(anteriorBoton.getText().charAt(0)) && !operacion.isEmpty()) {
                // Si el botón anterior era un número, este botón es un "=" y tenemos operacion, significa que tenemos que
                // calcular un resultado

                // Almacenamos en el historial lo que hubiese concatenado a el primer operando (que aplicamos el formato
                // de DecimalFormat), seguido de la operacion, seguido de lo que haya en el display (segundo operador),
                // seguido de un igual
                historial.setText("%s%s %s %s = ".formatted(historial.getText(), formato.format(númeroAnterior), operacion, display.getText()));

                if (operacion.equals("+"))
                    // Si la operacion es un + sumamos los operandos y les aplicamos el formato de DecimalFormat.
                    // Y así con todas las operaciones
                    display.setText(formato.format((double) númeroAnterior + Double.parseDouble(display.getText())));

                if (operacion.equals("-"))
                    display.setText(formato.format((double) númeroAnterior - Double.parseDouble(display.getText())));

                if (operacion.equals("x"))


                    display.setText(formato.format((double) númeroAnterior * Double.parseDouble(display.getText())));


                if (operacion.equals("/")) {
                    // Comprobamos que el segundo operando no sea 0
                    if (display.getText().equals("0")) {
                        // Si es 0 mostramos un error en el display, también se guardará en el historial de operaciones.
                        display.setText("Error: División por 0.");
                    } else {
                        // Si no es 0 procedemos a actualizar el display con el resultado de la division formateada con
                        // DecimalFormat
                        display.setText(formato.format((double) númeroAnterior / Double.parseDouble(display.getText())));
                    }
                }

                // Tras hacer una operacion reseteamos la variable que nos indica que operación estamos realizando
                operacion = "";

                // Actualizamos el texto del historial concatenando lo que hubiese con el resultado de la operación
                historial.setText("%s%s%n".formatted(historial.getText(), display.getText()));

                // Si hay mucho texto en el historial el Scroll se actualiza a la parte más baja
                historial.positionCaret(historial.getText().length());

            }

        } catch (StringIndexOutOfBoundsException e) {
            System.err.println("Error: Has pulsado número-operacion-igual. Necesitas: número-operacion-número-igual");
            display.setText("Error: Dos operaciones seguidas");
        } catch (NumberFormatException e){
            System.err.println("Error: Has pulsado una operación cuando se estaba mostrando un error por pantalla");
            display.setText("Error. Operación pulsada tras error");
        }
        
        // Guardamos el texto del botón que acabamos de pulsar
        anteriorBoton.setText(btn.getText());
        
    }


    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Establecemos el título de la ventana
        primaryStage.setTitle("Calculadora");

        // La ventana no puede cambiar de tamaño
        primaryStage.setResizable(false);

        // Para que el Label con el display se alinee a la derecha (movido al .css)
        //display.setAlignment(Pos.CENTER_RIGHT);

        // Vamos creando los botones que de nuesta calculadora, el texto de cada uno va a representar el numero o
        // la operacion que realizan
        // fila 1
        Button btn7 = new Button("7");
        btn7.getStyleClass().add("botonNumerico");
        btn7.setLayoutX(220);
        btn7.setLayoutY(80);

        Button btn8 = new Button("8");
        btn8.getStyleClass().add("botonNumerico");
        // x+75. Espacio horizontal entre botones = 75
        btn8.setLayoutX(295);
        btn8.setLayoutY(80);

        Button btn9 = new Button("9");
        btn9.getStyleClass().add("botonNumerico");
        // x+75
        btn9.setLayoutX(370);
        btn9.setLayoutY(80);

        Button btnSuma = new Button("+");
        btnSuma.setLayoutX(445);
        btnSuma.setLayoutY(80);

        // fila 2
        Button btn4 = new Button("4");
        btn4.getStyleClass().add("botonNumerico");

        btn4.setLayoutX(220);
        // y+61. Espacio vertical entre botones = 61
        btn4.setLayoutY(141);

        Button btn5 = new Button("5");
        btn5.getStyleClass().add("botonNumerico");
        btn5.setLayoutX(295);
        btn5.setLayoutY(141);

        Button btn6 = new Button("6");
        btn6.getStyleClass().add("botonNumerico");
        btn6.setLayoutX(370);
        btn6.setLayoutY(141);

        Button btnResta = new Button("-");
        btnResta.setLayoutX(445);
        btnResta.setLayoutY(141);

        // fila 3
        Button btn1 = new Button("1");
        btn1.getStyleClass().add("botonNumerico");
        btn1.setLayoutX(220);
        btn1.setLayoutY(202);

        Button btn2 = new Button("2");
        btn2.getStyleClass().add("botonNumerico");
        btn2.setLayoutX(295);
        btn2.setLayoutY(202);

        Button btn3 = new Button("3");
        btn3.getStyleClass().add("botonNumerico");
        btn3.setLayoutX(370);
        btn3.setLayoutY(202);

        Button btnDel = new Button("DEL");
        btnDel.setId("btnDel");
        btnDel.setLayoutX(445);
        btnDel.setLayoutY(202);

        // fila 4
        Button btn0 = new Button("0");
        btn0.getStyleClass().add("botonNumerico");
        btn0.setLayoutX(220);
        btn0.setLayoutY(263);

        Button btnMultiplicacion = new Button("x");
        btnMultiplicacion.setLayoutX(295);
        btnMultiplicacion.setLayoutY(263);

        Button btnDivision = new Button("/");
        btnDivision.setLayoutX(370);
        btnDivision.setLayoutY(263);

        Button btnIgual = new Button("=");
        btnIgual.setId("btnIgual");
        btnIgual.setLayoutX(445);
        btnIgual.setLayoutY(263);


        // botón reset historial
        Button btnReset = new Button("Limpiar historial");
        btnReset.setId("btnReset");
        btnReset.setLayoutX(40);
        btnReset.setLayoutY(324);

        // Creamos un anchorPane y aprovechamos el constructor para añadirle todos los botones, el Label y el TextArea
        AnchorPane anchorPane = new AnchorPane(display, btn7, btn8, btn9, btnSuma, btn4, btn5, btn6, btnResta, btn1,
                btn2, btn3, btnDel, btn0, btnMultiplicacion, btnDivision, btnIgual, historial, btnReset);

        // Anclar label al ancho del AnchorPane. Para que el label aparezca en la parte izquierda y conforme vayan
        // habiendo mas números crezca hacia la derecha.
        AnchorPane.setTopAnchor(display, 10.0);
        AnchorPane.setRightAnchor(display, 10.0);
        AnchorPane.setLeftAnchor(display, 10.0);

        // Todos los botones al accionarse llaman a actualizarDisplay()
        btn0.setOnAction(this::actualizarDisplay);
        btn1.setOnAction(this::actualizarDisplay);
        btn2.setOnAction(this::actualizarDisplay);
        btn3.setOnAction(this::actualizarDisplay);
        btn4.setOnAction(this::actualizarDisplay);
        btn5.setOnAction(this::actualizarDisplay);
        btn6.setOnAction(this::actualizarDisplay);
        btn7.setOnAction(this::actualizarDisplay);
        btn8.setOnAction(this::actualizarDisplay);
        btn9.setOnAction(this::actualizarDisplay);

        btnDel.setOnAction(this::actualizarDisplay);
        btnMultiplicacion.setOnAction(this::actualizarDisplay);
        btnDivision.setOnAction(this::actualizarDisplay);
        btnIgual.setOnAction(this::actualizarDisplay);
        btnSuma.setOnAction(this::actualizarDisplay);
        btnResta.setOnAction(this::actualizarDisplay);

        btnReset.setOnAction(this::actualizarDisplay);

        // Historial
        historial.setLayoutX(8);
        historial.setLayoutY(80);
        // Para que no se pueda editar
        historial.setEditable(false);

        // Establecemos una altura y un ancho acorde al tamaño de la Scene. (movido al .css)
       /* historial.setPrefHeight(236);
        historial.setPrefWidth(205);*/

        // Creamos el Scene, que contiene el anchorPane que, a su vez,
        // contiene el resto de elementos visuales
        Scene sc = new Scene(anchorPane, 520, 360);

        // Para que por defecto el foco de los botones este en la tecla igual, si no al pulsar intro va a tomar el
        // botón que tenga el foco y nos interesa que al pulsar intro actue como si hubiesemos pulsado el btnIgual
        btnIgual.requestFocus();

        // Eventos de teclado. Ponemos a la Scene a escuchar si pulsamos una tecla del teclado
        sc.setOnKeyPressed(event -> {
            // Para aprovechar la lógica utilizada en los eventos de botón, al pulsar la tecla que se tiene que comportar
            // igual que determinado botón llamo a la funcion actualizarDisplay y le paso un ActionEvent con el botón.
            switch (event.getCode()) {
                // Cada tecla del teclado tiene un KeyCode asociado (KeyCode es un Enum con los códigos de todas las teclas)
                case DIGIT0:
                case NUMPAD0:
                    actualizarDisplay(new ActionEvent(btn0, null));
                    break;
                case DIGIT1:
                case NUMPAD1:
                    actualizarDisplay(new ActionEvent(btn1, null));
                    break;
                case DIGIT2:
                case NUMPAD2:
                    actualizarDisplay(new ActionEvent(btn2, null));
                    break;
                case DIGIT3:
                case NUMPAD3:
                    actualizarDisplay(new ActionEvent(btn3, null));
                    break;
                case DIGIT4:
                case NUMPAD4:
                    actualizarDisplay(new ActionEvent(btn4, null));
                    break;
                case DIGIT5:
                case NUMPAD5:
                    actualizarDisplay(new ActionEvent(btn5, null));
                    break;
                case DIGIT6:
                case NUMPAD6:
                    actualizarDisplay(new ActionEvent(btn6, null));
                    break;
                case DIGIT7:
                case NUMPAD7:
                    actualizarDisplay(new ActionEvent(btn7, null));
                    break;
                case DIGIT8:
                case NUMPAD8:
                    actualizarDisplay(new ActionEvent(btn8, null));
                    break;
                case DIGIT9:
                case NUMPAD9:
                    actualizarDisplay(new ActionEvent(btn9, null));
                    break;
                case MULTIPLY:
                    actualizarDisplay(new ActionEvent(btnMultiplicacion, null));
                    break;
                case DIVIDE:
                    actualizarDisplay(new ActionEvent(btnDivision, null));
                    break;
                case SUBTRACT:
                    actualizarDisplay(new ActionEvent(btnResta, null));
                    break;
                case ADD:
                    actualizarDisplay(new ActionEvent(btnSuma, null));
                    break;
                case ENTER:
                    actualizarDisplay(new ActionEvent(btnIgual, null));
                    break;
                case BACK_SPACE:
                    actualizarDisplay(new ActionEvent(btnDel, null));
                    break;
            }
        });

        // Indicamos a nuestra Scene que archivo css tiene que usar
        sc.getStylesheets().add("css/main.css");

        // Asociamos nuestra Scene al Stage
        primaryStage.setScene(sc);

        // Mostramos el Stage con todo su contenido asociado
        primaryStage.show();


    }
}