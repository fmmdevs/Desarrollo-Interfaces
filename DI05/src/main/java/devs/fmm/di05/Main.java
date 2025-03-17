package devs.fmm.di05;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Informes con JasperReports");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void init() {
        // Conexion con bbdd sqlite
        String urlDB = "jdbc:sqlite:db/chinook.db";
        List<Artista> artistas = new ArrayList<>();
        String query = "SELECT ArtistId, Name FROM artists";
        DatosArtistas datosArtistas = DatosArtistas.getInstancia();

        try (var conn = DriverManager.getConnection(urlDB)) {
            System.out.println("Connection to SQLite has been established.");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // ResultSet apunta a una posicion antes de la primera, al hacer rs.next() por primera vez nos encontramos
            // en la primera fila devuelta
            while (rs.next()) {
                artistas.add(new Artista(rs.getInt("ArtistId"), rs.getString("Name")));
            }
            datosArtistas.getListaClientes().setAll(artistas);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        //System.out.println(datosArtistas.getListaClientes());
    }

}