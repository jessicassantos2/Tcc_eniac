package com.ems.projetoENIAC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ems.projetoENIAC.crud.login;
import com.ems.projetoENIAC.pojo.Agendamento;

import java.util.ArrayList;

public class ListaAgendamentosCliente extends AppCompatActivity {
    ListView listViewAgendamentos;
    ArrayList<Agendamento> agendamentos = new ArrayList<>();
    ArrayAdapter<Agendamento> adaptador;
    SQLiteDatabase db;
    String user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_agendamentos_cliente);

        Intent in = new Intent(ListaAgendamentosCliente.this, usuarioPadrao.class);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                user = null;
            } else {
                user = extras.getString("usuario");
            }
        } else {
            user = (String) savedInstanceState.getSerializable("usuario");
        }
        Toast.makeText(this, user, Toast.LENGTH_SHORT).show();


        // Mostra um botão na Barra Superior para voltar
        getSupportActionBar().setTitle("Listagem Geral");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Abreo banco de dados existente
        db = openOrCreateDatabase("db_agendamento", Context.MODE_PRIVATE, null);

        listViewAgendamentos = findViewById(R.id.listagem);

        // Carrega os registros em ordem alfabética no ArrayList para anexar ao adaptador
        agendamentos.clear();
        Cursor c = db.rawQuery("SELECT * FROM agendamento WHERE id_usuario = '"+ user +"' ORDER BY id DESC", null);
        while (c.moveToNext()) {
            agendamentos.add(new Agendamento(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getString(5)));
        }
        // Configura o adaptador
        adaptador = new ArrayAdapter<>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                agendamentos);


        // Anexa o adaptador à ListView
        listViewAgendamentos.setAdapter(adaptador);

        listViewAgendamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Agendamento agendamento = (Agendamento) listViewAgendamentos.getItemAtPosition(position);
                Intent itAgendamento = new Intent(getApplicationContext(), AgendamentosCliente.class);
                itAgendamento.putExtra("objAgendamento", agendamento);
                startActivity(itAgendamento);
            }
        });
    }

    // Configura o botão (seta) na ActionBar (Barra Superior)
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}