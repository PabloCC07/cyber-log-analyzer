# CyberLogAnalyzer

Proyecto académico de 2.º de DAM que genera accesos ficticios en memoria y analiza patrones sospechosos. Funciona por consola y utiliza reglas de detección.

## Funcionalidades

- Elegir mediante un menú entre generación aleatoria y tres escenarios preparados.
- Generar eventos en memoria y analizarlos directamente.
- Mostrar los eventos y su número total.
- Contar los fallos por dirección IP.
- Detectar cinco o más fallos de una misma IP en cinco minutos.
- Mostrar una sola alerta por IP durante cada ejecución.

Una alerta señala un patrón para revisar; no confirma que haya ocurrido un ataque.

## Ejecutar

1. Abre el proyecto en IntelliJ IDEA y carga su configuración Maven.
2. Selecciona Java 24 como SDK del proyecto.
3. Ejecuta `com.pablocc07.cyberlog.Main`.

En la opción 1 del menú se generan **1.000 eventos: 200 por minuto durante 5 minutos simulados**. Se generan al instante, sin esperar tiempo real. Los datos solo se conservan durante la ejecución. Puedes ajustar `minutos` e `intentosPorMinuto` en `Main`.

| Opción | Escenario | Resultado esperado |
| --- | --- | --- |
| 1 | 1.000 eventos aleatorios | Variable |
| 2 | Cuatro fallos entre las 10:00 y las 10:03 | 0 alertas |
| 3 | Cinco fallos a las 10:00, 10:01:15, 10:02:30, 10:03:45 y 10:05 | 1 alerta |
| 4 | Cinco fallos separados por seis minutos | 0 alertas |
| 0 | Salir | Sin análisis |

Los escenarios preparados usan la fecha fija 29/09/2026 y la IP ficticia `192.0.2.1`. La opción 3 comprueba que se incluye el límite exacto de cinco minutos. Cada ejecución analiza una opción; si introduces una opción inválida, muestra un mensaje y termina.

## Organización

| Clase | Responsabilidad |
| --- | --- |
| `EventoAcceso` | Guarda la fecha, el usuario, la IP y el resultado de un intento. |
| `AnalizadorAccesos` | Agrupa `generarEventos()`, `generarEscenario()`, `contarFallos()` y `detectarAlertas()`. |
| `Main` | Configura la simulación y muestra los resultados. |

El código usa arrays, bucles, `ArrayList`, `HashMap`, `Math.random()`, `Scanner` y `LocalDateTime`.

## Generación y análisis

El generador elige entre seis usuarios y diez IP ficticias del rango de documentación `192.0.2.0/24`. Cada intento tiene un 80 % de probabilidad de éxito. Las fechas se distribuyen aleatoriamente dentro de cada minuto simulado. Los resultados varían entre ejecuciones; no se garantiza una cantidad exacta de éxitos ni de alertas.

Cada evento fallido puede iniciar un intervalo de cinco minutos. El detector cuenta los fallos de esa IP dentro del intervalo, incluyendo ambos extremos. Los accesos correctos no suman ni reinician el contador. Se muestra el primer intervalo que cumple la regla según el recorrido de la lista, que puede estar desordenada.

## Limitaciones

- Los parámetros negativos muestran un mensaje y devuelven una lista vacía; un parámetro igual a cero también produce una lista vacía.
- Solo se informa de una alerta por IP, aunque existan varios intervalos sospechosos.
- El orden del resumen por IP puede variar porque se utiliza `HashMap`.
- La detección tiene un coste cuadrático en el peor caso; conviene revisarla antes de analizar volúmenes grandes.
- Todavía no hay interfaz gráfica, base de datos ni pruebas automatizadas incorporadas al repositorio.

## Próximos pasos

- Incorporar pruebas automatizadas de las reglas.
- Añadir reglas para accesos correctos tras varios fallos e intentos sobre distintas cuentas.
- Guardar eventos y revisiones de alertas con MariaDB mediante XAMPP y JDBC.
- Crear una interfaz con Swing para revisar y filtrar los registros.
