package br.aeso.boaviagem;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViagemListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private List<Map<String, Object>> viagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] de = {"imagem","destino","data","total"};
        int[] para = {R.id.tipoViagem,R.id.destino,R.id.data,R.id.valor};

        SimpleAdapter adapter = new SimpleAdapter(this, listarViagens(),
                        R.layout.activity_viagem_list, de, para);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);



        /*setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, listarViagens()));
        ListView listView = getListView();
        listView.setOnItemClickListener(this)*/


    }

    private List<Map<String, Object>> listarViagens() {
        viagens = new ArrayList<Map<String, Object>>();

        Map<String, Object> item = new HashMap<String, Object>();
        item.put("imagem", R.drawable.negocios);
        item.put("destino","São Paulo");
        item.put("data","02/02/2012 a 04/02/2012");
        item.put("total","Gasto Total R$314,98");
        viagens.add(item);

        item = new HashMap<String, Object>();
        item.put("imagem", R.drawable.lazer);
        item.put("destino", "Maceió");
        item.put("data","14/05/2012 a 22/05/2012");
        item.put("total","Gasto total R$ 25834,67");
        viagens.add(item);
        return viagens;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Map<String, Object> map = viagens.get(position);
        String destino = (String) map.get("destino");
        String mensagem = "Viagem selecionada: " + destino;

        Toast.makeText(this,mensagem,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GastoListActivity.class));
    }
}
