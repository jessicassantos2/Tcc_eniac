package com.ems.projetoENIAC.crud;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import com.ems.projetoENIAC.DatepickerFragment;
import com.ems.projetoENIAC.R;
import com.ems.projetoENIAC.pojo.Agendamento;
import com.ems.projetoENIAC.utils.Message;

import java.text.DateFormat;
import java.util.Calendar;

public class Insert extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {
    TextView dataView;
    EditText emailUsuario;
    Button btInserir, btData;
    Spinner spinner;
    SQLiteDatabase db;
    String horaEscolhida, servicoEscolhido, dataEscolhida;
    RadioGroup radioGrupo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        radioGrupo = (RadioGroup) findViewById(R.id.rdGroupPadrao);
        radioGrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @SuppressLint("NonConstantResourceId")
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdSombrancelhaPadrao:
                        servicoEscolhido = "Sombrancelha";
                        break;
                    case R.id.rdManicurePadrao:
                        servicoEscolhido = "Manicure";
                        break;
                    case R.id.rdPedicurePadrao:
                        servicoEscolhido = "Pedicure";
                        break;
                }
            }
        });

        spinner = findViewById(R.id.spinnerUser);
         ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.horarios, android.R.layout.simple_spinner_dropdown_item);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        emailUsuario = findViewById(R.id.editEmailUsuario);
        spinner = findViewById(R.id.spinnerUser);
        btInserir = findViewById(R.id.btInserir);
        dataView = findViewById(R.id.eDataPadrao);
        btData = findViewById(R.id.btDataPadrao);
        btData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatepickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
                Toast.makeText(Insert.this, "", Toast.LENGTH_SHORT).show();
            }
        });




//          new DateInputMask(data);

        // Abertura ou criação do Banco de Dados
        db = openOrCreateDatabase
                ("db_agendamento",
                        Context.MODE_PRIVATE,
                        null
                );

        // Cria a tabela se não existir, senão carrega a tabela para uso
        db.execSQL
                ("CREATE TABLE IF NOT EXISTS agendamento" +
                        "(" + "'id' INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "'id_usuario' VARCHAR NOT NULL, "
                        + "'servico' VARCHAR NOT NULL, "
                        + "'data' VARCHAR NOT NULL, "
                        + "'hora' VARCHAR NOT NULL, "
                        + "'status' VARCHAR DEFAULT 'PENDENTE');");


        btInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validacao();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        dataView.setText(currentDateString);
        dataEscolhida = currentDateString;
    }




    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        horaEscolhida = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    public void validacao() {
        // validação dos campos

        boolean validador = false;


        if (!emailUsuario.getText().toString().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            emailUsuario.setError("Email invalido, digite novamente!");
            validador = false;
        } else {
            validador = true;
        }

        if (validador == true) {
            consultarAprovado();
        } else Toast.makeText(this, "Revise os campos", Toast.LENGTH_SHORT).show();
    }

//    public class DateInputMask implements TextWatcher {
//
//        private String current = "";
//        private final String ddmmyyyy = "DDMMYYYY";
//        private final Calendar cal = Calendar.getInstance();
//        private final EditText input;
//
//        public DateInputMask(EditText input) {
//            this.input = input;
//            this.input.addTextChangedListener(this);
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            if (s.toString().equals(current)) {
//                return;
//            }
//
//            String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
//            String cleanC = current.replaceAll("[^\\d.]|\\.", "");
//
//            int cl = clean.length();
//            int sel = cl;
//            for (int i = 2; i <= cl && i < 6; i += 2) {
//                sel++;
//            }
//            //Fix for pressing delete next to a forward slash
//            if (clean.equals(cleanC)) sel--;
//
//            if (clean.length() < 8) {
//                clean = clean + ddmmyyyy.substring(clean.length());
//            } else {
//                //This part makes sure that when we finish entering numbers
//                //the date is correct, fixing it otherwise
//                int day = Integer.parseInt(clean.substring(0, 2));
//                int mon = Integer.parseInt(clean.substring(2, 4));
//                int year = Integer.parseInt(clean.substring(4, 8));
//
//                mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
//                cal.set(Calendar.MONTH, mon - 1);
//                year = (year < 2020) ? 2020 : (year > 2022) ? 2022 : year;
//                cal.set(Calendar.YEAR, year);
//                // ^ first set year for the line below to work correctly
//                //with leap years - otherwise, date e.g. 29/02/2012
//                //would be automatically corrected to 28/02/2012
//
//                day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
//                clean = String.format("%02d%02d%02d", day, mon, year);
//            }
//
//            clean = String.format("%s/%s/%s", clean.substring(0, 2),
//                    clean.substring(2, 4),
//                    clean.substring(4, 8));
//
//            sel = sel < 0 ? 0 : sel;
//            current = clean;
//            input.setText(current);
//            input.setSelection(sel < current.length() ? sel : current.length());
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    }


    public void consultarAprovado() {
        String Data = dataEscolhida;
        String Hora = horaEscolhida;

        BancoController bd = new BancoController(getBaseContext());

        Cursor dado = bd.CarregaAgendamentoAprovado(Data, Hora);

        if (dado.moveToFirst()) {
            String msg = "Já existe um agendamento nesse horário, escolha outro!";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        } else {
            // Cria um objeto Agendamento para receber os dados

            Agendamento agendamento = new Agendamento();
            agendamento.setId_usuario(emailUsuario.getText().toString());
            agendamento.setServico(servicoEscolhido);
            agendamento.setData(dataEscolhida);
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

            // Limpa os campos de entrada
            clearText();
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

    /**
     * Limpa os campos de entrada e fecha o teclado
     */
    public void clearText() {
        emailUsuario.setText("");
        radioGrupo.clearCheck();
        dataView.setText("");
        emailUsuario.requestFocus();

        // fecha o teclado virtual
        ((InputMethodManager) Insert.this.getSystemService(
                Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                getCurrentFocus().getWindowToken(), 0);
    }
}
