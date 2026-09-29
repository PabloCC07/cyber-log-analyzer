package com.pablocc07.cyberlog;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalizadorAccesos {

    /**
     * Genera eventos aleatorios y los guarda en una lista
     * @param minutos tiempo ficticio que durarían dichos intentos
     * @param intentosPorMinuto número de intentos por cada minuto
     * @return devuelve una lista con objetos de la clase EventoAcceso
     */
    public static List<EventoAcceso> generarEventos(int minutos, int intentosPorMinuto) {
        /* Lista vacía para guardar los eventos generados en memoria */
        List<EventoAcceso> eventos = new ArrayList<>();

        /* Comprueba que los minutos e intentos no sean negativos */
        if (minutos < 0 || intentosPorMinuto < 0) {
            System.out.println("Los minutos y los intentos no pueden ser negativos");
            return eventos;
        }

        /* Crea un array con los 6 usuarios que el generador puede elegir */
        String[] usuarios = {"pablo", "laura", "carlos", "ana", "marta", "luis"};
        /* Obtiene la fecha y hora actuales y las guarda como punto de inicio de la
        * simulación */
        LocalDateTime inicio = LocalDateTime.now();

        /* Bucle que recorre los minutos que queremos simular. Las vueltas dependerán
        * del valor que le demos al parámetro 'minutos' */
        for (int minuto = 0; minuto < minutos; minuto++) {

            /* Genera los intentosPorMinuto por cada minuto (vuelta del primer bucle) */
            for (int intento = 0; intento < intentosPorMinuto; intento++) {
                /* Elige aleatoriamente un número entre 0 y 5, que corresponde al usuario */
                int posicionUsuario = (int) (Math.random() * usuarios.length);
                /* Busca en el array de usuarios por el número generado aleatoriamente
                * y guarda su nombre en una variable */
                String usuario = usuarios[posicionUsuario];

                /* Genera un número entre 1 y 10 para después formar la IP */
                int numeroIp = (int) (Math.random() * 10) + 1;
                String ip = "192.0.2." + numeroIp;

                /* Genera el segundo en el que ocurre el evento dentro del minuto
                * actual y lo guarda en una variable nueva */
                int segundos = (int) (Math.random() * 60);
                LocalDateTime fecha = inicio.plusMinutes(minuto).plusSeconds(segundos);

                /* Genera un número entre 0 y 99 para determinar si el acceso es fallido
                * o no. Cada intento tiene un 80% de probabilidad de éxito, por ello si
                * el número generado está comprendido entre 0 y 79 es 'true', y de
                * 80 a 99 es 'false' */
                int resultado = (int) (Math.random() * 100);
                boolean exitoso = resultado < 80;

                /* Crea un objeto de la clase EventoAcceso y lo añade a la lista */
                eventos.add(new EventoAcceso(fecha, usuario, ip, exitoso));
            }
        }

        return eventos;
    }

    /**
     * Método que recibe como parámetro una lista con objetos de la clase EventoAcceso,
     * recorre dichos objetos y cuenta los fallos que llevan acumulados para meterlos en
     * un HashMap
     * @param eventos lista de la clase EventoAcceso
     * @return devuelve un HashMap con la IP y los fallos que lleva acumulados
     */
    public static HashMap<String, Integer> contarFallos(List<EventoAcceso> eventos) {
        /* Crea un mapa vacío */
        HashMap<String, Integer> fallosPorIp = new HashMap<String, Integer>();

        /* Recorre cada objeto de la lista evento, que contiene
         * objetos de la clase EventoAcceso */
        for (EventoAcceso evento : eventos) {

            /* Si el acceso fue exitoso niega el resultado con '!',
            * así conseguimos filtrar los accesos fallidos */
            if (!evento.isExitoso()) {
                /* Obtiene la IP del acceso fallido y lo guarda en
                * una variable */
                String ip = evento.getIp();

                /* Comprueba si esa IP ya existe en el HashMap para ver
                * si ya hemos contado algún fallo suyo */
                if (fallosPorIp.containsKey(ip)) {
                    /* Cuenta los fallos que tiene esa IP */
                    int cantidad = fallosPorIp.get(ip);

                    /* Guarda para la IP el número de fallos que tiene hasta
                    * ahora más uno */
                    fallosPorIp.put(ip, cantidad + 1);
                } else {
                    /* Guarda la IP con un fallo ya que es su primero */
                    fallosPorIp.put(ip, 1);
                }
            }
        }

        return fallosPorIp;
    }

    /**
     * Detecta cinco o más fallos de una misma IP en cinco minutos.
     * Incluye las fechas de inicio y fin y devuelve una sola alerta por IP.
     */
    public static List<String> detectarAlertas(List<EventoAcceso> eventos) {
        /* Crea dos listas para guardar las alertas y las IP's que ya tienen una alerta */
        List<String> alertas = new ArrayList<>();
        List<String> ipsAlertadas = new ArrayList<>();

        /* Recorre la lista EventoAcceso */
        for (EventoAcceso evento : eventos) {
            /* Guarda cada IP en una variable */
            String ip = evento.getIp();

            /* Comprueba que el acceso haya fallado y que la IP no tenga ya una alerta */
            if (!evento.isExitoso() && !ipsAlertadas.contains(ip)) {
                /* Guarda la fecha de inicio en una variable */
                LocalDateTime inicio = evento.getFecha();
                /* Guarda la fecha de fin en una variable agregando 5 minutos */
                LocalDateTime fin = inicio.plusMinutes(5);
                /* Inicia a cero el contador de este intervalo */
                int cantidad = 0;

                /* Cuenta los fallos de la misma IP dentro del intervalo */
                for (EventoAcceso otroEvento : eventos) {
                    /* Selecciona los eventos que son accesos fallidos y tienen la
                    * misma IP que el anterior */
                    if (!otroEvento.isExitoso() && otroEvento.getIp().equals(ip)) {
                        /* Guarda la fecha evento que está revisando la segunda variable */
                        LocalDateTime fecha = otroEvento.getFecha();

                        /* Comprueba que la fecha esté dentro del intervalo */
                        if (!fecha.isBefore(inicio) && !fecha.isAfter(fin)) {
                            /* Si se cumple la condición aumenta el contador en uno */
                            cantidad++;
                        }
                    }
                }

                /* Si la cantidad es de al menos 5 intentos fallidos es mayor o igual a 5
                * devuelve un mensaje de alerta */
                if (cantidad >= 5) {
                    alertas.add("ALERTA | IP: " + ip + " | Fallos: " + cantidad
                            + " | Intervalo: " + inicio + " - " + fin);
                    /* Guarda la IP en una lista de IP's ya alertadas para que el
                    * primer bucle no vuelva a analizarla */
                    ipsAlertadas.add(ip);
                }
            }
        }

        return alertas;
    }

    public static List<EventoAcceso> generarEscenario(int opcion) {
        List<EventoAcceso> eventos = new ArrayList<>();

        if (opcion < 1 || opcion > 3) {
            System.out.println("Escenario no válido");
            return eventos;
        }

        int cantidad = 5;
        int segundosEntreIntentos = 60;

        if (opcion == 1) {
            cantidad = 4;
        } else if (opcion == 2) {
            // Cuatro separaciones de 75 segundos suman exactamente cinco minutos.
            segundosEntreIntentos = 75;
        } else {
            segundosEntreIntentos = 360;
        }

        LocalDateTime inicio = LocalDateTime.of(2026, 9, 29, 10, 0);
        for (int i = 0; i < cantidad; i++) {
            LocalDateTime fecha = inicio.plusSeconds(i * segundosEntreIntentos);
            eventos.add(new EventoAcceso(fecha, "pablo", "192.0.2.1", false));
        }

        return eventos;
    }
}
