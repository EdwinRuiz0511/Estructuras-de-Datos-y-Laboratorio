import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

/**
 * Laboratorio 1 - Escribir una matriz de 100.000 x 100.000 en disco duro.
 *
 * PUNTO CLAVE (lo que pide el profesor): el archivo debe ser AUTODESCRIPTIVO.
 * Sus primeros bytes (el "header") le dicen a cualquier programa que lo
 * abra cuántas filas y columnas tiene la matriz — el programa NUNCA debe
 * asumir el tamaño desde una constante fija en el código. Así, aunque
 * alguien no sepa de antemano cuántas filas tiene el archivo, el propio
 * archivo se lo dice.
 *
 * Estructura del archivo:
 *   [ filas (4 bytes) ] [ columnas (4 bytes) ] [ datos: filas x columnas bytes ]
 *
 * SIN CARGAR TODO A RAM: para leer cualquier celda, fila, columna o chunk,
 * el programa usa RandomAccessFile con seek() — salta directo a la
 * posición exacta en disco y solo trae a memoria los bytes que necesita
 * en ese momento (nunca los 9+ GB completos). Eso es lo que hay que
 * poder demostrarle al profesor: que el tamaño del archivo no importa
 * para consultar una parte pequeña de él.
 */
public class MatrizDisco {

    // Solo se usa para GENERAR el archivo la primera vez. Una vez que el
    // archivo existe, el programa YA NO usa esta constante para leerlo:
    // lee las dimensiones reales desde el propio header del archivo.
    static final int N = 100_000;

    static final String RUTA = "matriz.dat";

    static final int TAMAÑO_HEADER = 8; // 4 bytes filas + 4 bytes columnas

    // Pon esto en false si ya generaste el archivo antes y solo quieres
    // consultarlo (evita esperar de nuevo la escritura completa).
    static final boolean GENERAR_ARCHIVO = true;

    public static void main(String[] args) throws IOException {

        if (GENERAR_ARCHIVO) {
            System.out.println("Generando matriz de " + N + " x " + N + "...");
            long inicio = System.currentTimeMillis();
            escribirMatriz(RUTA, N, N);
            long fin = System.currentTimeMillis();
            System.out.println("Escritura completada en " + (fin - inicio) + " ms");
        }

        // A PARTIR DE AQUÍ el programa ya no usa la constante N: todo lo
        // que sigue se basa en lo que dice el archivo, no en el código.
        int[] dimensiones = leerHeader(RUTA);
        int filas = dimensiones[0];
        int columnas = dimensiones[1];

        System.out.println("El archivo dice tener " + filas + " filas x " + columnas + " columnas.");

        long tamañoBytes = Files.size(Paths.get(RUTA));
        double tamañoGB = tamañoBytes / (1024.0 * 1024.0 * 1024.0);
        System.out.printf("Tamaño del archivo: %d bytes (%.4f GB)%n", tamañoBytes, tamañoGB);

        menuInteractivo(filas, columnas);
    }

    /**
     * Escribe el header (filas, columnas) y luego la matriz completa,
     * fila por fila, sin crear el arreglo completo en memoria.
     */
    public static void escribirMatriz(String ruta, int filas, int columnas) throws IOException {
        byte[] fila = new byte[columnas]; // un solo buffer de una fila, reutilizado
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(ruta), 1 << 20))) {
            out.writeInt(filas);
            out.writeInt(columnas);
            for (int i = 0; i < filas; i++) {
                out.write(fila);
            }
        }
    }

    /**
     * Lee ÚNICAMENTE los primeros 8 bytes del archivo (el header) para
     * saber cuántas filas y columnas tiene. Esto es intencionalmente
     * barato: no toca el resto del archivo, sin importar si pesa 1 KB o
     * 9 GB.
     */
    public static int[] leerHeader(String ruta) throws IOException {
        try (DataInputStream in = new DataInputStream(new FileInputStream(ruta))) {
            int filas = in.readInt();
            int columnas = in.readInt();
            return new int[]{filas, columnas};
        }
    }

    /** Lee y devuelve el valor de una única celda (fila, columna). */
    public static int leerCelda(String ruta, int columnas, int fila, int columna) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
            long posicion = TAMAÑO_HEADER + (long) fila * columnas + columna;
            raf.seek(posicion);
            return raf.readByte();
        }
    }

    /** Lee una fila completa (un solo seek + un solo read: los bytes son contiguos). */
    public static byte[] leerFila(String ruta, int columnas, int fila) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
            long posicion = TAMAÑO_HEADER + (long) fila * columnas;
            raf.seek(posicion);
            byte[] buffer = new byte[columnas];
            raf.readFully(buffer);
            return buffer;
        }
    }

    /** Lee una columna completa (un seek por cada valor: están dispersos en el archivo). */
    public static byte[] leerColumna(String ruta, int filas, int columnas, int columna) throws IOException {
        byte[] resultado = new byte[filas];
        try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
            for (int i = 0; i < filas; i++) {
                long posicion = TAMAÑO_HEADER + (long) i * columnas + columna;
                raf.seek(posicion);
                resultado[i] = raf.readByte();
            }
        }
        return resultado;
    }

    /**
     * Lee un chunk (bloque rectangular) de la matriz, sin cargar el
     * resto del archivo — esto es lo que hay que poder mostrarle al
     * profe que NO carga todo a RAM: solo trae a memoria "filas x cols"
     * bytes, sin importar qué tan grande sea el archivo completo.
     */
    public static byte[][] leerChunk(String ruta, int columnas, int filaInicio, int colInicio,
                                     int filas, int cols) throws IOException {
        byte[][] chunk = new byte[filas][cols];
        try (RandomAccessFile raf = new RandomAccessFile(ruta, "r")) {
            for (int i = 0; i < filas; i++) {
                long posicion = TAMAÑO_HEADER + (long) (filaInicio + i) * columnas + colInicio;
                raf.seek(posicion);
                byte[] buffer = new byte[cols];
                raf.readFully(buffer);
                chunk[i] = buffer;
            }
        }
        return chunk;
    }

    /**
     * Exporta un chunk a un .txt legible, incluyendo la información real
     * del HEADER DEL ARCHIVO (no una constante del código) — así el
     * mismo archivo de texto es prueba de que el programa leyó las
     * dimensiones desde el archivo binario, no que las inventó.
     */
    public static void exportarChunkATexto(String rutaOrigen, byte[][] chunk, String archivoSalida,
                                           int filaInicio, int colInicio) throws IOException {
        int[] dimensiones = leerHeader(rutaOrigen); // vuelve a leer el header, no una constante
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))) {
            writer.write("Archivo: " + rutaOrigen);
            writer.newLine();
            writer.write("Dimensiones leídas del HEADER del archivo: "
                    + dimensiones[0] + " filas x " + dimensiones[1] + " columnas");
            writer.newLine();
            writer.write("Fragmento mostrado: filas [" + filaInicio + ".."
                    + (filaInicio + chunk.length - 1) + "], columnas [" + colInicio + ".."
                    + (colInicio + chunk[0].length - 1) + "]");
            writer.newLine();
            writer.newLine();
            for (byte[] fila : chunk) {
                StringBuilder linea = new StringBuilder();
                for (byte b : fila) {
                    linea.append(b).append(" ");
                }
                writer.write(linea.toString());
                writer.newLine();
            }
        }
        System.out.println("Archivo de texto generado: " + archivoSalida
                + " (ábrelo con el Bloc de notas)");
    }

    /** Muestra un chunk en la consola, con forma de tabla. */
    public static void mostrarChunkEnConsola(byte[][] chunk) {
        for (byte[] fila : chunk) {
            for (byte b : fila) {
                System.out.print(b + " ");
            }
            System.out.println();
        }
    }

    public static void menuInteractivo(int filas, int columnas) throws IOException {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            System.out.println("\n--- ¿Qué parte de la matriz quieres ver? ---");
            System.out.println("1. Una celda (fila, columna)");
            System.out.println("2. Una fila completa");
            System.out.println("3. Una columna completa");
            System.out.println("4. Un chunk (bloque de filas x columnas) en consola");
            System.out.println("5. Exportar un chunk a un .txt (para abrir en el Bloc de notas)");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = Integer.parseInt(sc.nextLine().trim());

            switch (opcion) {
                case 1 -> {
                    System.out.print("Fila (0 a " + (filas - 1) + "): ");
                    int f = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Columna (0 a " + (columnas - 1) + "): ");
                    int c = Integer.parseInt(sc.nextLine().trim());
                    int valor = leerCelda(RUTA, columnas, f, c);
                    System.out.println("Valor en (" + f + ", " + c + ") = " + valor);
                }
                case 2 -> {
                    System.out.print("Número de fila (0 a " + (filas - 1) + "): ");
                    int f = Integer.parseInt(sc.nextLine().trim());
                    byte[] fila = leerFila(RUTA, columnas, f);
                    System.out.println("Primeros 20 valores: " + primerosValores(fila, 20));
                }
                case 3 -> {
                    System.out.print("Número de columna (0 a " + (columnas - 1) + "): ");
                    int c = Integer.parseInt(sc.nextLine().trim());
                    byte[] columna = leerColumna(RUTA, filas, columnas, c);
                    System.out.println("Primeros 20 valores: " + primerosValores(columna, 20));
                }
                case 4 -> {
                    System.out.print("Fila inicial: ");
                    int fi = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Columna inicial: ");
                    int ci = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Cuántas filas: ");
                    int nf = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Cuántas columnas: ");
                    int nc = Integer.parseInt(sc.nextLine().trim());
                    byte[][] chunk = leerChunk(RUTA, columnas, fi, ci, nf, nc);
                    mostrarChunkEnConsola(chunk);
                }
                case 5 -> {
                    System.out.print("Fila inicial: ");
                    int fi = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Columna inicial: ");
                    int ci = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Cuántas filas: ");
                    int nf = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Cuántas columnas: ");
                    int nc = Integer.parseInt(sc.nextLine().trim());
                    byte[][] chunk = leerChunk(RUTA, columnas, fi, ci, nf, nc);
                    exportarChunkATexto(RUTA, chunk, "matriz_fragmento.txt", fi, ci);
                }
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.");
            }
        }
        sc.close();
    }

    /** Devuelve una representación en texto de los primeros k valores de un arreglo. */
    public static String primerosValores(byte[] datos, int k) {
        StringBuilder sb = new StringBuilder();
        int limite = Math.min(k, datos.length);
        for (int i = 0; i < limite; i++) {
            sb.append(datos[i]).append(" ");
        }
        sb.append("...");
        return sb.toString();
    }
}