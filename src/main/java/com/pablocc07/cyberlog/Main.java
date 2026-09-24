package com.pablocc07.cyberlog;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            List<EventoAcceso> eventos = LectorEventosCsv.leer();

            for (EventoAcceso evento : eventos) {
                System.out.println(evento);
            }

            System.out.println("Total de eventos: " + eventos.size());
        } catch (IllegalStateException e) {
            System.err.println("Error al cargar los eventos: " + e.getMessage());
        }
    }
}
