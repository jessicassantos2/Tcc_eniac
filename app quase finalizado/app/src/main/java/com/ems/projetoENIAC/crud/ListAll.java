package com.ems.projetoENIAC.crud;

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

import androidx.appcompat.app.AppCompatActivity;

import com.ems.projetoENIAC.R;
import com.ems.projetoENIAC.pojo.Agendamento;

import java.util.ArrayList;

public class ListAll extends AppCompatActivity {
    ListView listViewAgendamentos;
    ArrayList<Agendamento> agendamentos = new ArrayList<>();
    ArrayAdapter<Agendamento> adaptador;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Mostra um botão na Barra Superior para voltar
        getSupportActionBar().setTitle("Listagem Geral");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Abreo banco de dados existente
        db = openOrCreateDatabase("db_agendamento", Context.MODE_PRIVATE, null);

        listViewAgendamentos = findViewById(R.id.listagem);

        // Carrega os registros em ordem alfabética no ArrayList para anexar ao adaptador
        agendamentos.clear();
        Cursor c = db.rawQuery("SELECT * FROM agendamento ORDER BY id DESC", null);
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
                Intent itAgendamento = new Intent(getApplicationContext(), Details.class);
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