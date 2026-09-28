# Datos de ejemplo

`eventos.csv` contiene 15 accesos ficticios para demostrar el análisis. Las IP utilizan rangos reservados para documentación.

## Formato

```csv
fecha,usuario,ip,resultado
2026-09-24T08:12:35,pablo,192.0.2.10,EXITO
```

- `fecha`: fecha y hora compatible con `LocalDateTime`, por ejemplo `2026-09-24T08:12:35`.
- `usuario`: nombre ficticio de la cuenta.
- `ip`: dirección de origen simulada.
- `resultado`: `EXITO` o `FALLO`, en mayúsculas.

La primera línea es la cabecera y el lector la descarta. Utiliza una fila por evento, sin líneas vacías, comillas, espacios adicionales ni comas dentro de los campos.

## Escenarios y resultado esperado

1. Cinco registros iniciales: dos accesos correctos y tres fallos aislados. No generan alertas.
2. Cinco fallos de `203.0.113.50`, desde las 10:00 hasta las 10:04. Generan una alerta para el intervalo de 10:00 a 10:05.
3. Cinco fallos de `192.0.2.80`, desde las 11:00 hasta las 11:24, separados por seis minutos. No generan alertas.

Resultado total: **15 eventos y una alerta**.

## Casos útiles para comprobar la regla

- Cuatro fallos de una IP: sin alerta.
- Cinco fallos comprendidos exactamente en cinco minutos: con alerta.
- Fallos a los 0, 60, 120, 180 y 301 segundos: sin alerta.
- Accesos correctos y fallos de otras IP: no se suman al contador del intervalo de la IP analizada.
- Eventos desordenados: se mantiene la detección, aunque el intervalo mostrado puede cambiar.

El generador de intentos se incorporará más adelante. Actualmente los registros de este archivo están preparados a mano.
