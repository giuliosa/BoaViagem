package br.aeso.boaviagem;

import android.app.Activity;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class DashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void selecionarOpcao(View view) {
        switch (view.getId()) {
            case R.id.nova_viagem:
                startActivity(new Intent(this, NovaViagemActivity.class));
                break;

            case R.id.novo_gasto:
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                boolean modoViagem = preferences.getBoolean(Constantes.MODO_VIAGEM, false);
                int viagemAtual = 1;
                String destino = "Recife";
                Intent intent = new Intent(this, GastoActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, viagemAtual);
                intent.putExtra(Constantes.VIAGEM_DESTINO, destino);
                startActivity(intent);
                break;

            case R.id.minhas_viagens:
                startActivity(new Intent(this, ViagemListActivity.class));
                break;
            case R.id.configuracoes:
                startActivity(new Intent(this, ConfiguracoesActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            String id = data.getExtras().getString(Constantes.VIAGEM_ID);
            String destino = data.getExtras().getString(Constantes.VIAGEM_DESTINO);

            Intent intent = new Intent(this, GastoActivity.class);
            intent.putExtra(Constantes.VIAGEM_ID, id);
            intent.putExtra(Constantes.VIAGEM_DESTINO, destino);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.erro_selecionar_viagem),
                    Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashbord_menu, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        finish();
        return true;
    }

}



