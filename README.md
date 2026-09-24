# CyberLogAnalyzer

Proyecto académico en Java para practicar la lectura de registros de acceso y, más adelante, detectar patrones que merezcan una revisión de seguridad. Está en desarrollo como parte del aprendizaje de 2.º de DAM.

## Estado actual

La aplicación lee `src/main/resources/eventos.csv`, convierte cada fila en un `EventoAcceso`, muestra los eventos por consola e indica cuántos ha cargado. Todavía no genera alertas ni analiza intentos sospechosos.

## Requisitos

- Java 24, configurado en `pom.xml`.
- IntelliJ IDEA con soporte para proyectos Maven.

## Ejecutar en IntelliJ IDEA

1. Abre la carpeta del proyecto en IntelliJ IDEA.
2. Comprueba que el SDK del proyecto sea Java 24.
3. Ejecuta `com.pablocc07.cyberlog.Main` con el directorio de trabajo configurado en la raíz del proyecto.
4. Deberías ver los cinco eventos de ejemplo y `Total de eventos: 5`.

El lector usa una ruta relativa (`src/main/resources/eventos.csv`), por lo que debe ejecutarse desde la raíz del proyecto.

## Formato de los datos

El CSV lleva la cabecera `fecha,usuario,ip,resultado`. La fecha sigue el formato de `LocalDateTime`, por ejemplo `2026-09-24T08:12:35`; el resultado es `EXITO` o `FALLO`.

Los registros incluidos son ficticios. Las direcciones IP pertenecen a rangos reservados para documentación.

## Próximos pasos

- Contar los fallos de acceso por dirección IP.
- Detectar cinco fallos de una misma IP en un intervalo de cinco minutos.
- Mostrar alertas con la IP, la hora y el motivo.

## Limitaciones actuales

El lector espera filas con los cuatro campos en el orden indicado y no admite comas dentro de los campos. La detección de anomalías aún no está implementada.
