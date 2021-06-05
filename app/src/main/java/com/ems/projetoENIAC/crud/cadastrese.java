package com.ems.projetoENIAC.crud;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ems.projetoENIAC.R;

public class cadastrese extends AppCompatActivity  implements View.OnClickListener {

    Button btnSalvar;
    EditText txtNomeCad, txtEmailCad,txtTelefoneCad, txtSenhaCad, txtConfSenhaCad;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrese);

        btnSalvar = findViewById(R.id.btnSalvar);
        txtNomeCad = findViewById(R.id.txtNomeCad);
        txtEmailCad =  findViewById(R.id.txtEmailCad);
        txtTelefoneCad = findViewById(R.id.txtTelefoneCad);
        txtSenhaCad = findViewById(R.id.txtSenhaCad);
        txtConfSenhaCad = findViewById(R.id.txtConfSenhaCad);

        btnSalvar.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        String NomeCad = txtNomeCad.getText().toString();
        String EmailCad = txtEmailCad.getText().toString();
        String TelefoneCad = txtTelefoneCad.getText().toString();
        String SenhaCad = txtSenhaCad.getText().toString();
        String ConfSenhaCad = txtConfSenhaCad.getText().toString();


        BancoController bd = new BancoController(getBaseContext());
        String resultado;



        // validação dos campos

        boolean validador = false;
        String numero = txtTelefoneCad.getText().toString();
        String expressao = "[1-9]{2}?[0-9]{8,9}";


        if (txtNomeCad.getText().toString().length()< 3){
            txtNomeCad.setError("Insira o seu nome!");
        } else if (!txtEmailCad.getText().toString().matches("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")) {
            txtEmailCad.setError("Email invalido, digite novamente!");
        }else if (!numero.matches(expressao)){
            txtTelefoneCad.setError("Telefone invalido, digite novamente!");
        } else if (txtSenhaCad.getText().toString().length()< 8) {
            txtSenhaCad.setError("Minimo 8 caracteres");
        } else if (!txtSenhaCad.getText().toString().equals(txtConfSenhaCad.getText().toString())){
            txtConfSenhaCad.setError("as senhas estão diferentes, digite novamente!");
        } else {
            validador = true;
        }

        if (validador){
            resultado = bd.insereDadosUsuario(NomeCad, EmailCad,TelefoneCad, SenhaCad);
            Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
            limpar();
            finish();


        }

    }
    public void limpar()
    {
        txtNomeCad.setText("");
        txtEmailCad.setText("");
        txtSenhaCad.setText("");
        txtConfSenhaCad.setText("");
        txtNomeCad.requestFocus();
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
