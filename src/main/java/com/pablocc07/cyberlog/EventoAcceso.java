package com.pablocc07.cyberlog;

import java.time.LocalDateTime;

public class EventoAcceso {
    private final LocalDateTime fecha;
    private final String usuario;
    private final String ip;
    private final boolean exitoso;

    public EventoAcceso(LocalDateTime fecha, String usuario, String ip, boolean exitoso) {
        this.fecha = fecha;
        this.usuario = usuario;
        this.ip = ip;
        this.exitoso = exitoso;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getIp() {
        return ip;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    @Override
    public String toString() {
        return "EventoAcceso{" +
                "fecha=" + fecha +
                ", usuario='" + usuario + '\'' +
                ", ip='" + ip + '\'' +
                ", exitoso=" + exitoso +
                '}';
    }
}
