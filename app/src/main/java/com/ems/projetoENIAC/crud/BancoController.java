package com.ems.projetoENIAC.crud;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BancoController {

    private SQLiteDatabase db;
    private CriaBanco banco;

    public BancoController(Context context) {
        banco = new CriaBanco(context);
    }



    public String insereDadosUsuario(String nome, String email,String telefone, String senha) {
        ContentValues valores;
        long resultado;
        db = banco.getWritableDatabase();

        valores = new ContentValues();
        valores.put("nome", nome);
        valores.put("email", email);
        valores.put("telefone", telefone);
        valores.put("senha", senha);

        resultado = db.insert("usuarios", null, valores);
        db.close();

        if (resultado == -1)
            return "Erro ao inserir registro os dados, tente novamente!";
        else
            return "Dados do Usuário cadastrado com sucesso!";
    }

    public Cursor carregaDadosLogin(String Login, String SenhaLogin) {
        Cursor cursor;
        String[] campos = { "codigo", "nome", "email","telefone", "senha" };
        String where = "email = '" + Login + "' and senha = '" + SenhaLogin + "'";
        db = banco.getReadableDatabase();
        cursor = db.query("usuarios", campos, where, null, null, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        db.close();
        return cursor;
    }

    public Cursor CarregaAgendamentoAprovado(String Data, String Hora) {
        Cursor cursor;
        String[] campos = { "id", "id_usuario", "servico", "data", "hora", "status" };
        String where = "data = '" + Data + "' and hora = '" + Hora + "' and status = 'APROVADO'";
        db = banco.getReadableDatabase();
        cursor = db.query("agendamento", campos, where, null, null, null,
                null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        db.close();
        return cursor;
    }




//    public Cursor carregaDadoPeloCodigo(int id) {
//        Cursor cursor;
//        String[] campos = { "codigo", "nome", "email" };
//        String where = "codigo=" + id;
//        db = banco.getReadableDatabase();
//        cursor = db.query("contatos", campos, where, null, null, null,
//                null, null);
//        if (cursor != null) {
//            cursor.moveToFirst();
//        }
//
//        db.close();
//        return cursor;
//    }
//

//    public String excluirDado(int id){
//        String msg = "Registro Excluído" ;
//
//        db = banco.getReadableDatabase();
//
//        String condicao = "codigo = " + id ;
//
//        int linhas ;
//        linhas = db.delete("contatos", condicao, null) ;
//
//        if ( linhas < 1) {
//            msg = "Erro ao Excluir" ;
//        }
//
//        db.close();
//        return msg;
//    }

}

