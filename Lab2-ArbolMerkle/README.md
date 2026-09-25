# Laboratorio 2 — Árbol de Merkle

Implementación en Java de un **Árbol de Merkle** para el curso de Estructura de Datos.

## Especificación implementada

- Cada **hoja** contiene el hash **SHA-256** de un bloque de datos (una transacción).
- Cada **nodo interno** contiene el hash SHA-256 de la **concatenación** de los hashes de sus dos hijos.
- Si el número de nodos en un nivel es **impar**, el último se **duplica** para poder seguir emparejando.
- La **raíz (Merkle Root)** es el hash que representa todo el conjunto de datos.
- Se implementa además la **generación y verificación de pruebas de inclusión** (Merkle Proof), que permiten demostrar que una transacción pertenece al árbol sin necesidad de recorrerlo completo.

## Estructura del proyecto

```
merkle-lab/
├── src/
│   ├── MerkleNode.java      # Nodo del árbol (hoja o interno)
│   ├── ProofElement.java    # Elemento de una prueba de inclusión
│   ├── MerkleTree.java      # Lógica de construcción, raíz y pruebas
│   └── Main.java            # Experimento completo (punto de entrada)
├── salida_ejemplo.txt       # Ejemplo de salida por consola
└── README.md
```

## Cómo compilar y ejecutar

Requiere JDK 11 o superior.

```bash
cd src
javac *.java
java Main
```

## Diagrama del árbol construido

Con las 5 transacciones de ejemplo (número impar → se duplica la última hoja en el primer nivel de combinación), el árbol queda así:

```
                              RAÍZ
                          [d30601cd...]
                         /              \
               [4bfdaa86...]         [744f4667...]
               /          \           /          \
      [26a966fc...] [3cde3d99...] [0cca3daf...]  (duplicado)
        /      \        /      \        /      \
   H(TX1)   H(TX2)  H(TX3)  H(TX4)  H(TX5)   H(TX5) <- se repite

Hojas:
 H(TX1)=3b4dda4f...   H(TX2)=b289ad2a...   H(TX3)=b439609c...
 H(TX4)=41a4381b...   H(TX5)=c13b9303...
```

Como el número de transacciones (5) es impar, en el primer nivel de agrupación
`TX5` no tiene pareja, así que se **duplica** (`H(TX5)` se combina consigo mismo)
para formar el tercer nodo del nivel 1.

## Experimento realizado (`Main.java`)

1. **Creación de 5 transacciones simuladas** (strings con remitente, destinatario y monto).
2. **Construcción del árbol** y despliegue del diagrama + Merkle Root.
3. **Modificación de una transacción** (TX3) y reconstrucción del árbol:
   se demuestra que la Merkle Root cambia por completo, aunque solo se alteró un dato.
4. **Generación de la prueba de inclusión** para la transacción 3 (los hashes hermanos
   necesarios para recalcular la raíz desde esa hoja).
5. **Verificación con el dato correcto** de TX3 → la prueba es **válida**.
6. **Verificación con un dato incorrecto** (TX3 alterada) usando la misma prueba →
   la verificación **falla**, demostrando que el árbol detecta cualquier manipulación.

Salida real de una ejecución: ver [`salida_ejemplo.txt`](./salida_ejemplo.txt).

## Capturas de pantalla

<img width="796" height="125" alt="image" src="https://github.com/user-attachments/assets/4b909d36-cacd-4fb9-9e6b-d02267de2f1c" />

>
> ![Verificación válida](./capturas/verificacion-valida.png)
> ![Verificación inválida](./capturas/verificacion-invalida.png)

## Notas de diseño

- El hash de un nodo interno se calcula como `SHA256(hash_izquierdo + hash_derecho)`,
  concatenando las representaciones hexadecimales de los hijos.
- La prueba de inclusión (`getProof`) recorre el árbol desde la hoja hasta la raíz,
  guardando en cada nivel el hash del **hermano** y si ese hermano queda a la
  izquierda o a la derecha al concatenar. Esto permite verificar sin tener el árbol completo.
- `verifyProof` es un método **estático** que solo necesita: el dato original,
  la prueba y la raíz esperada — igual a como funcionaría un verificador real
  (por ejemplo, un nodo ligero en una blockchain) sin descargar todos los datos.
