import csv
import math
import matplotlib.pyplot as plt

# ============================================
# CONFIGURACIÓN
# ============================================

RUTA_RESUMEN = "datos/resultados/resumen_busqueda.csv"
RUTA_CRUDO = "datos/resultados/resultados_busqueda.csv"
CARPETA_GRAFICAS = "graficas/"

# ============================================
# LECTURA DE CSV
# ============================================

def leer_resumen():
    """Lee el CSV resumen y devuelve una lista de diccionarios."""
    filas = []
    with open(RUTA_RESUMEN, "r", encoding="utf-8") as archivo:
        lector = csv.DictReader(archivo)
        for fila in lector:
            filas.append(fila)
    return filas

def leer_crudo():
    """Lee el CSV crudo y devuelve una lista de diccionarios."""
    filas = []
    with open(RUTA_CRUDO, "r", encoding="utf-8") as archivo:
        lector = csv.DictReader(archivo)
        for fila in lector:
            filas.append(fila)
    return filas

# ============================================
# FUNCIÓN AUXILIAR
# ============================================

def extraer_datos(filas, estructura, tipoInsercion):
    """
    Devuelve tres listas (ns, promediosUs, desviacionesUs) para la
    configuración dada. Los tiempos se convierten a microsegundos
    por búsqueda.
    """
    ns = []
    promedios = []
    desviaciones = []
    
    for fila in filas:
        if fila["estructura"] == estructura and fila["tipoInsercion"] == tipoInsercion:
            n = int(fila["n"])
            m = int(fila["m"])
            promedioNs = float(fila["promedioNs"])
            desviacionNs = float(fila["desviacionNs"])
            
            promedioUs = promedioNs / m / 1000
            desviacionUs = desviacionNs / m / 1000
            
            ns.append(n)
            promedios.append(promedioUs)
            desviaciones.append(desviacionUs)
    
    datos = sorted(zip(ns, promedios, desviaciones))
    ns = [d[0] for d in datos]
    promedios = [d[1] for d in datos]
    desviaciones = [d[2] for d in datos]
    
    return ns, promedios, desviaciones

# ============================================
# GRÁFICA 1: Tiempo por búsqueda vs N
# ============================================

def grafica_1_tiempo_vs_n(filas):
    configuraciones = [
        ("Lista", "aleatoria", "blue", "o", "-"),
        ("ABB", "aleatoria", "green", "s", "-"),
        ("ABB", "ordenada", "red", "^", "-"),
        ("B+", "aleatoria", "purple", "D", "-"),
        ("B+", "ordenada", "orange", "v", "-")
    ]
    
    plt.figure(figsize=(10, 6))
    
    for estructura, tipoInsercion, color, marcador, linea in configuraciones:
        ns, promedios, desviaciones = extraer_datos(filas, estructura, tipoInsercion)
        etiqueta = f"{estructura} ({tipoInsercion})"
        plt.errorbar(ns, promedios, yerr=desviaciones, 
                     label=etiqueta, color=color, marker=marcador, 
                     linestyle=linea, capsize=4, markersize=6)
    
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("N (cantidad de estudiantes)")
    plt.ylabel("Tiempo por búsqueda (microsegundos)")
    plt.title("Tiempo de búsqueda vs N\n(5 configuraciones, ejes logarítmicos)")
    plt.legend()
    plt.grid(True, which="both", alpha=0.3)
    plt.tight_layout()
    
    ruta = CARPETA_GRAFICAS + "grafica_1_tiempo_vs_n.png"
    plt.savefig(ruta, dpi=100)
    plt.close()
    print(f"Gráfica guardada: {ruta}")

# ============================================
# GRÁFICA 2: ABB aleatorio vs ABB ordenado
# ============================================

def grafica_2_abb_comparacion(filas):
    configuraciones = [
        ("ABB", "aleatoria", "green", "s", "-"),
        ("ABB", "ordenada", "red", "^", "-")
    ]
    
    plt.figure(figsize=(10, 6))
    
    for estructura, tipoInsercion, color, marcador, linea in configuraciones:
        ns, promedios, desviaciones = extraer_datos(filas, estructura, tipoInsercion)
        etiqueta = f"ABB ({tipoInsercion})"
        plt.errorbar(ns, promedios, yerr=desviaciones, 
                     label=etiqueta, color=color, marker=marcador, 
                     linestyle=linea, capsize=4, markersize=6)
    
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("N (cantidad de estudiantes)")
    plt.ylabel("Tiempo por búsqueda (microsegundos)")
    plt.title("Efecto del orden de inserción en el ABB\n(aleatorio vs ordenado)")
    plt.legend()
    plt.grid(True, which="both", alpha=0.3)
    plt.tight_layout()
    
    ruta = CARPETA_GRAFICAS + "grafica_2_abb_comparacion.png"
    plt.savefig(ruta, dpi=100)
    plt.close()
    print(f"Gráfica guardada: {ruta}")

# ============================================
# GRÁFICA 3: Altura vs N
# ============================================

def grafica_3_altura_vs_n(filas):
    plt.figure(figsize=(10, 6))
    
    # ABB aleatorio
    ns_abb_ale, _, _ = extraer_datos(filas, "ABB", "aleatoria")
    alturas_abb_ale = []
    for n in ns_abb_ale:
        for fila in filas:
            if (fila["estructura"] == "ABB" and fila["tipoInsercion"] == "aleatoria" 
                    and int(fila["n"]) == n):
                alturas_abb_ale.append(float(fila["alturaPromedio"]))
                break
    plt.plot(ns_abb_ale, alturas_abb_ale, "g-s", label="ABB (aleatoria)", markersize=6)
    
    # ABB ordenado
    ns_abb_ord, _, _ = extraer_datos(filas, "ABB", "ordenada")
    alturas_abb_ord = []
    for n in ns_abb_ord:
        for fila in filas:
            if (fila["estructura"] == "ABB" and fila["tipoInsercion"] == "ordenada" 
                    and int(fila["n"]) == n):
                alturas_abb_ord.append(float(fila["alturaPromedio"]))
                break
    plt.plot(ns_abb_ord, alturas_abb_ord, "r-^", label="ABB (ordenada)", markersize=6)
    
    # B+ aleatorio
    ns_bmas, _, _ = extraer_datos(filas, "B+", "aleatoria")
    alturas_bmas = []
    for n in ns_bmas:
        for fila in filas:
            if (fila["estructura"] == "B+" and fila["tipoInsercion"] == "aleatoria" 
                    and int(fila["n"]) == n):
                alturas_bmas.append(float(fila["alturaPromedio"]))
                break
    plt.plot(ns_bmas, alturas_bmas, "D-", color="purple", label="B+ (aleatoria)", markersize=6)
    
    # Curva teórica log2(N)
    ns_teoricos = []
    log_ns = []
    n = 100
    while n <= 100000:
        ns_teoricos.append(n)
        log_ns.append(math.log2(n))
        n = n * 2
    plt.plot(ns_teoricos, log_ns, "k--", label="log₂(N) teórico", linewidth=1.5)
    
    plt.xscale("log")
    plt.yscale("log")
    plt.xlabel("N (cantidad de estudiantes)")
    plt.ylabel("Altura (niveles)")
    plt.title("Altura de las estructuras vs N\n(ABB aleatorio, ABB ordenado, B+, y teoría log₂N)")
    plt.legend()
    plt.grid(True, which="both", alpha=0.3)
    plt.tight_layout()
    
    ruta = CARPETA_GRAFICAS + "grafica_3_altura_vs_n.png"
    plt.savefig(ruta, dpi=100)
    plt.close()
    print(f"Gráfica guardada: {ruta}")

# ============================================
# GRÁFICA 4: Altura vs tiempo (ABB aleatorio, N=100000)
# ============================================

def grafica_4_altura_vs_tiempo(crudo):
    """
    Para el ABB aleatorio con N=100.000, muestra los 30 puntos
    (altura, tiempo por búsqueda). Cada punto es una repetición.
    """
    alturas = []
    tiempos = []
    
    for fila in crudo:
        if (fila["estructura"] == "ABB" 
                and fila["tipoInsercion"] == "aleatoria" 
                and int(fila["n"]) == 100000):
            altura = int(fila["altura"])
            tiempoNs = int(fila["tiempoNanosegundos"])
            m = int(fila["m"])
            tiempoUs = tiempoNs / m / 1000
            alturas.append(altura)
            tiempos.append(tiempoUs)
    
    plt.figure(figsize=(10, 6))
    plt.scatter(alturas, tiempos, color="green", s=60, alpha=0.7, edgecolors="black")
    
    plt.xlabel("Altura del árbol (niveles)")
    plt.ylabel("Tiempo por búsqueda (microsegundos)")
    plt.title("Relación entre altura y tiempo de búsqueda\n(ABB aleatorio, N = 100.000, 30 repeticiones)")
    plt.grid(True, alpha=0.3)
    plt.tight_layout()
    
    ruta = CARPETA_GRAFICAS + "grafica_4_altura_vs_tiempo.png"
    plt.savefig(ruta, dpi=100)
    plt.close()
    print(f"Gráfica guardada: {ruta}")
    print(f"  Puntos graficados: {len(alturas)}")

# ============================================
# PROGRAMA PRINCIPAL
# ============================================

def main():
    print("Leyendo CSV resumen...")
    filas = leer_resumen()
    print(f"Filas del resumen: {len(filas)}")
    
    print("Leyendo CSV crudo...")
    crudo = leer_crudo()
    print(f"Filas del crudo: {len(crudo)}")
    
    print("Generando gráfica 1...")
    grafica_1_tiempo_vs_n(filas)
    
    print("Generando gráfica 2...")
    grafica_2_abb_comparacion(filas)
    
    print("Generando gráfica 3...")
    grafica_3_altura_vs_n(filas)
    
    print("Generando gráfica 4...")
    grafica_4_altura_vs_tiempo(crudo)
    
    print("¡Listo!")

if __name__ == "__main__":
    main()