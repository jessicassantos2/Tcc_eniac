package com.ems.projetoENIAC;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.ems.projetoENIAC.crud.BancoController;
import com.ems.projetoENIAC.crud.Insert;
import com.ems.projetoENIAC.crud.login;
import com.ems.projetoENIAC.pojo.Agendamento;
import com.ems.projetoENIAC.utils.Message;

import java.text.DateFormat;
import java.util.Calendar;

public class usuarioPadrao extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    TextView dataView;
    EditText servico;
    Button btInserir, btDataPadrao, agendados;
    Spinner spinner2;
    SQLiteDatabase db;
    String user, horaEscolhida, servicoEscolhidoPadrao, dataEscolhidaPadrao;
    RadioGroup rdGroupPadrao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_padrao);
        servicoEscolhidoPadrao = "";


        dataView = findViewById(R.id.eDataPadrao);
        spinner2 = findViewById(R.id.spinnerUser);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.horarios, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(this);

        rdGroupPadrao = (RadioGroup) findViewById(R.id.rdGroupPadrao);
        rdGroupPadrao.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @SuppressLint("NonConstantResourceId")
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdSombrancelhaPadrao:
                        servicoEscolhidoPadrao = "Sombrancelha";
                        break;
                    case R.id.rdManicurePadrao:
                        servicoEscolhidoPadrao = "Manicure";
                        break;
                    case R.id.rdPedicurePadrao:
                        servicoEscolhidoPadrao = "Pedicure";
                        break;
                }
            }
        });



        btDataPadrao = findViewById(R.id.btDataPadrao);
        btDataPadrao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatepickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                Toast.makeText(usuarioPadrao.this, "", Toast.LENGTH_SHORT).show();
            }
        });



        Intent in = new Intent(usuarioPadrao.this, login.class);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                user = "";
            } else {
                user = extras.getString("usuario");
            }
        } else {
            user = (String) savedInstanceState.getSerializable("usuario");
        }
        Toast.makeText(this, user, Toast.LENGTH_SHORT).show();


        agendados = findViewById(R.id.agendados);
        agendados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tela1 = new Intent(usuarioPadrao.this, ListaAgendamentosCliente.class);
                tela1.putExtra("usuario", user);
                startActivity(tela1);
            }
        });





        // Abertura ou criação do Banco de Dados
        db = openOrCreateDatabase("db_agendamento", Context.MODE_PRIVATE, null);

        // Cria a tabela se não existir, senão carrega a tabela para uso
        db.execSQL("CREATE TABLE IF NOT EXISTS agendamento(" +
                "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'id_usuario' VARCHAR NOT NULL, " +
                "'servico' VARCHAR NOT NULL, " +
                "'data' VARCHAR NOT NULL, " +
                "'hora' VARCHAR NOT NULL, " +
                "'status' VARCHAR DEFAULT 'pendente');");

        // Mostra um botão na Barra Superior para voltar
        getSupportActionBar().setTitle("Inserir");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        servico = findViewById(R.id.rdManicure);
//        data = findViewById(R.id.eData);
//        new DateInputMask(data);
        btInserir = findViewById(R.id.btInserir);
        btInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacao();
            }
        });
    }

    public void validacao() {
        // validação dos campos

        boolean validador = false;


        if (dataView.getText().toString().equals("")){
            Toast.makeText(this,"Escolha uma data!", Toast.LENGTH_SHORT).show();
        }else if (horaEscolhida.equals("")){
            Toast.makeText(this,"Escolha um horário!", Toast.LENGTH_SHORT).show();
        }else if (servicoEscolhidoPadrao.equals("")){
            Toast.makeText(this,"Escolha um serviço!", Toast.LENGTH_SHORT).show();
        }

        else {
            validador = true;
        }

        if (validador){
            consultarAprovado();
        }
    }

    public void consultarAprovado() {
        String Data = dataEscolhidaPadrao;
        String Hora = horaEscolhida;

        BancoController bd = new BancoController(getBaseContext());

        Cursor dado = bd.CarregaAgendamentoAprovado(Data, Hora);

        if (dado.moveToFirst()) {
            String msg = "Já existe um agendamento nesse horário, escolha outro!";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }else {
            // Cria um objeto Aluno para receber os dados
            Agendamento agendamento = new Agendamento();
            agendamento.setId_usuario(user);
            agendamento.setServico(servicoEscolhidoPadrao);
            agendamento.setData(dataEscolhidaPadrao);
            agendamento.setHora(horaEscolhida);

            // Coleta os dados digitados nos campos
            ContentValues values = new ContentValues();
            values.put("id_usuario", agendamento.getId_usuario());
            values.put("servico", agendamento.getServico());
            values.put("data", agendamento.getData());
            values.put("hora", agendamento.getHora());

            // Insere os dados na tabela
            db.insert("agendamento", null, values);

            // Cria uma caixa de mensagem e mostra os dados incluídos
            Toast.makeText(this, "Pedido de agendamento realizado com sucesso!!!", Toast.LENGTH_SHORT).show();




        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        horaEscolhida = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dataView.setText(currentDateString);
        dataEscolhidaPadrao = currentDateString;
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


    public void clearText() {
        servico.setText("");
        dataView.setText("");
        rdGroupPadrao.clearCheck();
    }
}
