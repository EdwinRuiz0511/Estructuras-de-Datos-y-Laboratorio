# Laboratorio 3: Sistema de Búsqueda con Lista, ABB y B+

## Descripción

Comparación experimental de tres estructuras de datos (Lista, Árbol Binario de Búsqueda y Árbol B+) para la búsqueda de estudiantes por su ID. Se midió el tiempo de búsqueda con distintos tamaños de datos (N) y se analizó cómo afecta el orden de inserción.

---

## Estructura del proyecto

```text
Laboratorio-3-Sistema-Busqueda/
├── datos/
│   └── resultados/
│       ├── resultados_busqueda.csv     (1.050 mediciones crudas)
│       └── resumen_busqueda.csv        (35 filas resumidas)
├── graficas/
│   ├── grafica_1_tiempo_vs_n.png
│   ├── grafica_2_abb_comparacion.png
│   ├── grafica_3_altura_vs_n.png
│   └── grafica_4_altura_vs_tiempo.png
├── scripts/
│   └── graficar.py                     (genera las gráficas)
├── src/
│   ├── abb/                             (Árbol Binario de Búsqueda)
│   ├── bmas/                            (Árbol B+)
│   ├── experimento/                     (generadores, medidor, estadísticas)
│   ├── lista/                           (ListaEstudiantes)
│   ├── modelo/                          (Estudiante)
│   ├── Principal.java                   (experimento de búsqueda)
│   └── PrincipalResumen.java            (análisis estadístico)
├── README.md
└── CODIGO_DE_HONOR.md
```

---

## Metodología

### Configuración del experimento
- **N** (cantidad de estudiantes): 100, 500, 1.000, 5.000, 10.000, 50.000, 100.000
- **M** (búsquedas por repetición): 1.000
- **Repeticiones medidas:** 30 por configuración
- **Repeticiones de calentamiento:** 3 (descartadas)
- **Semilla base:** 1000 (cada repetición usa `base + r`)
- **Capacidad del B+:** 32 IDs por nodo

### Configuraciones comparadas (5)
1. **Lista** (inserción aleatoria)
2. **ABB** (inserción aleatoria)
3. **ABB** (inserción ordenada)
4. **B+** (inserción aleatoria)
5. **B+** (inserción ordenada)

### Qué se mide
- **Solo las M búsquedas** (no la construcción, no la impresión, no el guardado).
- Se usa `System.nanoTime()`.
- Se reporta el tiempo total de las M búsquedas en nanosegundos.

### Calentamiento
- 3 repeticiones de calentamiento antes de cada configuración (para que el JIT de Java optimice el código).
- **Motivo:** las primeras ejecuciones de Java son lentas porque el código está interpretado.

---

## Hardware y software

### Hardware
- **Procesador:** Intel(R) Core(TM) i3-1005G1 (2 núcleos, 4 hilos)
- **Frecuencia base:** 1.20 GHz
- **RAM:** 8 GB
- **Disco:** SSD
- **Sistema operativo:** Windows 10 (versión 10.0.19044.1288)

### Software
- **Java:** JDK 24.0.1
- **Python:** 3.14.8
- **Matplotlib:** 3.11.2
- **IntelliJ IDEA:** Community Edition 2025.2
- **VS Code:** Última versión

### Condiciones de las mediciones
- Cargador conectado.
- Plan de energía en "Mejor rendimiento".
- Programas innecesarios cerrados (navegador, WhatsApp, etc.).
- No se usó el computador mientras corrían las mediciones.

---

## Resultados

### Tabla resumen (N = 100.000, tiempo en microsegundos por búsqueda)

| Estructura | Tipo de inserción | Tiempo | Altura | Complejidad |
|-----------|-------------------|--------|--------|-------------|
| Lista | aleatoria | ~170 µs | 0 | O(N) |
| ABB | aleatoria | ~0.85 µs | 36-46 | O(log N) |
| ABB | ordenada | ~230 µs | 100.000 | O(N) |
| B+ | aleatoria | ~0.18 µs | 4 | O(log N) |
| B+ | ordenada | ~0.19 µs | 4 | O(log N) |

### Gráficas

1. **Gráfica 1:** Tiempo de búsqueda vs N (5 configuraciones).
2. **Gráfica 2:** Efecto del orden de inserción en el ABB.
3. **Gráfica 3:** Altura vs N (ABB aleatorio, ABB ordenado, B+, log₂N teórico).
4. **Gráfica 4:** Relación entre altura y tiempo (ABB aleatorio, N=100.000).

---

## Conclusiones

1. **El B+ es la estructura más eficiente.** Con N=100.000, tarda ~0.18 µs por búsqueda (1000 veces más rápido que la lista).

2. **El orden de inserción destruye al ABB.** Con IDs ordenados, el ABB se degenera en cadena (altura = N) y su tiempo pasa de 0.85 µs a ~230 µs (~270 veces más lento).

3. **El B+ no se afecta por el orden de inserción.** Queda balanceado en ambos casos (altura 4).

4. **La lista es lenta para búsquedas.** O(N) significa recorrer toda la lista en el peor caso.

5. **La altura explica el tiempo.** A mayor altura, mayor tiempo de búsqueda. El B+ tiene altura ~4, el ABB aleatorio ~40, el ABB ordenado = N.

6. **Se cumplen las predicciones teóricas:**
   - Lista: O(N).
   - ABB aleatorio: O(log N).
   - ABB ordenado: O(N) (degenerado).
   - B+: O(log N) con altura muy pequeña.

---

## Limitaciones

1. **Calentamiento de Java (JIT):** a pesar del calentamiento, el JIT puede seguir optimizando durante la corrida. Se ve en las primeras repeticiones más lentas.

2. **Cambios de nivel:** en algunas configuraciones (especialmente N pequeño), se observan dos "regímenes" de tiempo. Probablemente por el calentamiento insuficiente con N pequeño.

3. **Ruido de la máquina:** otros procesos (antivirus, actualizaciones, etc.) pueden afectar los tiempos. Se mitigó con 30 repeticiones y detección de atípicos (regla IQR).

4. **Una sola máquina:** los resultados son específicos de este hardware. Otras máquinas pueden dar tiempos distintos (pero las tendencias deberían ser similares).

---

## Cómo reproducir el experimento

### Requisitos
- Java 24
- Python 3.14.8 con Matplotlib
- IntelliJ IDEA o VS Code

### Pasos

1. **Clonar el repositorio:**

```bash
git clone https://github.com/EdwinRuiz0511/Estructuras-de-Datos-y-Laboratorio.git
```

2. **Ejecutar el experimento (Java):**
   - Abrir el proyecto en IntelliJ.
   - Ejecutar `Principal.java`.
   - Esperar ~16 minutos.
   - Se genera `datos/resultados/resultados_busqueda.csv`.

3. **Generar el resumen estadístico (Java):**
   - Ejecutar `PrincipalResumen.java`.
   - Se genera `datos/resultados/resumen_busqueda.csv`.

4. **Generar las gráficas (Python):**

```bash
python scripts/graficar.py
```

   - Se generan los 4 PNG en `graficas/`.

---

## Referencias

- Documentación de Java: https://docs.oracle.com/javase/
- Documentación de Matplotlib: https://matplotlib.org/
- Árbol B+: https://en.wikipedia.org/wiki/B%2B_tree

---

## Autor

**Edwin Ruiz**
- Curso: Estructuras de Datos y Laboratorio
- Fecha: Octubre 2026
- GitHub: [@EdwinRuiz0511](https://github.com/EdwinRuiz0511)

---

## Licencia

Este proyecto es parte de un trabajo académico. Uso educativo.