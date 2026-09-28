# CyberLogAnalyzer

Proyecto académico de 2.º de DAM para analizar registros de acceso y practicar detección de patrones sospechosos en Java. La versión actual funciona por consola con datos ficticios.

## Funcionalidades actuales

- Leer eventos desde un archivo CSV.
- Mostrar los eventos y su número total.
- Contar los accesos fallidos por dirección IP.
- Generar una alerta cuando una misma IP acumula al menos cinco fallos en un intervalo de cinco minutos.
- Mostrar una sola alerta por IP durante cada ejecución.

La detección utiliza reglas, no inteligencia artificial. Una alerta señala un patrón para revisar; no confirma que haya ocurrido un ataque.

## Tecnologías

Java 24, Maven, IntelliJ IDEA, colecciones `ArrayList` y `HashMap`, lectura con `File` y `Scanner`, y fechas con `LocalDateTime`.

## Ejecutar en IntelliJ IDEA

1. Clona este repositorio o descarga el código.
2. Abre la carpeta del proyecto y carga su configuración Maven (`pom.xml`).
3. Selecciona Java 24 como SDK del proyecto.
4. Configura el directorio de trabajo de la ejecución en la raíz del proyecto.
5. Ejecuta `com.pablocc07.cyberlog.Main`.

El lector busca `src/main/resources/eventos.csv` mediante una ruta relativa. El programa necesita esa carpeta de datos en el directorio de trabajo; todavía no está preparado para ejecutarse como un JAR independiente.

## Organización del código

| Clase | Responsabilidad |
| --- | --- |
| `EventoAcceso` | Guarda la fecha, el usuario, la IP y el resultado de un intento. |
| `AnalizadorAccesos` | Agrupa `leer()`, `contarFallos()` y `detectarAlertas()`. |
| `Main` | Llama a los métodos del analizador y muestra los resultados. |

Los métodos se agrupan en `AnalizadorAccesos` para facilitar el aprendizaje y la lectura del proyecto.

## Regla de detección

Cada evento fallido puede iniciar un intervalo de cinco minutos. Se recorre la lista para contar los fallos de esa IP dentro del intervalo, incluyendo el inicio y el final. Si el contador alcanza cinco, se genera una alerta y se recuerda la IP para no repetirla.

Los accesos correctos no suman ni reinician el contador. Los eventos pueden estar desordenados; se muestra el primer intervalo que cumple la regla según el recorrido del archivo, que no tiene por qué ser el primero cronológicamente.

## Ejemplo incluido

El CSV contiene 15 eventos: dos accesos correctos y trece fallidos.

| IP | Fallos totales | Resultado |
| --- | ---: | --- |
| `198.51.100.23` | 2 | Sin alerta |
| `192.0.2.42` | 1 | Sin alerta |
| `203.0.113.50` | 5 | Alerta: fallos entre las 10:00 y las 10:04 |
| `192.0.2.80` | 5 | Sin alerta: fallos separados por seis minutos |

La salida incluye:

```text
Total de eventos: 15
Total de alertas: 1
ALERTA | IP: 203.0.113.50 | Fallos: 5 | Intervalo: 2026-09-24T10:00 - 2026-09-24T10:05
```

El orden del resumen por IP puede variar porque se utiliza `HashMap`. Consulta el [README de los datos](src/main/resources/README.md) para conocer el formato y los escenarios.

## Limitaciones actuales

- El lector espera cuatro campos válidos, sin líneas vacías, espacios adicionales ni comas dentro de los campos.
- Una fecha incorrecta o una fila incompleta puede interrumpir la ejecución. Cualquier resultado distinto de `EXITO` se interpreta como fallo.
- Si el archivo no existe, se muestra un mensaje y se devuelve una lista vacía.
- Solo se informa de una alerta por IP, aunque existan varios intervalos sospechosos.
- La búsqueda mediante dos bucles tiene un coste cuadrático en el peor caso; conviene revisarla antes de analizar volúmenes grandes.
- Todavía no hay interfaz gráfica, base de datos ni pruebas automatizadas incorporadas al repositorio.

## Próximos pasos

- [ ] Generar intentos simulados y escribirlos en el CSV, combinando actividad normal con patrones preparados.
- [ ] Mejorar la validación de las filas y los mensajes de error.
- [ ] Incorporar pruebas automatizadas de las reglas.
- [ ] Añadir reglas para accesos correctos tras varios fallos e intentos sobre distintas cuentas.
- [ ] Guardar eventos y revisiones de alertas con SQLite.
- [ ] Crear una interfaz con Swing para cargar, filtrar y revisar los registros.

El generador es el siguiente paso acordado y todavía no está implementado.
