package devs.fmm.di05;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ArtistasViewController {
    @FXML
    private Button generarInformeBtn;
    @FXML
    private TableColumn<Artista, Integer> columnaId;
    @FXML
    private TableColumn<Artista, String> columnaNombre;
    @FXML
    private TableView<Artista> tablaArtistas;

    private String artistaSeleccionado;


    DatosArtistas datosArtistas = DatosArtistas.getInstancia();
    ObservableList<Artista> listaArtistas = datosArtistas.getListaClientes();

    public void handlerGenerarInforme(ActionEvent actionEvent) {
        String urlDB="jdbc:sqlite:db/chinook.db";
        String jasperFilePath = "informes/InformeArtistas.jrxml";

        // Abrimos el .jrxml en un try-with-resources
        try (InputStream inputStream = Main.class.getResourceAsStream(jasperFilePath)) {
            // Compilamos el .jrxml
            System.out.println("Compilando: " + jasperFilePath);
            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

            // Conexión a la BBDD
            Connection conn = DriverManager.getConnection(urlDB);

            // Creamos el parámetro
            Map<String, Object> parametros = new HashMap<>();
            parametros.put("nombreArtista", artistaSeleccionado);


            // Cargamos los parametros y la conexion con la BBDD, en este caso,cargamos la conexion
            // y como no tenemos parametros proporcionamos objetos vacíos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conn);

            // Mostramos el informe, false  para que no se cierre la aplicacion
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

    public void handlerCerrar(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();

    }

    public void initialize(){
        generarInformeBtn.setDisable(true);


        listaArtistas = datosArtistas.getListaClientes();

        columnaId.setCellValueFactory(new PropertyValueFactory<Artista,Integer>("id"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<Artista,String>("name"));

        tablaArtistas.setItems(listaArtistas);

        tablaArtistas.getSelectionModel().selectedItemProperty().addListener((((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // Si tengo algo seleccionado
                artistaSeleccionado=newValue.getName();
                generarInformeBtn.setDisable(false);
            }
        })));
    }
}
