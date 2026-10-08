package experimento;

import abb.ArbolABB;
import bmas.ArbolBMas;
import lista.ListaEstudiantes;
import modelo.Estudiante;

public class MedidorTiempo {

    private long instanteInicio;

    // Cuántos IDs se encontraron en la última medición (debe ser igual a M).
    private int encontradosUltimaMedicion;

    public void iniciarCronometro() {
        instanteInicio = System.nanoTime();
    }

    // Devuelve los nanosegundos transcurridos desde iniciarCronometro().
    public long detenerCronometro() {
        long instanteFin = System.nanoTime();
        return instanteFin - instanteInicio;
    }

    public int getEncontradosUltimaMedicion() {
        return encontradosUltimaMedicion;
    }

    // Mide el tiempo total de buscar todos los IDs en la lista.
    // Solo se cronometran las búsquedas, nada más.
    public long medirBusquedasLista(ListaEstudiantes lista, int[] idsBusqueda) {
        int encontrados = 0;
        iniciarCronometro();
        for (int i = 0; i < idsBusqueda.length; i++) {
            Estudiante resultado = lista.buscar(idsBusqueda[i]);
            if (resultado != null) {
                encontrados++;
            }
        }
        long tiempo = detenerCronometro();
        encontradosUltimaMedicion = encontrados;
        return tiempo;
    }

    public long medirBusquedasABB(ArbolABB arbol, int[] idsBusqueda) {
        int encontrados = 0;
        iniciarCronometro();
        for (int i = 0; i < idsBusqueda.length; i++) {
            Estudiante resultado = arbol.buscar(idsBusqueda[i]);
            if (resultado != null) {
                encontrados++;
            }
        }
        long tiempo = detenerCronometro();
        encontradosUltimaMedicion = encontrados;
        return tiempo;
    }

    public long medirBusquedasBMas(ArbolBMas arbol, int[] idsBusqueda) {
        int encontrados = 0;
        iniciarCronometro();
        for (int i = 0; i < idsBusqueda.length; i++) {
            Estudiante resultado = arbol.buscar(idsBusqueda[i]);
            if (resultado != null) {
                encontrados++;
            }
        }
        long tiempo = detenerCronometro();
        encontradosUltimaMedicion = encontrados;
        return tiempo;
    }
}