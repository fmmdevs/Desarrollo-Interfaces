module devs.fmm.di03 {
    requires javafx.controls;
    requires javafx.fxml;


    opens devs.fmm.di03 to javafx.fxml;
    exports devs.fmm.di03;
}