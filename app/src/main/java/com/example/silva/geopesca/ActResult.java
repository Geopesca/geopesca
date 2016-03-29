package com.example.silva.geopesca;

import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.*;

import com.example.silva.geopesca.Dominio.RepositorioDados;
import com.example.silva.geopesca.GlobalAPP.MsgBox;

public class ActResult extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private EditText edtResult;

    private ArrayAdapter<String> adpGeodados;
    private ListView lstDados;
    private RepositorioDados repDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_result);

        lstDados = (ListView) findViewById(R.id.lstResult);
        TextView tv = (TextView) findViewById(R.id.txtResult);
        tv.setMovementMethod(new ScrollingMovementMethod());

        //para testar os parametros do intent
        Bundle bundle = getIntent().getExtras();

        if (bundle.containsKey("LONGITUDE")) {
            String longitude = bundle.getString("LONGITUDE");
            String latitude = bundle.getString("LATITUDE");
            longitude = longitude.replaceAll(",", ".");
            latitude = latitude.replaceAll(",", ".");

            try {
                double lat = Double.parseDouble(latitude);
                double lng = Double.parseDouble(longitude);

                //faz a conexao com o banco sqlite e a busca(sql)
                repDados = new RepositorioDados(this);
                //faz o select sql e devolve tipo adp pronto
                adpGeodados = repDados.buscaGeodados(this, lat, lng);
                if (adpGeodados.getCount() > 0) {
                    exibeHistorico(0); //zero é o primeiro registro
                    lstDados.setAdapter(adpGeodados);
                    lstDados.setOnItemClickListener(this);
                } else {
                    //MsgBox.Alert(this, "Não consta proibição ou restrição para pesca no local pesquisado.");
                    TextView resumo = (TextView) findViewById(R.id.txtResult);
                    resumo.setText("Não consta proibição ou restrição para pesca no local pesquisado.");
                }
            } catch (SQLException er) {
                MsgBox.Alert(this, "Erro ao abrir o banco de dados. Erro: " + er.getMessage());
                //this.finish(); //finaliza a active result
            }
        }
    }

    //liberando recurso ao finalizar a active
    @Override
    protected void onDestroy() {
        super.onDestroy();
        repDados.closeDB();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        exibeHistorico(position);
    }

    private void exibeHistorico(int position){
        TextView resumo = (TextView) findViewById(R.id.txtResult);
        String rowpos = adpGeodados.getItem(position);
        int index = rowpos.indexOf("-");
        //BigInteger recno = new BigInteger(rowpos.substring(0, index));
        int recno = Integer.parseInt(rowpos.substring(0, index));
        resumo.setText(repDados.buscaItem(recno));
    }
}
