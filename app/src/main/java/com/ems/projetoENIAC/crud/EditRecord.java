package com.ems.projetoENIAC.crud;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ems.projetoENIAC.MainActivity;
import com.ems.projetoENIAC.R;
import com.ems.projetoENIAC.pojo.Agendamento;
import com.ems.projetoENIAC.utils.Message;

import java.util.Calendar;

public class EditRecord extends AppCompatActivity {

    TextView id, situacao;
    EditText emailUsuario, servico, data, hora, status;
    Button btSalvar, aprovar, rejeitar, Btexcluir;
    String idString;

    SQLiteDatabase db;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Mostra um botão na Barra Superior para voltar
        getSupportActionBar().setTitle("Edição");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        id = findViewById(R.id.id);
        emailUsuario = findViewById(R.id.id_usuario);
        servico = findViewById(R.id.servico);
        data = findViewById(R.id.data);
        hora = findViewById(R.id.hora);
        status = findViewById(R.id.status);
        btSalvar = findViewById(R.id.btSalvar);
        aprovar = findViewById(R.id.aprovar);
        rejeitar = findViewById(R.id.rejeitar);
        situacao = findViewById(R.id.situacao);

        status.setVisibility(View.INVISIBLE);

        aprovar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("APROVADO");
                situacao.setText("APROVADO");
                situacao.setTextColor(Color.parseColor("#FF20B120"));
                validacao();
            }
        });

        rejeitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status.setText("REJEITADO");
                situacao.setText("REJEITADO");
                situacao.setTextColor(Color.parseColor("#FFB12020"));
                validacao();
            }
        });


        final Intent itAgendamento = getIntent();
        final Agendamento agendamento = (Agendamento) itAgendamento.getExtras().getSerializable("objAgendamento");
        id.setText(String.valueOf(agendamento.getId()));
        idString = id.getText().toString();
        Toast.makeText(this, idString, Toast.LENGTH_SHORT).show();
        emailUsuario.setText(agendamento.getId_usuario());
        servico.setText(agendamento.getServico());
        data.setText(agendamento.getData());
        hora.setText(agendamento.getHora());
        status.setText(agendamento.getStatus());


        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Coleta os dados digitados nos campos
                ContentValues values = new ContentValues();
                values.put("id_usuario", emailUsuario.getText().toString());
                values.put("servico", servico.getText().toString());
                values.put("data", data.getText().toString());
                values.put("hora", hora.getText().toString());
                values.put("status", status.getText().toString());

                Agendamento novosDados = new Agendamento();
                novosDados.setId_usuario(emailUsuario.getText().toString());
                novosDados.setServico(servico.getText().toString());
                novosDados.setData(data.getText().toString());
                novosDados.setHora(hora.getText().toString());
                novosDados.setStatus(status.getText().toString());

                // Atualiza os dados na tabela
                db = openOrCreateDatabase("db_agendamento", Context.MODE_PRIVATE, null);
                db.execSQL("UPDATE agendamento SET " +
                        "id_usuario='" + novosDados.getId_usuario() + "'," +
                        "servico='" + novosDados.getServico() + "'," +
                        "data='" + novosDados.getData() + "'," +
                        "hora='" + novosDados.getHora() + "'," +
                        "status='" + novosDados.getStatus() + "' " +
                        "WHERE id=" + agendamento.getId()
                );

                // Cria uma caixa de mensagem e mostra os dados incluídos
                Toast.makeText(EditRecord.this, "Pedido de agendamento realizado com sucesso!!!", Toast.LENGTH_SHORT).show();




                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
            }
        });

        Btexcluir = findViewById(R.id.excluir);
        Btexcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmarExclusao();
            }
        });





    }

    public void confirmarExclusao() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Excluir").setMessage("Tem certeza que deseja excluir esse agendamento?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        excluir();
                        Toast.makeText(EditRecord.this, "Pedido excluido!",Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No", null).show();
    }


    public class DateInputMask implements TextWatcher {

        private String current = "";
        private String ddmmyyyy = "DDMMYYYY";
        private Calendar cal = Calendar.getInstance();
        private EditText input;

        public DateInputMask(EditText input) {
            this.input = input;
            this.input.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().equals(current)) {
                return;
            }

            String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
            String cleanC = current.replaceAll("[^\\d.]|\\.", "");

            int cl = clean.length();
            int sel = cl;
            for (int i = 2; i <= cl && i < 6; i += 2) {
                sel++;
            }
            //Fix for pressing delete next to a forward slash
            if (clean.equals(cleanC)) sel--;

            if (clean.length() < 8){
                clean = clean + ddmmyyyy.substring(clean.length());
            }else{
                //This part makes sure that when we finish entering numbers
                //the date is correct, fixing it otherwise
                int day  = Integer.parseInt(clean.substring(0,2));
                int mon  = Integer.parseInt(clean.substring(2,4));
                int year = Integer.parseInt(clean.substring(4,8));

                mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                cal.set(Calendar.MONTH, mon-1);
                year = (year<2020)?2020:(year>2022)?2022:year;
                cal.set(Calendar.YEAR, year);
                // ^ first set year for the line below to work correctly
                //with leap years - otherwise, date e.g. 29/02/2012
                //would be automatically corrected to 28/02/2012

                day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                clean = String.format("%02d%02d%02d",day, mon, year);
            }

            clean = String.format("%s/%s/%s", clean.substring(0, 2),
                    clean.substring(2, 4),
                    clean.substring(4, 8));

            sel = sel < 0 ? 0 : sel;
            current = clean;
            input.setText(current);
            input.setSelection(sel < current.length() ? sel : current.length());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public void validacao() {
        // validação dos campos

        boolean validador = false;



        if ((!hora.getText().toString().matches("^([01]?[0-9]|2[0-3]):[0-5][0-9]$"))) {
            Toast.makeText(this, "Hora invalida, tente outra", Toast.LENGTH_SHORT).show();
        }  if (!emailUsuario.getText().toString().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            Toast.makeText(this,"Email invalido, digite novamente!", Toast.LENGTH_SHORT).show();
        } else if (data.getText().toString().length()< 0) {
            data.setError("Informe a data do agendamento");
        }  else {
            validador = true;
        }

        if (validador){

        }
    }

    public void excluir(){
        db = openOrCreateDatabase("db_agendamento", Context.MODE_PRIVATE, null);
        db.execSQL("DELETE FROM agendamento WHERE id = '"+ idString + "'");


        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(main);
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