package com.pablocc07.cyberlog;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class AnalizadorAccesos {

    /**
     * Método que lee un archivo .csv línea por línea y guarda cada elemento de
     * cada línea como un objeto de la clase EventoAcceso en una lista
     * @return devuelve una lista con objetos de la clase EventoAcceso
     */
    public static List<EventoAcceso> leer() {
        /* Crea una lista con objetos de la clase EventoAcceso */
        List<EventoAcceso> eventos = new ArrayList<>();
        /* Crea un objeto que representa la ubicación de un fichero */
        File fichero = new File("src/main/resources/eventos.csv");

        /* Comprueba si el archivo existe */
        if (fichero.exists()) {
            try {
                /* Abre un Scanner para leer el contenido del archivo */
                Scanner sc = new Scanner(fichero);

                /* Comprueba si hay alguna línea disponible */
                if (sc.hasNextLine()) {
                    /* Saltar la cabecera */
                    sc.nextLine();
                }

                /* Lee el archivo mientras queden líneas */
                while (sc.hasNextLine()) {
                    /* Guarda la línea en una variable */
                    String linea = sc.nextLine();

                    /* Convierte la línea en un array y cada elemento del array
                    * está separado por comas */
                    String[] campos = linea.split(",");

                    /* Guarda cada elemento del array en una variable */
                    LocalDateTime fecha = LocalDateTime.parse(campos[0]);
                    String usuario = campos[1];
                    String ip = campos[2];
                    boolean exitoso = campos[3].equals("EXITO");

                    /* Añade los elementos del array a la lista como un objeto
                    EventoAcceso */
                    eventos.add(new EventoAcceso(fecha, usuario, ip, exitoso));
                }

                sc.close();
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("El fichero eventos.csv no existe");
        }

        return eventos;
    }

    /**
     * Método que recibe como parámetro una lista con objetos de la clase EventoAcceso,
     * recorre dichos objetos y cuenta los fallos que llevan acumulados para meterlos en
     * una lista HashMap
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
                    /* Guarda la IP con un fallo ya qué es su primero */
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

            /* Comprueba si el acceso es fallido y dicha IP está contenida en la lista
            * ipsAlertadas */
            if (!evento.isExitoso() && !ipsAlertadas.contains(ip)) {
                /* Guarda la fecha de inicio en una variable */
                LocalDateTime inicio = evento.getFecha();
                /* Guara la fecha de fin en una variable agregando 5 minutos */
                LocalDateTime fin = inicio.plusMinutes(5);
                /* Inicia a cero el contador de este intervalo */
                int cantidad = 0;

                /* Recorre otra vez la lista para */
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
}
