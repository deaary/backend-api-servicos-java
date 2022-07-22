package com.soulcode.Servicos.Controllers;

import com.soulcode.Servicos.Models.Cliente;
import com.soulcode.Servicos.Models.Endereco;
import com.soulcode.Servicos.Services.ClienteService;
import com.soulcode.Servicos.Services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("servicos")
public class EnderecoController {

    @Autowired
    EnderecoService enderecoService;

    @GetMapping("/enderecos")
    public List<Endereco> mostrarTodosEnredecos(){
        List<Endereco> enderecos = enderecoService.mostrarTodosEnderecos();
        return enderecos;
    }

    @GetMapping("/enderecos/{idEndereco}")
    public ResponseEntity<Endereco> mostrarPeloId(@PathVariable Integer idEndereco) {
        Endereco endereco = enderecoService.mostrarPeloId(idEndereco);
        return ResponseEntity.ok().body(endereco);
    }

    @PostMapping("/enderecos/{idCliente}")
    public ResponseEntity<Endereco> cadastrarEnderecoDoCliente(@RequestBody Endereco endereco,
                                                               @PathVariable Integer idCliente){
        try {
            endereco = enderecoService.cadastrarEnderecoDoCliente(endereco, idCliente);
            URI novaURI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(endereco.getIdEndereco()).toUri();

            return ResponseEntity.created(novaURI).body(endereco);
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/enderecos/{idEndereco}")
    public ResponseEntity<Endereco> editarEndereco(@PathVariable Integer idEndereco,
                                                   @RequestBody Endereco endereco){
        endereco.setIdEndereco(idEndereco);
        enderecoService.editarEndereco(endereco);

        return ResponseEntity.ok().body(endereco);
    }

    @GetMapping("/enderecosPeloCliente/{idCliente}")
    public ResponseEntity<Cliente> mostrarPeloCliente(@PathVariable Integer idCliente){
        Cliente cliente = enderecoService.mostrarPeloCliente(idCliente);
        return ResponseEntity.ok().body(cliente);
    }
}
