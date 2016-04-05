package com.example.silva.geopesca;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.silva.geopesca.DataBase.DataBase;
import com.example.silva.geopesca.Dominio.RepositorioDados;
import com.example.silva.geopesca.GPSTracker.GPSTracker;
import com.example.silva.geopesca.GlobalAPP.MsgBox;

import java.io.IOException;
import java.util.Locale;


public class MainAct extends AppCompatActivity implements View.OnClickListener {
    private static final int CH_ID_LIMITE_TCC = 33;
    private static final String CH_LIMITE_PLG = "1";

    private EditText edtLngG;
    private EditText edtLngM;
    private EditText edtLngS;

    private EditText edtLatG;
    private EditText edtLatM;
    private EditText edtLatS;

    private TextView txtMsg;
    private ImageView imgLogo;
    private AnimationDrawable animation;

    private Button btnPesquisa;
    private Button btnOndeEstou;

    private int tam;
    private EditText nextGet;

    //private Timer timerAtual = new Timer();
    //private TimerTask task;
    //private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        //recupera longitude
        edtLngG = (EditText) findViewById(R.id.edtLngG);
        edtLngM = (EditText) findViewById(R.id.edtLngM);
        edtLngS = (EditText) findViewById(R.id.edtLngS);

        //recupera latitude
        edtLatG = (EditText) findViewById(R.id.edtLatG);
        edtLatM = (EditText) findViewById(R.id.edtLatM);
        edtLatS = (EditText) findViewById(R.id.edtLatS);

        //titulo do tcc
        txtMsg = (TextView) findViewById(R.id.txtMsg);

        //imagens e logo
        imgLogo = (ImageView) findViewById(R.id.animacao);
        imgLogo.setBackgroundResource(R.drawable.animacao);
        animation = (AnimationDrawable) imgLogo.getBackground();
        animation.start();
        imgLogo.setOnClickListener(this);

        //Animation deslocamento = new TranslateAnimation(0, 1000, 0, 0);
        //deslocamento.setDuration(3000);
        //imgLogo.startAnimation(deslocamento);


        btnPesquisa = (Button) findViewById(R.id.btnPesquisa);
        btnOndeEstou = (Button) findViewById(R.id.btnOndeEstou);
        //registra o botão
        btnPesquisa.setOnClickListener(this);
        btnOndeEstou.setOnClickListener(this);

        //para pular para proximo campo
        edtLngG.addTextChangedListener(new myTextWatcher(edtLngM,2));
        edtLngM.addTextChangedListener(new myTextWatcher(edtLngS,2));
        edtLngS.addTextChangedListener(new myTextWatcher(edtLatG,5));
        edtLatG.addTextChangedListener(new myTextWatcher(edtLatM,2));
        edtLatM.addTextChangedListener(new myTextWatcher(edtLatS,2));

        //rotinas de teste
        //DataBase.excluir_files(); //deleta todos os arquivos database

        //DataBase.exibe_files(this);
    }

    private class myTextWatcher implements TextWatcher {
        EditText mProx;
        int tam;

        public myTextWatcher( EditText p, int tamanho) {
            mProx = p;
            tam = tamanho;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if(s.length() == tam){
                mProx.requestFocus();
            }
        }
    }

    //evento principal esta ligado ao implements...duas formas de fazer
    public void onClick(View v) {
        if (v == imgLogo) {
            if (!animation.isRunning())
                animation.start();
            else{
                animation.stop();
                //MsgBox.Alert(this,animation.getCurrent().toString());
            }

            return;
        }

        if (v == btnPesquisa) {
            String x1 = edtLngG.getText().toString();
            String x2 = edtLngM.getText().toString();
            String x3 = edtLngS.getText().toString();
            String y1 = edtLatG.getText().toString();
            String y2 = edtLatM.getText().toString();
            String y3 = edtLatS.getText().toString();

            /* Exemplo legal
            File dataBase = getApplicationContext().getDatabasePath(DataBase.DB_NAME);
            if (!dataBase.exists()){
                MsgBox.Alert(this, "Problemas com banco de dados. 1");
            }
            */

            //DataBase.appPath(this);

            //if(DataBase.isFile()) {
            //    MsgBox.Alert(this, "O arquivo está OK.");
            //}

            if(!DataBase.isFile()) {
                try {
                    DataBase.copyDataBase(this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(!DataBase.isFile()) {
                MsgBox.Alert(this, "Problemas com banco de dados.");
                return;
            }

            if (!x1.trim().isEmpty() && !x2.trim().isEmpty() && !x3.trim().isEmpty() &&
               !y1.trim().isEmpty() && !y2.trim().isEmpty() && !y3.trim().isEmpty()) {
                //converte gms para graus decimais
                double longdec = (Double.parseDouble(x1) + (Double.parseDouble(x2) / 60) +
                                 (Double.parseDouble(x3) / 3600)) * -1;

                double latdec = (Double.parseDouble(y1) + (Double.parseDouble(y2) / 60) +
                                (Double.parseDouble(y3) / 3600)) * -1;

                //trata-se do limite do TCC _id é 33 idpto "9" no BD.
                RepositorioDados rd = new RepositorioDados(this);
                double[][] paths = rd.buscaPolygon(CH_ID_LIMITE_TCC, CH_LIMITE_PLG );
                rd.closeDB();

                boolean lcheck = false;
                if (Double.parseDouble(x2) > 59 || Double.parseDouble(y2) > 59 ||
                    Double.parseDouble(x3) > 59.99 || Double.parseDouble(y3) > 59.99) {
                    lcheck = true;
                }

                if (lcheck){
                    MsgBox.ToolTip(this, "Atenção, coordenadas inválidas.");
                }else if (RepositorioDados.testarPolygon(paths, latdec, longdec)) {
                    Intent it = new Intent(this, ActResult.class);
                    it.putExtra("LONGITUDE", String.format("%.6f", longdec));
                    it.putExtra("LATITUDE", String.format("%.6f", latdec));
                    startActivity(it);
                } else {
                    MsgBox.ToolTip(this,"Este local está fora dos limites mapeado.");
                }
            }else {
                MsgBox.ToolTip(this,"Campos vazios, click no botão Onde Estou?");
            }
        } else {
            // create class object
            GPSTracker gps = new GPSTracker(MainAct.this);

            // check if GPS enabled
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                double precisao = gps.getPrecisao();

                if (latitude != 0.0 && longitude != 0.0) {
                    DecimalToGMS(latitude, "LAT");  // Latitude coletada
                    DecimalToGMS(longitude, "LNG"); // Longitude coletada
                    txtMsg.setText("Precisão de " + precisao + " metros");
                    gps.stopUsingGPS();
                } else {
                    MsgBox.ToolTip(this, "Aguarde localizando os satélites.");
                }
            } else {
                gps.showSettingsAlert();
            }

        }
    }

    //lembrar de tratar o numero negativo
    public void DecimalToGMS(Double valor, String x) {
        //valor negativo para positivo
        if (valor < 0)
            valor = valor * -1;

        // graus
        int graus = valor.intValue();

        //minutos
        Double aux = (valor - graus) * 60;
        int minutos = aux.intValue();

        //segundos decimais
        double segundos = (aux - minutos) * 60;

        //System.out.println(""+graus+"º "+minutos+"' "+segundos+'"');
        if (x.contentEquals("LNG")) {
            edtLngG.setText("" + graus);
            edtLngM.setText("" + minutos);
            edtLngS.setText(String.format(Locale.US, "%.2f", segundos));
        } else {
            edtLatG.setText("" + graus);
            edtLatM.setText("" + minutos);
            edtLatS.setText(String.format(Locale.US, "%.2f", segundos));
        }
    }
}