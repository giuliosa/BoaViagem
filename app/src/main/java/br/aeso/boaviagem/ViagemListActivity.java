package br.aeso.boaviagem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.PreferenceChangeEvent;

import br.aeso.boaviagem.br.aeso.boaviagem.dao.BoaViagemDAO;
import br.aeso.boaviagem.domain.Viagem;

public class ViagemListActivity extends ListActivity
        implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener,
        SimpleAdapter.ViewBinder {

    private List<Map<String, Object>> viagens;
    private AlertDialog alertDialog;
    private AlertDialog dialogConfirmacao;
    private int viagemSelecionada;
    private SimpleDateFormat dateFormat;
    private Double valorLimite;
    private BoaViagemDAO dao;
    private boolean modoSelecionarViagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dao = new BoaViagemDAO(this);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String valor = preferences.getString("valor_limite", "-1");
        valorLimite = Double.valueOf(valor);

        String[] de = {"imagem","destino","data","total","barraProgresso"};
        int[] para = {R.id.tipoViagem,R.id.destino,R.id.data,R.id.valor,R.id.barraProgresso};

        SimpleAdapter adapter = new SimpleAdapter(this, listarViagens(),
                        R.layout.activity_viagem_list, de, para);

        adapter.setViewBinder(this);

        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        this.alertDialog = criaAlertDialog();

        this.dialogConfirmacao = criaDialogConfirmacao();

        if(getIntent().hasExtra(Constantes.MODO_SELECIONAR_VIAGEM)){
            modoSelecionarViagem = getIntent().getExtras().getBoolean(Constantes.MODO_SELECIONAR_VIAGEM);
        }

    }

    private List<Map<String, Object>> listarViagens() {
        viagens = new ArrayList<Map<String, Object>>();
        List<Viagem> listarViagens = dao.listarViagens();

        for (Viagem viagem: listarViagens){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("id", viagem.getId());

            if(viagem.getTipoViagem() == Constantes.VIAGEM_LAZER){
                item.put("imagem", R.drawable.lazer);
            }else{
                item.put("imagem", R.drawable.negocios);
            }

            item.put("destino", viagem.getDestino());

            String periodo = dateFormat.format(viagem.getDataChegada()) + " a "
                    + dateFormat.format(viagem.getDataSaida());

            item.put("data", periodo);

            double totalGasto = dao.calcularTotalGasto(viagem);

            item.put("total", "Gasto total: R$ " + totalGasto);

            double alerta = viagem.getOrcamento() * valorLimite / 100;
            Double[] valores = new Double[]{viagem.getOrcamento(), alerta,totalGasto};
            item.put("barraProgresso", valores);
            viagens.add(item);
        }
        return viagens;
    }

    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation){
        if(view.getId() == R.id.barraProgresso){
            Double valores [] = (Double[]) data;
            ProgressBar progressBar = (ProgressBar) view;
            progressBar.setMax(valores[0].intValue());
            progressBar.setSecondaryProgress(valores[1].intValue());
            progressBar.setProgress(valores[2].intValue());
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        /*this.viagemSelecionada = position;
        alertDialog.show();*/

        if(modoSelecionarViagem){
            String destino = (String) viagens.get(position).get("destino");
            String idViagem = (String) viagens.get(position).get("id");

            Intent data = new Intent();
            data.putExtra(Constantes.VIAGEM_ID, idViagem);
            data.putExtra(Constantes.VIAGEM_DESTINO, destino);
            setResult(Activity.RESULT_OK, data);
            finish();
        }else{
            viagemSelecionada = position;
            alertDialog.show();
        }

       /* Map<String, Object> map = viagens.get(position);
        String destino = (String) map.get("destino");
        String mensagem = "Viagem selecionada: " + destino;

        Toast.makeText(this,mensagem,Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GastoListActivity.class));*/
    }

    private AlertDialog criaDialogConfirmacao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);

        return builder.create();
    }

    private AlertDialog criaAlertDialog(){
        final CharSequence [] items = {
                getString(R.string.editar),
                getString(R.string.novo_gasto),
                getString(R.string.gastos_realizados),
                getString(R.string.remover)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        builder.setItems(items, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int item) {
        Intent intent;
        Integer identificador = (Integer) viagens.get(viagemSelecionada).get("id");
        String id = "" + identificador;
        switch (item){
            case 0://editar Viagem
                intent = new Intent(this, NovaViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 1://novo gasto
                String destino = viagens.get(viagemSelecionada).get("destino").toString();
                intent = new Intent(this, GastoActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                intent.putExtra(Constantes.VIAGEM_DESTINO, destino);
                startActivity(intent);
                break;
            case 2://listar gastos
                intent = new Intent(this, GastoListActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 3://confirmação de exclusão
                dialogConfirmacao.show();
                break;
            case DialogInterface.BUTTON_POSITIVE: //exclusao
                viagens.remove(viagemSelecionada);
                dao.removerViagem(Long.valueOf(id));
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE: // cancela
                dialogConfirmacao.dismiss();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        dao.close();
        super.onDestroy();
    }
}
