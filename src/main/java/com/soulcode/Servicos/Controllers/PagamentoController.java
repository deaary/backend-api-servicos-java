package com.soulcode.Servicos.Controllers;

import com.soulcode.Servicos.Models.Pagamento;
import com.soulcode.Servicos.Services.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("servicos")
public class PagamentoController {

    @Autowired
    PagamentoService pagamentoService;

    @GetMapping("/pagamentosChamadosComCliente")
    public List<List> orcamentoComServicoCliente() {
        List<List> pagamentos = pagamentoService.orcamentoComServicoCliente();
        return pagamentos;
    }

    @GetMapping("/pagamentos")
    public List<Pagamento> mostrarTodosPagamentos(){
        return pagamentoService.mostrarTodosPagamentos();
    }

    @GetMapping("/pagamentos/{idPagamento}")
    public ResponseEntity<Pagamento> mostrarPagamentoPeloId(@PathVariable Integer idPagamento){
        Pagamento pagamento = pagamentoService.mostrarPagamentoPeloId(idPagamento);
        return ResponseEntity.ok().body(pagamento);
    }

    @PostMapping("/pagamentos/{idChamado}")
    public ResponseEntity<Pagamento> cadastrarPagamentoDoChamado(@RequestBody Pagamento pagamento,
                                                                 @PathVariable Integer idChamado){
        try {
            pagamento = pagamentoService.cadastrarPagamentoDoChamado(pagamento, idChamado);
            URI novaURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(pagamento.getIdPagamento()).toUri();
            return ResponseEntity.created(novaURI).body(pagamento);
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/pagamentosModificarStatus/{idPagamento}")
    public ResponseEntity<Pagamento> modificarStatus(@PathVariable Integer idPagamento,
                                                     @RequestParam("status") String status) {
        Pagamento pagamento = pagamentoService.modificarStatus(idPagamento, status);
        return ResponseEntity.ok().body(pagamento);
    }

    @GetMapping("/pagamentosPeloStatus")
    public List<Pagamento> buscarPorStatus(@RequestParam("status") String status){
        List<Pagamento> pagamentos = pagamentoService.buscarPorStatus(status);
        return pagamentos;
    }

    @PutMapping("/pagamentos/{idPagamento}")
    public ResponseEntity<Pagamento> editarPagamento(@PathVariable Integer idPagamento,
                                                     @RequestBody Pagamento pagamento){
        pagamento.setIdPagamento(idPagamento);
        pagamentoService.editarPagamento(pagamento, idPagamento);

        return ResponseEntity.ok().body(pagamento);
    }
}
