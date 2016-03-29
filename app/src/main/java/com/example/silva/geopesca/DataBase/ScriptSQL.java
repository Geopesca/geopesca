package com.example.silva.geopesca.DataBase;

/**
 * Created by Silva on 16/02/2016.
 */
public class ScriptSQL {

    //comando static permite acessar o método sem intanciar a classe
    public static String getTabelaGeoDados(){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("CREATE TABLE IF NOT EXISTS geodados( ");
        strBuilder.append("_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        strBuilder.append("idtipo varchar(1), historico text);");

        return strBuilder.toString();
    }

    public static String getTabelaGeoPontos(){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("CREATE TABLE IF NOT EXISTS geopontos( ");
        strBuilder.append("_id INTEGER, idpto varchar(1), ");
        strBuilder.append("latitude decimal(10,6), longitude decimal(10,6));");

        return strBuilder.toString();
    }

    public static String getSqlItem(){
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("Select historico FROM geodados where _id= S%;");

        return strBuilder.toString();
    }
}
