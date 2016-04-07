package com.example.silva.geopesca.Dominio;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.example.silva.geopesca.DataBase.DataBase;

/**
 * Created by Silva on 17/02/2016.
 */
public class RepositorioDados {
    private static final String CH_LIMITE_MBR = "0";
    private static final String CH_LIMITE_PLG = "1";

    private DataBase db;
    private SQLiteDatabase conn;

    public RepositorioDados(Context context) {
       if (db ==null){
           conexaoSQLite(context);
       }
    }

    public  void conexaoSQLite(Context context){
        db = new DataBase(context);
        conn = db.getWritableDatabase(); //cria, abre e update
    }

    public void closeDB(){
        if(conn!= null)
            conn.close();
    }

    //teste de inclusão
    /*public void testeCargaDados(Context context) {
        ContentValues values;
        for (int i = 0; i < 10; i++) {
            values = new ContentValues();
            values.put("idtipo", "1");
            values.put("historico", "Restrição " + i);
            conn.insertOrThrow("geodados", null, values); //insere a tupla
        }
    } */

    public ArrayAdapter<String> buscaGeodados(Context context, double lat, double lng) {
        ArrayAdapter<String> adpGeodados = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
        Cursor cursor = conn.query("geodados", null, null, null, null, null, null);
        double[][] polygon;
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            Geodados geodados = new Geodados();
            for (int i = 0; i < cursor.getCount(); i++) {
                geodados.set_ID(cursor.getInt(cursor.getColumnIndex("_id")));
                geodados.set_IdTipo(cursor.getString(cursor.getColumnIndex("idtipo")));

                polygon = buscaPolygon(geodados.get_ID(), CH_LIMITE_MBR );
                if(testarPolygon(polygon, lat, lng)){
                    polygon = buscaPolygon(geodados.get_ID(), CH_LIMITE_PLG );
                    if(testarPolygon(polygon, lat, lng)){
                        //so vai adiconar se passar pelo MBR e polígono
                        adpGeodados.add(""+geodados.get_ID()+"-"+geodados.get_DescricaoTipo());
                    }
                }
                cursor.moveToNext();
            };
        }
        return adpGeodados;
    }

    public String buscaItem(int id) {
        Cursor cursor = conn.rawQuery("SELECT * FROM geodados WHERE _id = "+id,null);
        Geodados geodados = new Geodados();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            geodados.set_Historico(cursor.getString(cursor.getColumnIndex("historico")));
        }
        return (geodados.get_Historico());
    }

    public double[][] buscaPolygon(int id, String tp) {
        String sql = "SELECT latitude,longitude FROM geopontos WHERE _id = " + id + " and idpto ='" + tp + "';";
        Cursor cursor = conn.rawQuery(sql, null);
        double[][] plg = new double[cursor.getCount()][2];
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            Geopontos pto = new Geopontos();
            for (int i = 0; i < cursor.getCount(); i++) {
                pto.set_latitude(cursor.getDouble(cursor.getColumnIndex("latitude")));
                pto.set_longitude(cursor.getDouble(cursor.getColumnIndex("longitude")));

                plg[i][0] = pto.get_latitude();
                plg[i][1] = pto.get_longitude();
                cursor.moveToNext();
            }
        }
        return (plg);
    }

    public static boolean testarPolygon(double paths[][], double lat, double lng) {
        boolean retorno1 = false;
        boolean retorno2 = false;
        boolean retorno3 = false;
        double calc;
        int j = 0;

        for (int i = 0; i < paths.length; i++) {
            j++;
            if (j >= paths.length) {
                j = 0;
            }

            if (((paths[i][0] < lat) && (paths[j][0] >= lat)) || ((paths[j][0] < lat) && (paths[i][0] >= lat))){
                calc = paths[i][1] + (lat - paths[i][0]) / (paths[j][0] - paths[i][0]) * (paths[j][1] - paths[i][1]);
                if(calc < lng){retorno1 = !retorno1;}
                if(calc == lng){retorno2 = !retorno2;}
            }else if(paths[i][0] == lat || paths[j][0] == lat) {
                calc = paths[i][1] + (lat - paths[i][0]) / (paths[j][0] - paths[i][0]) * (paths[j][1] - paths[i][1]);
                if(calc == lng){retorno3 = !retorno3;}
            }
        }
        return (retorno1 || retorno2 || retorno3);
    }

}