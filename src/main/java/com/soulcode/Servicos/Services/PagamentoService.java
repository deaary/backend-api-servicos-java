package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Chamado;
import com.soulcode.Servicos.Models.Pagamento;
import com.soulcode.Servicos.Models.StatusPagamento;
import com.soulcode.Servicos.Repositories.ChamadoRepository;
import com.soulcode.Servicos.Repositories.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    ChamadoRepository chamadoRepository;

    @Autowired
    PagamentoRepository pagamentoRepository;

    @Cacheable("pagamentosCache")
    public List<Pagamento> mostrarTodosPagamentos() {
        return pagamentoRepository.findAll();
    }

    @Cacheable(value = "pagamentosCache", key = "#idPagamento")
    public Pagamento mostrarPagamentoPeloId(Integer idPagamento) {
        Optional<Pagamento> pagamento = pagamentoRepository.findById(idPagamento);
        return pagamento.orElseThrow();
    }

    @CachePut(value = "pagamentosCache", key = "#idChamado")
    public Pagamento cadastrarPagamentoDoChamado(Pagamento pagamento, Integer idChamado) throws Exception{
        Optional<Chamado> chamado = chamadoRepository.findById(idChamado);
        if(chamado.isPresent()) {
            pagamento.setIdPagamento(idChamado);
            pagamentoRepository.save(pagamento);

            chamado.get().setPagamento(pagamento);
            chamadoRepository.save(chamado.get());
            return pagamento;
        }else{
            throw new Exception();
        }
    }

    @CachePut(value = "pagamentosCache", key = "#idPagamento")
    public Pagamento modificarStatus(Integer idPagamento, String status){
        Pagamento pagamento = mostrarPagamentoPeloId(idPagamento);

        if (pagamento.getChamado() != null) {
            switch (status) {
                case "LANCADO":
                    pagamento.setStatusPagamento(StatusPagamento.LANCADO);
                    break;
            }
        }
            switch (status) {
                case "QUITADO":
                    pagamento.setStatusPagamento(StatusPagamento.QUITADO);
                    break;
            }
        return pagamentoRepository.save(pagamento);
    }

    @Cacheable(value = "pagamentosCache", key = "#status")
    public List<Pagamento> buscarPorStatus(String status) {
        return pagamentoRepository.findByStatus(status);
    }

    @CachePut(value = "pagamentosCache", key = "#idPagamento")
    public Pagamento editarPagamento(Pagamento pagamento, Integer idPagamento){
        Pagamento pagamentoSemAlteracao = mostrarPagamentoPeloId(idPagamento);
        Chamado chamado = pagamentoSemAlteracao.getChamado();

        pagamento.setChamado(chamado);
        return pagamentoRepository.save(pagamento);
    }

    public List<List> orcamentoComServicoCliente() {
        return Collections.singletonList(pagamentoRepository.orcamentoComServicoCliente());
    }
}
