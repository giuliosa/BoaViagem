package br.aeso.boaviagem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


public class DashboardActivity extends AppCompatActivity {

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
                startActivity(new Intent(this, GastoActivity.class));
                break;

            case R.id.minhas_viagens:
                startActivity(new Intent(this, ViagemListActivity.class));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashbord_menu,menu);
        return true;
    }

    /*@Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        finish();
        return true;
    }*/

}



