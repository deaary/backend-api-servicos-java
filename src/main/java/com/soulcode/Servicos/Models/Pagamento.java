package com.soulcode.Servicos.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;

@Entity
public class Pagamento {

    @Id
    private Integer idPagamento;

    @NumberFormat(pattern = "#.##0,00")
    @Column(nullable = false)
    private  Double valor;

    @Column(nullable = false)
    private String formaPagamento;

    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;

    @JsonIgnore
    @OneToOne(mappedBy = "pagamento")
    private Chamado chamado;

    public Integer getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Integer idPagamento) {
        this.idPagamento = idPagamento;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public StatusPagamento getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(StatusPagamento statusPagamento) {
        this.statusPagamento = statusPagamento;
    }

    public Chamado getChamado() {
        return chamado;
    }

    public void setChamado(Chamado chamado) {
        this.chamado = chamado;
    }
}
