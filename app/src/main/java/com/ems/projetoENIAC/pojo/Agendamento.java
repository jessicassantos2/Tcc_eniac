package com.ems.projetoENIAC.pojo;

import java.io.Serializable;

// POJO - Plain Old Java Objects
public class Agendamento implements Serializable {
    private int id;
    private String id_usuario;
    private String servico;
    private String data;
    private String hora;
    private String status;

    /**
     * Método construtor vazio
     */
    public Agendamento() {
    }

    /**
     * Método construtor da classe com assinatura
     *
     * @param id_usuario
     * @param servico
     * @param data
     * @param hora
     * @param status
     */
    public Agendamento(String id_usuario, String servico, String data, String hora, String status) {
        this.id_usuario = id_usuario;
        this.servico = servico;
        this.data = data;
        this.hora = hora;
        this.status = status;
    }

    /**
     * Método construtor da classe com assinatura
     *
     * @param id
     * @param id_usuario
     * @param servico
     * @param data
     * @param hora
     * @param status
     */
    public Agendamento(int id, String id_usuario, String servico, String data, String hora, String status) {
        this.id = id;
        this.id_usuario = id_usuario;
        this.servico = servico;
        this.data = data;
        this.hora = hora;
        this.status = status;
    }
    // Getters and Setters

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getId_usuario() {
        return id_usuario;
    }
    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getServico() {
        return servico;
    }
    public void setServico(String servico) {
        this.servico = servico;
    }

    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }
    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Método sobrescrito para retornar o nome do aluno na ListView
     *
     * @return
     */
    @Override
    public String toString() {
        return id_usuario;
    }

    /**
     * Método que retorna todos os dados de uma só vez
     *
     * @return
     */
    public String getDados() {
        return "ID: " + id + "\n" +
                "ID_usuario: " + id_usuario + "\n" +
                "Servico: " + servico + "\n" +
                "Data: " + data + "\n" +
                "Hora: " + hora + "\n" +
                "Status: " + status;
    }
}