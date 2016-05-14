package br.aeso.boaviagem;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import br.aeso.boaviagem.br.aeso.boaviagem.dao.BoaViagemDAO;

public class GastoActivity extends Activity {
    private Spinner categoria;
    private int ano, mes, dia;
    private Button dataGasto;
    private TextView destino;
    private EditText valor;
    private EditText descricao;
    private EditText local;
    private Date data;
    private BoaViagemDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasto);

        dao = new BoaViagemDAO(this);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataGasto = (Button) findViewById(R.id.data);
        dataGasto.setText(dia + "/" + (mes +1) + "/" + ano);


        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.categoria_gasto,
                        android.R.layout.simple_spinner_item);
        categoria = (Spinner) findViewById(R.id.categoria);
        categoria.setAdapter(adapter);

        String viagemDestino = getIntent().getExtras().getString(Constantes.VIAGEM_DESTINO);
        destino = (TextView) findViewById(R.id.destino);
        destino.setText(viagemDestino);

        valor = (EditText) findViewById(R.id.valor);
        descricao = (EditText) findViewById(R.id.descricao);
        local = (EditText) findViewById(R.id.local);
    }

    public void selecionarData(View view){
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if (R.id.data == id){
            return new DatePickerDialog(this,listener,ano,mes,dia);
        }
        return null;
    }

    private OnDateSetListener listener = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dataGasto.setText(dia + "/" + (mes+1) + "/" + ano);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gasto_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item){
        switch(item.getItemId()){
            case R.id.remover_gasto:
                //remover gasto do banco de dados que ainda não existe;
                return true;
            default:
                return super.onMenuItemSelected(featureId,item);
        }
    }
}
