package com.gm3_developer.sem5_demo;


import java.io.Serializable;

public class Dto implements Serializable {
    int codigo;
    String descripcion;
    double precio;
    int id_cate;
    String nombrecategoria;
    int estadocategoria;

    public Dto() {
    }

    public Dto(int codigo, String descripcion, double precio, int id_cate, String nombrecategoria, int estadocategoria) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.id_cate = id_cate;
        this.nombrecategoria = nombrecategoria;
        this.estadocategoria = estadocategoria;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getId_cate() {
        return id_cate;
    }

    public void setId_cate(int id_cate) {
        this.id_cate = id_cate;
    }

    public String getNombrecategoria() {
        return nombrecategoria;
    }

    public void setNombrecategoria(String nombrecategoria) {
        this.nombrecategoria = nombrecategoria;
    }

    public int getEstadocategoria() {
        return estadocategoria;
    }

    public void setEstadocategoria(int estadocategoria) {
        this.estadocategoria = estadocategoria;
    }
}
