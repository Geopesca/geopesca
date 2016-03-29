package com.example.silva.geopesca.DataBase;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.silva.geopesca.Dominio.RepositorioDados;
import com.example.silva.geopesca.GlobalAPP.MsgBox;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Silva on 09/02/2016.
 */
public class DataBase extends SQLiteOpenHelper {
    private static String DB_PATH = "/data/data/com.example.silva.geopesca/databases/";
    //private static String DB_PATH = "/storage/sdcard0/Download/";
    private static String DB_NAME = "geopesca.db";
    private SQLiteDatabase db;

    // Constructor
    public DataBase(Context context) {
        super(context, DB_PATH + DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //não implementar
        //db.execSQL(ScriptSQL.getTabelaGeoDados());
        //db.execSQL(ScriptSQL.getTabelaGeoPontos());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //não implementar
    }

    //==============================================================================================
    public static void copyDataBase(Context context) throws IOException {
        InputStream inputStream = context.getAssets().open(DB_NAME);

        //tem que criar o arquivo vazio
        RepositorioDados rd = new RepositorioDados(context);
        rd.closeDB();

        String outFileName = DB_PATH + DB_NAME;
        OutputStream outputStream = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    //==============================================================================================
    public SQLiteDatabase openDataBase() throws SQLException {
        //Abre o banco geopesca.db
        String myPath = DB_PATH + DB_NAME;
        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        return null;
    }

    //==============================================================================================
    @Override
    public synchronized void close() {
        if(db != null)
            db.close();
        super.close();
    }

    //==============================================================================================
    public static void excluir_files(){
        File appsDir = new File(DB_PATH);
        //lista os files de uma pasta
        String[] files = appsDir.list();
        for (int i = 0 ; i < files.length ; i++ ) {
            System.out.println("File: " + files[i]);
            System.out.println("Size: " + files[i].getBytes().length);

            File xx = new File(DB_PATH+files[i]);
            System.out.println(xx.delete());
            System.out.println("Excluido o arquivo: " + files[i]);
        }
    }

    //==============================================================================================
    public static void exibe_files(Context context){
        //lista os files de uma pasta
        File dbPath = new File(DB_PATH);
        String[] files = dbPath.list();

        MsgBox.Alert(context, "Total de arquivos: " + files.length);
        for (int i = 0 ; i < files.length ; i++ ) {
            MsgBox.Alert(context,"File: " + files[i]);
        }
    }

    //==============================================================================================
    public static boolean isFile() {
        if (new File(DB_PATH + DB_NAME).exists())
            return true;
        return false;
    }

    //==============================================================================================
    public static void appPath(Context context) {
        //String appRoot = context.getFilesDir().getPath() + File.separator;
        //String apkPath = context.getPackageCodePath();
        String dbPath = context.getPackageName();
        //MsgBox.Alert(context,appRoot);
        MsgBox.Alert(context, dbPath);
    }
}