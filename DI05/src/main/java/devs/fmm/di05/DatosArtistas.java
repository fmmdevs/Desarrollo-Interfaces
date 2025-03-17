package devs.fmm.di05;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatosArtistas {
    private static DatosArtistas instancia = null;
    private static ObservableList<Artista> listaArtistas = FXCollections.observableArrayList();

    private static void DatosClientes () {
    }

    public static DatosArtistas getInstancia() {
        if (instancia == null) {
            instancia = new DatosArtistas();
        }
        return instancia;
    }

    public ObservableList<Artista> getListaClientes() {
        return listaArtistas;
    }

}
