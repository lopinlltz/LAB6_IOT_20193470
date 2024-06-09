package com.example.lab6_iot_20193470;

public class Egreso {
    private String titulo;
    private double monto;
    private String descripcion;
    private String fecha;

    public Egreso() {
    }

    public Egreso(String titulo, double monto, String descripcion, String fecha) {
        this.titulo = titulo;
        this.monto = monto;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}

