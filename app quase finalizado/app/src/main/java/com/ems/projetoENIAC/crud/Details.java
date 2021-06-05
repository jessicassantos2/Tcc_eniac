package com.ems.projetoENIAC.crud;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ems.projetoENIAC.R;
import com.ems.projetoENIAC.pojo.Agendamento;

public class Details extends AppCompatActivity {
    Button btEditar;
    TextView id, emailUsuario, servico, data, hora, status;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Mostra um botão na Barra Superior para voltar
        getSupportActionBar().setTitle("Detalhes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        id = findViewById(R.id.id);
        emailUsuario = findViewById(R.id.id_usuario);
        servico = findViewById(R.id.servico);
        data = findViewById(R.id.data);
        hora = findViewById(R.id.hora);
        status = findViewById(R.id.status);
        btEditar = findViewById(R.id.btSalvar);

        Intent itAgendamento = getIntent();
        final Agendamento agendamento = (Agendamento) itAgendamento.getExtras().getSerializable("objAgendamento");
        id.setText(String.valueOf(agendamento.getId()));
        emailUsuario.setText(agendamento.getId_usuario());
        servico.setText(agendamento.getServico());
        data.setText(agendamento.getData());
        hora.setText(agendamento.getHora());
        status.setText(agendamento.getStatus());
        if (status.getText().toString().equals("APROVADO")){
            status.setTextColor(Color.parseColor("#FF20B120"));
        }else if (status.getText().toString().equals("REJEITADO")){
            status.setTextColor(Color.parseColor("#FFB12020"));
        }


        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editar = new Intent(getApplicationContext(), EditRecord.class);
                editar.putExtra("objAgendamento", agendamento);
                startActivity(editar);
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