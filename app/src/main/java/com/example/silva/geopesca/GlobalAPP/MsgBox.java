package com.example.silva.geopesca.GlobalAPP;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.widget.Toast;

import com.example.silva.geopesca.R;

/**
 * Created by Silva on 19/02/2016.
 */
public class MsgBox {

    public static void ToolTip(Context contex, String msg){
        Toast.makeText(contex, msg, Toast.LENGTH_LONG).show();
    }

    public static void Alert(Context context,String msg){
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setIcon(R.drawable.ifsp);
        dlg.setTitle("GeoPesca");
        dlg.setMessage(msg);
        dlg.setNeutralButton("OK",null);
        dlg.show();
    }
    public static void MsgImage(Context context,AnimationDrawable animation){
        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setIcon(animation.getCurrent());
        dlg.setTitle("GeoPesca");
        //dlg.setMessage("GeoPesca");
        dlg.setNeutralButton("OK",null);
        dlg.show();
    }


    public static void Contatos(Context context){
        StringBuilder strBuilder = new StringBuilder();
        //strBuilder.append("APP: GeoPesca \n");
        strBuilder.append("Autores: \n");
        strBuilder.append("José Maria da Silva e \n");
        strBuilder.append("Rodrigo Maia da Costa \n \n");
        strBuilder.append("Orientador: \n");
        strBuilder.append("Prof. Eduardo Pereira de Sousa \n \n");
        strBuilder.append("e-mail:\n");
        strBuilder.append("saa50@bol.com.br");

        Alert(context, strBuilder.toString());
    }

    public static void Sobre(Context context){
        StringBuilder strBuilder = new StringBuilder();
        //strBuilder.append("APP: GeoPesca \n");
        strBuilder.append("Classificação: Livre.\n");
        strBuilder.append("Mapeamento:\nLitoral Norte do Estado SP. \n");
        strBuilder.append("Instituição:\nIFSP - Campus Caraguatatuba. \n");
        strBuilder.append("Dados:\nMapa da Pesca Sustentável. \n");
        strBuilder.append("Lei: Resolução SMA nº 101 de 18Out2013.");

        Alert(context, strBuilder.toString());
    }

    //Info e com icone

}
