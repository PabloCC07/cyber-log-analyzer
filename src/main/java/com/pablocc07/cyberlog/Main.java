package com.pablocc07.cyberlog;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("CYBERLOGANALYZER");
        System.out.println("1. Generación aleatoria (1.000 eventos)");
        System.out.println("2. Cuatro fallos: sin alerta");
        System.out.println("3. Cinco fallos en cinco minutos: una alerta");
        System.out.println("4. Cinco fallos separados: sin alerta");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");

        if (!teclado.hasNextLine()) {
            teclado.close();
            return;
        }

        String opcion = teclado.nextLine();
        teclado.close();
        List<EventoAcceso> eventos;

        if (opcion.equals("0")) {
            return;
        } else if (opcion.equals("1")) {
            int minutos = 5;
            int intentosPorMinuto = 200;
            eventos = AnalizadorAccesos.generarEventos(minutos, intentosPorMinuto);
        } else if (opcion.equals("2")) {
            eventos = AnalizadorAccesos.generarEscenario(1);
        } else if (opcion.equals("3")) {
            eventos = AnalizadorAccesos.generarEscenario(2);
        } else if (opcion.equals("4")) {
            eventos = AnalizadorAccesos.generarEscenario(3);
        } else {
            System.out.println("Opción no válida. Ejecuta de nuevo y elige del 0 al 4.");
            return;
        }

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
    }
}
