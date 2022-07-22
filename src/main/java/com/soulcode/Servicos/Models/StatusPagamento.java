package com.soulcode.Servicos.Models;

public enum StatusPagamento {

    LANCADO("Lançado"),
    QUITADO("Quitado");

    private String conteudo;

    StatusPagamento(String conteudo){
        this.conteudo = conteudo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
