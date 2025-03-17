package devs.fmm.di03;

/* Para evitar los problemas que pueda dar que nuestra clase principal extienda de otra a la hora de generar el jar
* usamos esta clase Main, que lo único que hace es llamar al método main de nuestra MainApplication*/
public class Main {
    public static void main(String[] args) {
        MainApplication.main(args);
    }
}
