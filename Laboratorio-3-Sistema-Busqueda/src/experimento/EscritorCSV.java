package experimento;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class EscritorCSV {

    private PrintWriter escritor;

    public EscritorCSV(String rutaArchivo) throws IOException {
        escritor = new PrintWriter(new FileWriter(rutaArchivo));
        escritor.println("operacion,estructura,tipoInsercion,n,m,capacidad,"
                + "repeticion,semilla,tiempoNanosegundos,altura,encontrados");
        escritor.flush();
    }

    public void escribirFila(ResultadoExperimento resultado) {
        escritor.println(resultado.aLineaCSV());
    }

    public void guardar() {
        escritor.flush();
    }

    public void cerrar() {
        escritor.close();
    }
}