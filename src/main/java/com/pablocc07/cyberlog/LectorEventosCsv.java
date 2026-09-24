package com.pablocc07.cyberlog;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LectorEventosCsv {

    public static List<EventoAcceso> leer() {
        List<EventoAcceso> eventos = new ArrayList<>();
        File fichero = new File("src/main/resources/eventos.csv");

        if (fichero.exists()) {
            try {
                Scanner sc = new Scanner(fichero);

                if (sc.hasNextLine()) {
                    sc.nextLine(); // Saltar la cabecera
                }

                while (sc.hasNextLine()) {
                    String linea = sc.nextLine();
                    String[] campos = linea.split(",");

                    LocalDateTime fecha = LocalDateTime.parse(campos[0]);
                    String usuario = campos[1];
                    String ip = campos[2];
                    boolean exitoso = campos[3].equals("EXITO");

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
}
