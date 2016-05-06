package com.example.silva.geopesca.GlobalAPP;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.silva.geopesca.R;

/**
 * Created by Silva on 06/05/2016.
 */
public class Progress {
    private ProgressDialog dlg;

    public Progress(Context context){
        dlg = new ProgressDialog(context);
        dlg.setIcon(R.drawable.ifsp);
        dlg.setMessage("Processando...");
//        dlg.setTitle("GeoPesca");
        dlg.show();
    }

    public void Close(){
        dlg.dismiss();
    }

}
