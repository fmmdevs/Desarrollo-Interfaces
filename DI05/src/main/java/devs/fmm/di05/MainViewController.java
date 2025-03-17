package devs.fmm.di05;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

public class MainViewController {


String urlDB="jdbc:sqlite:db/chinook.db";

    @FXML
    protected void handlerCerrar() {
        System.exit(0);
    }

    @FXML
    protected void handlerInformeClientes() {
        String jasperFilePath = "informes/InformeClientes.jrxml";

        // Abrimos el .jrxml en un try-with-resources
        try (InputStream inputStream = Main.class.getResourceAsStream(jasperFilePath)) {
            // Compilamos el .jrxml
            System.out.println("Compilando: " + jasperFilePath);
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            // Conexión a la BBDD
            Connection conn = DriverManager.getConnection(urlDB);

            // Cargamos los parametros y la conexion con la BBDD, en este caso,cargamos la conexion
            // y como no tenemos parametros proporcionamos objetos vacíos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), conn);

            // Mostramos el informe, false para que no se cierre la aplicacion
            JasperViewer.viewReport(jasperPrint, false);

        } catch (IOException e) {
            System.err.println("Error abriendo fichero");
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error con la BBDD");
            e.printStackTrace();
        }
    }

    @FXML
    protected void handlerInformeArtistas() {
        Scene scene = null;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/devs/fmm/di05/artistas-view.fxml"));

        try {
            scene = new Scene(fxmlLoader.load(), 600, 400);
        } catch (IOException e) {
            System.err.println("Error al mostrar vista de artistas");
            System.out.println(e);
        }
        Stage stage = new Stage();
        stage.setTitle("Artistas");
        ArtistasViewController controller = fxmlLoader.getController();
        // Para controlar los campos en el mismo método, y antes del initialize
        // controller.setPelicula(null);
        stage.setScene(scene);
        stage.showAndWait();
    }
}