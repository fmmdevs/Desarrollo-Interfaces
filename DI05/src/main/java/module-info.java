module devs.fmm.di05 {
    requires javafx.controls;
    requires javafx.fxml;

    // IMPORTANTE
    requires jasperreports;
    requires org.xerial.sqlitejdbc;
    requires log4j;
    requires java.sql;


    // Importante
    opens devs.fmm.di05;
    exports devs.fmm.di05;
}