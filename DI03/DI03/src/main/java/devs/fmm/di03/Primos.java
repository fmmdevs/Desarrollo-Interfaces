package devs.fmm.di03;

import java.util.ArrayList;

public class Primos {

    // Función que devuelve true si el argumento es un número primo, false en caso contrario
    private static boolean esPrimo(int numero) {
        // Por convencion 1 no se considera primo
        if (numero <= 1) return false;

        // Vamos a comprobar si el número tiene algún divisor en el rango [2,número/2], si lo tiene no es primo.
        for (int i = 2; i <= numero / 2; i++) {
            if (numero % i == 0) return false;
        }
        return true;
    }

    public static ArrayList<Integer> nPrimos(int n) {

        ArrayList<Integer> resultado = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            if (esPrimo(i)) resultado.add(i);
        }

        return resultado;

    }
}
