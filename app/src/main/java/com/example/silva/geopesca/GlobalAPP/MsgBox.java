package com.example.silva.geopesca.GlobalAPP;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Silva on 19/02/2016.
 */
public class MsgBox {

    public static void ToolTip(Context contex, String msg){
        Toast.makeText(contex, msg, Toast.LENGTH_LONG).show();
    }

    public static void Alert(final Context contex,String msg){
        AlertDialog.Builder dlg = new AlertDialog.Builder(contex);
        dlg.setMessage(msg);
        dlg.setNeutralButton("OK",null);
        dlg.show();
    }


    //Info e com icone

}
