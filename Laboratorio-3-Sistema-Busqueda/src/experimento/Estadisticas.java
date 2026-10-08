package experimento;

public class Estadisticas {

    // Promedio = suma de los valores dividida entre la cantidad.
    // El arreglo no debe estar vacío.
    public static double calcularPromedio(double[] valores) {
        double suma = 0;
        for (int i = 0; i < valores.length; i++) {
            suma = suma + valores[i];
        }
        return suma / valores.length;
    }

    // Desviación estándar MUESTRAL: se divide entre (n - 1).
    // Con menos de 2 valores devuelve 0.
    public static double calcularDesviacionEstandar(double[] valores) {
        if (valores.length < 2) {
            return 0;
        }
        double promedio = calcularPromedio(valores);
        double sumaCuadrados = 0;
        for (int i = 0; i < valores.length; i++) {
            double diferencia = valores[i] - promedio;
            sumaCuadrados = sumaCuadrados + diferencia * diferencia;
        }
        return Math.sqrt(sumaCuadrados / (valores.length - 1));
    }

    // Copia el arreglo y lo ordena de menor a mayor (ordenamiento por inserción).
    // Con unas decenas de valores es suficiente y fácil de explicar.
    private static double[] ordenarCopia(double[] valores) {
        double[] copia = new double[valores.length];
        for (int i = 0; i < valores.length; i++) {
            copia[i] = valores[i];
        }
        for (int i = 1; i < copia.length; i++) {
            double actual = copia[i];
            int posicion = i - 1;
            while (posicion >= 0 && copia[posicion] > actual) {
                copia[posicion + 1] = copia[posicion];
                posicion--;
            }
            copia[posicion + 1] = actual;
        }
        return copia;
    }

    // Mediana de un tramo [desde, hasta] de un arreglo YA ordenado.
    private static double medianaDeTramo(double[] ordenado, int desde, int hasta) {
        int cantidad = hasta - desde + 1;
        int medio = desde + cantidad / 2;
        if (cantidad % 2 == 1) {
            return ordenado[medio];
        }
        return (ordenado[medio - 1] + ordenado[medio]) / 2.0;
    }

    public static double calcularMediana(double[] valores) {
        double[] ordenado = ordenarCopia(valores);
        return medianaDeTramo(ordenado, 0, ordenado.length - 1);
    }

    // Q1 = mediana de la mitad inferior. Necesita al menos 4 valores.
    public static double calcularCuartil1(double[] valores) {
        double[] ordenado = ordenarCopia(valores);
        int mitad = ordenado.length / 2;
        return medianaDeTramo(ordenado, 0, mitad - 1);
    }

    // Q3 = mediana de la mitad superior. Si la cantidad es impar,
    // la mediana del centro no se incluye en ninguna mitad.
    public static double calcularCuartil3(double[] valores) {
        double[] ordenado = ordenarCopia(valores);
        int inicioMitadSuperior = ordenado.length - ordenado.length / 2;
        return medianaDeTramo(ordenado, inicioMitadSuperior, ordenado.length - 1);
    }

    // Marca con true los valores atípicos según la regla del IQR.
    // Con menos de 4 valores no se marca ninguno.
    public static boolean[] marcarAtipicos(double[] valores) {
        boolean[] esAtipico = new boolean[valores.length];
        if (valores.length < 4) {
            return esAtipico;
        }
        double q1 = calcularCuartil1(valores);
        double q3 = calcularCuartil3(valores);
        double rango = q3 - q1;
        double limiteInferior = q1 - 1.5 * rango;
        double limiteSuperior = q3 + 1.5 * rango;
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] < limiteInferior || valores[i] > limiteSuperior) {
                esAtipico[i] = true;
            }
        }
        return esAtipico;
    }

    public static int contarAtipicos(double[] valores) {
        boolean[] esAtipico = marcarAtipicos(valores);
        int cantidad = 0;
        for (int i = 0; i < esAtipico.length; i++) {
            if (esAtipico[i]) {
                cantidad++;
            }
        }
        return cantidad;
    }

    // Devuelve un arreglo nuevo SIN los atípicos. El original no se modifica.
    public static double[] quitarAtipicos(double[] valores) {
        boolean[] esAtipico = marcarAtipicos(valores);
        int cantidadNormales = valores.length - contarAtipicos(valores);
        double[] sinAtipicos = new double[cantidadNormales];
        int posicion = 0;
        for (int i = 0; i < valores.length; i++) {
            if (esAtipico[i] == false) {
                sinAtipicos[posicion] = valores[i];
                posicion++;
            }
        }
        return sinAtipicos;
    }
}