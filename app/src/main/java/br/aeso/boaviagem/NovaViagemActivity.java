package br.aeso.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.aeso.boaviagem.br.aeso.boaviagem.dao.BoaViagemDAO;
import br.aeso.boaviagem.domain.Viagem;

public class NovaViagemActivity extends Activity {
    private int ano, mes, dia;
    private Button dataChegadaButton;
    private Button dataSaidaButton;
    private Date dataChegada, dataSaida;
    private DatabaseHelper helper;
    private EditText destino, quantidadePessoas, orcamento;
    private RadioGroup radioGroup;
    private String id;
    private BoaViagemDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_viagem);

        dao = new BoaViagemDAO(this);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataChegadaButton = (Button) findViewById(R.id.dataChegada);
        dataSaidaButton = (Button) findViewById(R.id.dataSaida);
        destino = (EditText) findViewById(R.id.destino);
        quantidadePessoas = (EditText) findViewById(R.id.quantidadePessoas);
        orcamento = (EditText) findViewById(R.id.orcamento);
        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);

        helper = new DatabaseHelper(this);

        id = getIntent().getStringExtra(Constantes.VIAGEM_ID);

        if(id != null){
            preparaEdicao();
        }
    }

    private void preparaEdicao(){
        /*SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT tipo_viagem, destino, data_chegada, " +
                "data_saida, quantidade_pessoas, orcamento " +
                "FROM viagem WHERE _id=?", new String[]{id});
        cursor.moveToFirst();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        if(cursor.getInt(0) == Constantes.VIAGEM_LAZER){
            radioGroup.check(R.id.lazer);
        } else {
            radioGroup.check(R.id.negocios);
        }*/

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Viagem viagem = dao.buscarViagemPorId(Integer.parseInt(id));

        destino.setText(viagem.getDestino());
        dataChegada = new Date(viagem.getDataChegada().getTime());
        dataSaida = new Date(viagem.getDataSaida().getTime());
        dataChegadaButton.setText(dateFormat.format(viagem.getDataChegada()));
        dataSaidaButton.setText(dateFormat.format(viagem.getDataSaida()));
        quantidadePessoas.setText(viagem.getQuantidadePessoas().toString());
        orcamento.setText(viagem.getOrcamento().toString());
        //cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viagem_menu, menu);
        return true;
    }

    public void selecionarDataChegada(View view){
        showDialog(view.getId());
    }

    public void selecionarDataSaida(View view) {
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if (R.id.dataChegada == id){
            return new DatePickerDialog(this,listenerChegada,ano,mes,dia);
        } else if(R.id.dataSaida == id){
            return new DatePickerDialog(this,listenerSaida,ano,mes,dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener listenerChegada = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            dataChegada = criarData(year,monthOfYear,dayOfMonth);
            dataChegadaButton.setText(dia + "/" + (mes + 1) + "/" + ano);
        }
    };

    private DatePickerDialog.OnDateSetListener listenerSaida = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            dataSaida = criarData(year,monthOfYear,dayOfMonth);
            dataSaidaButton.setText(dia + "/" + (mes + 1) + "/" + ano);
        }
    };

    private Date criarData(int year, int monthOfYear, int dayOfMonth){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,monthOfYear,dayOfMonth);
        return calendar.getTime();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item){
        switch(item.getItemId()){
            case R.id.novo_gasto:
                startActivity(new Intent(this, GastoActivity.class));
                return true;

            case R.id.remover_viagem:
                //remover viagem do banco de dados que ainda não existe;
                return true;
            default:
                return super.onMenuItemSelected(featureId,item);
        }
    }

    public void salvarViagem(View view){
        Viagem viagem = new Viagem();
        viagem.setDestino(destino.getText().toString());
        viagem.setOrcamento(Double.parseDouble(orcamento.getText().toString()));
        viagem.setQuantidadePessoas(Integer.parseInt(quantidadePessoas.getText().toString()));
        viagem.setDataChegada(dataChegada);
        viagem.setDataSaida(dataSaida);

        int tipo = radioGroup.getCheckedRadioButtonId();

        if(tipo == R.id.lazer) {
            viagem.setTipoViagem(Constantes.VIAGEM_LAZER);
            /*values.put("tipo_viagem", Constantes.VIAGEM_LAZER);*/
        } else {
            viagem.setTipoViagem(Constantes.VIAGEM_NEGOCIOS);
            /*values.put("tipo_viagem", Constantes.VIAGEM_NEGOCIOS);*/
        }
        /*SQLiteDatabase db = helper.getWritableDatabase();*/
        /*
        ContentValues values = new ContentValues();
        values.put("destino", destino.getText().toString());
        values.put("data_chegada", dataChegada.getTime());
        values.put("data_saida", dataSaida.getTime());
        values.put("orcamento", orcamento.getText().toString());
        values.put("quantidade_pessoas",
                quantidadePessoas.getText().toString());
        tipo = radioGroup.getCheckedRadioButtonId();

        if(tipo == R.id.lazer) {
            values.put("tipo_viagem", Constantes.VIAGEM_LAZER);
        } else {
            values.put("tipo_viagem", Constantes.VIAGEM_NEGOCIOS);
        }*/

        long resultado;

        if(id == null){
            /*resultado = dao.insert("viagem", null, values);*/
            resultado = dao.inserirViagem(viagem);
        }   else {
            Toast.makeText(this, viagem.getQuantidadePessoas(), Toast.LENGTH_SHORT).show();
            resultado = dao.editarViagem(viagem);
        }


        if(resultado != -1){
            Toast.makeText(this, getString(R.string.registro_salvo), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, getString(R.string.erro_salvar), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onDestroy() {
        helper.close();
        super.onDestroy();
    }
}
