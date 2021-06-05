package com.ems.projetoENIAC;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ems.projetoENIAC.crud.Insert;
import com.ems.projetoENIAC.crud.ListAll;
import com.ems.projetoENIAC.crud.login;

public class MainActivity extends AppCompatActivity {

    // Declaração dos botões
    Button btInsert, btList, btSearch, btExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent(MainActivity.this, login.class);
        String user;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                user= null;
            } else {
                user= extras.getString("usuario");
            }
        } else {
            user= (String) savedInstanceState.getSerializable("usuario");
        }
        Toast.makeText(this, user, Toast.LENGTH_SHORT).show();


        // Associa o botão de inserção e configura o evento do clique para abrir a tela de inclusão
        btInsert = findViewById(R.id.btMainInsert);
        btInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent insert = new Intent(getApplicationContext(), Insert.class);
                startActivity(insert);
            }
        });

        // Associa o botão e configura a ação para abrr a tela de buscas
        btList = findViewById(R.id.btMainList);
        btList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent insert = new Intent(getApplicationContext(), ListAll.class);
                startActivity(insert);
            }
        });

        // Associa e configura o botão para sair da aplicação
        btExit = findViewById(R.id.btMainExit);
        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finaliza a aplicação e remove da pilha
                finishAffinity();
            }
        });
    }
}
