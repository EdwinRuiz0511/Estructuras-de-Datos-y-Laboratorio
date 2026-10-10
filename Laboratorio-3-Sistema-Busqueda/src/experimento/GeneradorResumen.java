package experimento;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Locale;

public class GeneradorResumen {

    // Lee el CSV crudo y escribe un CSV con una fila por configuración.
    public static void generar(String rutaEntrada, String rutaSalida) throws IOException {
        ArrayList<String[]> filas = leerFilas(rutaEntrada);
        ArrayList<String> claves = obtenerClaves(filas);

        PrintWriter escritor = new PrintWriter(new FileWriter(rutaSalida));
        escritor.println("operacion,estructura,tipoInsercion,n,m,capacidad,repeticiones,"
                + "promedioNs,desviacionNs,medianaNs,coeficienteVariacion,atipicos,"
                + "promedioSinAtipicosNs,desviacionSinAtipicosNs,alturaPromedio");

        for (int k = 0; k < claves.size(); k++) {
            escribirResumenDeConfiguracion(filas, claves.get(k), escritor);
        }
        escritor.close();
    }

    private static ArrayList<String[]> leerFilas(String ruta) throws IOException {
        ArrayList<String[]> filas = new ArrayList<String[]>();
        BufferedReader lector = new BufferedReader(new FileReader(ruta));
        String linea = lector.readLine(); // títulos: se descartan
        linea = lector.readLine();
        while (linea != null) {
            if (linea.length() > 0) {
                filas.add(linea.split(","));
            }
            linea = lector.readLine();
        }
        lector.close();
        return filas;
    }

    private static String construirClave(String[] fila) {
        return fila[0] + "," + fila[1] + "," + fila[2] + ","
                + fila[3] + "," + fila[4] + "," + fila[5];
    }

    private static ArrayList<String> obtenerClaves(ArrayList<String[]> filas) {
        ArrayList<String> claves = new ArrayList<String>();
        for (int i = 0; i < filas.size(); i++) {
            String clave = construirClave(filas.get(i));
            if (claves.contains(clave) == false) {
                claves.add(clave);
            }
        }
        return claves;
    }

    private static void escribirResumenDeConfiguracion(ArrayList<String[]> filas,
                                                       String clave, PrintWriter escritor) {
        int cantidad = 0;
        for (int i = 0; i < filas.size(); i++) {
            if (construirClave(filas.get(i)).equals(clave)) {
                cantidad++;
            }
        }

        double[] tiempos = new double[cantidad];
        double[] alturas = new double[cantidad];
        int posicion = 0;
        for (int i = 0; i < filas.size(); i++) {
            String[] fila = filas.get(i);
            if (construirClave(fila).equals(clave)) {
                tiempos[posicion] = Double.parseDouble(fila[8]);
                alturas[posicion] = Double.parseDouble(fila[9]);
                posicion++;
            }
        }

        double promedio = Estadisticas.calcularPromedio(tiempos);
        double desviacion = Estadisticas.calcularDesviacionEstandar(tiempos);
        double mediana = Estadisticas.calcularMediana(tiempos);
        double coeficiente = desviacion / promedio;
        int atipicos = Estadisticas.contarAtipicos(tiempos);
        double[] sinAtipicos = Estadisticas.quitarAtipicos(tiempos);
        double promedioSin = Estadisticas.calcularPromedio(sinAtipicos);
        double desviacionSin = Estadisticas.calcularDesviacionEstandar(sinAtipicos);
        double alturaPromedio = Estadisticas.calcularPromedio(alturas);

        escritor.println(clave + "," + cantidad + ","
                + formatearUnDecimal(promedio) + "," + formatearUnDecimal(desviacion) + ","
                + formatearUnDecimal(mediana) + "," + formatearCuatroDecimales(coeficiente) + ","
                + atipicos + ","
                + formatearUnDecimal(promedioSin) + "," + formatearUnDecimal(desviacionSin) + ","
                + formatearUnDecimal(alturaPromedio));
    }

    private static String formatearUnDecimal(double valor) {
        return String.format(Locale.US, "%.1f", valor);
    }

    private static String formatearCuatroDecimales(double valor) {
        return String.format(Locale.US, "%.4f", valor);
    }
}