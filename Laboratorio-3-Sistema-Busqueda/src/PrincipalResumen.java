import experimento.GeneradorResumen;
import java.io.IOException;

public class PrincipalResumen {
    public static void main(String[] args) throws IOException {
        String rutaEntrada = "datos/resultados/resultados_busqueda.csv";
        String rutaSalida = "datos/resultados/resumen_busqueda.csv";
        GeneradorResumen.generar(rutaEntrada, rutaSalida);
        System.out.println("Resumen generado en: " + rutaSalida);
    }
}