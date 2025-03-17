package devs.fmm.di03;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 510, 450);

        /*Título de la ventana*/
        stage.setTitle("Calcula los números primos");
        stage.setScene(scene);
        // Para evitar que la ventana se pueda reducir de más lo indicamos aquí.
        stage.setMinWidth(510);
        stage.setMinHeight(450);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}