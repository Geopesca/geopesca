package com.example.silva.geopesca.Dominio;

/**
 * Created by Silva on 20/02/2016.
 */

public class Geodados{

    private int _id;
    private String _idTipo;
    private String _descricaoTipo;
    private String _historico;

    public Geodados() {
    }

    public void set_ID(int id) {
        this._id = id;
    }

    public int get_ID() {
        return this._id;
    }

    public void set_IdTipo(String idTipo) {
        this._idTipo = idTipo;
        set_DescricaoTipo();
    }

    public String get_IdTipo() {
        return this._idTipo;
    }

    private void set_DescricaoTipo(){
        if (this._idTipo.equals("0"))
            this._descricaoTipo = "Local Proibido";
        else
            this._descricaoTipo = "Local com Restrição";
    }

    public String get_DescricaoTipo(){
        return this._descricaoTipo;
    }

    public String get_Historico() {  return _historico; }

    public void set_Historico(String historico) {
         this._historico = historico;
    }
}


class Geopontos{
    private double _latitude = 0.0;
    private double _longitude = 0.0;

    public double get_latitude() {
        return _latitude;
    }

    public void set_latitude(double latitude) {
        this._latitude = latitude;
    }

    public double get_longitude() {
        return _longitude;
    }

    public void set_longitude(double longitude) {
        this._longitude = longitude;
    }
}