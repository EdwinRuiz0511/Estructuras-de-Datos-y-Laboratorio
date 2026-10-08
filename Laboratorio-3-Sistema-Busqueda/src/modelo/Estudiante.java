package modelo;

public class Estudiante {

    // Atributos privados: solo se pueden leer con los getters.
    // "final" significa que se asignan una sola vez, en el constructor.
    private final int id;
    private final String nombre;
    private final int edad;
    private final double promedio;

    // Constructor: recibe los cuatro datos y los guarda.
    public Estudiante(int id, String nombre, int edad, double promedio) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.promedio = promedio;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public double getPromedio() {
        return promedio;
    }

    // Devuelve el estudiante como texto, útil para imprimirlo.
    public String toString() {
        return "Estudiante: [id=" + id + ", nombre=" + nombre
                + ", edad=" + edad + ", promedio=" + promedio + "]";
    }
}