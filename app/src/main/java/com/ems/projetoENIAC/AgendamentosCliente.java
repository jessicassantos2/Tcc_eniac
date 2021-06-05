package com.ems.projetoENIAC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ems.projetoENIAC.crud.EditRecord;
import com.ems.projetoENIAC.pojo.Agendamento;

public class AgendamentosCliente extends AppCompatActivity {
    Button btEditar;
    TextView id, emailUsuario, servico, data, hora, status;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamentos_cliente);
        // Mostra um botão na Barra Superior para voltar
        getSupportActionBar().setTitle("Detalhes cliente");
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