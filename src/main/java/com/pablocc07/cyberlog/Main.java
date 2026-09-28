package com.pablocc07.cyberlog;

import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<EventoAcceso> eventos = AnalizadorAccesos.leer();

            for (EventoAcceso evento : eventos) {
                System.out.println(evento);
            }

            System.out.println("Total de eventos: " + eventos.size());

            HashMap<String, Integer> fallosPorIp = AnalizadorAccesos.contarFallos(eventos);
            System.out.println("Accesos fallidos por IP:");

            for (String ip : fallosPorIp.keySet()) {
                System.out.println("IP: " + ip + " | Fallos: " + fallosPorIp.get(ip));
            }

            List<String> alertas = AnalizadorAccesos.detectarAlertas(eventos);
            System.out.println("Total de alertas: " + alertas.size());

            for (String alerta : alertas) {
                System.out.println(alerta);
            }
        } catch (IllegalStateException e) {
            System.err.println("Error al cargar los eventos: " + e.getMessage());
        }
    }
}
