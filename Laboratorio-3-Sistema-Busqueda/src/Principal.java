import experimento.Estadisticas;

public class Principal {
    public static void main(String[] args) {

        // Prueba 1: el ejemplo de la tabla.
        double[] ejemplo = {2, 4, 4, 4, 5, 5, 7, 9};
        System.out.println("Prueba 1");
        System.out.println("Promedio: " + Estadisticas.calcularPromedio(ejemplo));
        System.out.println("Desviacion estandar: " + Estadisticas.calcularDesviacionEstandar(ejemplo));
        System.out.println("Mediana: " + Estadisticas.calcularMediana(ejemplo));
        System.out.println("Q1: " + Estadisticas.calcularCuartil1(ejemplo));
        System.out.println("Q3: " + Estadisticas.calcularCuartil3(ejemplo));
        System.out.println("Atipicos: " + Estadisticas.contarAtipicos(ejemplo));

        // Prueba 2: un valor muy distinto de los demas.
        double[] conAtipico = {10, 11, 12, 13, 14, 15, 16, 17, 100};
        System.out.println("Prueba 2");
        System.out.println("Promedio con todos: " + Estadisticas.calcularPromedio(conAtipico));
        System.out.println("Desviacion con todos: " + Estadisticas.calcularDesviacionEstandar(conAtipico));
        System.out.println("Mediana: " + Estadisticas.calcularMediana(conAtipico));
        System.out.println("Q1: " + Estadisticas.calcularCuartil1(conAtipico));
        System.out.println("Q3: " + Estadisticas.calcularCuartil3(conAtipico));
        System.out.println("Atipicos: " + Estadisticas.contarAtipicos(conAtipico));

        double[] sinAtipicos = Estadisticas.quitarAtipicos(conAtipico);
        System.out.println("Valores restantes: " + sinAtipicos.length);
        System.out.println("Promedio sin atipicos: " + Estadisticas.calcularPromedio(sinAtipicos));
        System.out.println("Desviacion sin atipicos: " + Estadisticas.calcularDesviacionEstandar(sinAtipicos));
        System.out.println("Original intacto, longitud: " + conAtipico.length);
    }
}