# Laboratorio 1 - Matriz de 100.000 x 100.000 en disco

**Estudiante:** Edwin A. Ruiz Ocampo

**Curso:** Estructuras de Datos y Lab

**Universidad de Antioquia**

## Descripción del problema

El laboratorio pide escribir una matriz de 100.000 x 100.000 (10.000.000.000
de celdas) en disco duro, y entregar una forma de "mostrar" la matriz creada.

Una matriz de ese tamaño **no cabe en memoria RAM** (a 1 byte por celda,
pesa ~9.3 GB) y tampoco se puede imprimir completa en pantalla. Por eso la
solución consiste en:

1. Escribirla en disco de forma eficiente, sin crear el arreglo completo en memoria.
2. Ofrecer una forma de consultar cualquier parte de la matriz (celda, fila,
   columna, o bloque) leyendo directamente del archivo en disco.
3. Generar evidencia verificable de que el archivo existe y que el programa
   lee su forma (filas y columnas) del propio archivo, no de una constante.

## Archivos de esta carpeta

- **`src/MatrizDisco.java`**: contiene toda la lógica del laboratorio. Cada
  método tiene comentarios explicando qué hace y por qué.
- **`matriz.dat`** (NO incluido en el repositorio, ~9.3 GB): el archivo
  binario con la matriz, generado al ejecutar el programa.
- **`matriz_fragmento.txt`** (NO incluido en el repositorio): evidencia
  legible en texto plano, generada al usar la opción 5 del menú.

## Diseño de la solución

El archivo `matriz.dat` tiene un header de 8 bytes al inicio (4 para filas,
4 para columnas), seguido de los datos. Es autodescriptivo: el programa lee
las dimensiones del propio archivo, nunca de una constante fija en el código.

Nunca se carga la matriz completa a RAM: la escritura reutiliza un buffer de
una sola fila, y la lectura usa `RandomAccessFile` con `seek()` para saltar
directo a la posición exacta que se necesita.

### Diferencia entre leer una fila y leer una columna

- Una **fila** ocupa bytes contiguos en el archivo → se lee con un solo
  `seek` + un solo `read`.
- Una **columna** tiene sus valores dispersos (separados cada `columnas`
  bytes) → se necesita un `seek` individual por cada valor.

## Cómo compilar y ejecutar

Desde la carpeta `src/`:

```bash
javac MatrizDisco.java
java MatrizDisco
```

El código trae `N = 1_000` por defecto para pruebas rápidas. Para el
tamaño real del laboratorio, cambia `static final int N = 1_000;` a
`static final int N = 100_000;` (requiere ~10 GB libres en disco).

## Cómo verificar el contenido del archivo generado

El programa ofrece un menú interactivo con estas opciones:

| Opción | Qué hace |
|---|---|
| 1 | Consulta el valor de una celda específica |
| 2 | Muestra los primeros valores de una fila completa |
| 3 | Muestra los primeros valores de una columna completa |
| 4 | Muestra un bloque rectangular (chunk) en la consola |
| 5 | Exporta un chunk a `matriz_fragmento.txt`, abrible con el Bloc de notas |

**La opción 5 es la evidencia principal**, porque genera un archivo de
texto independiente de la ejecución del programa. Su contenido incluye
la línea `Dimensiones leídas del HEADER del archivo: ...`, que prueba
que el programa leyó el tamaño real desde el archivo binario, no de una
constante del código.
