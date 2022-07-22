package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Cliente;
import com.soulcode.Servicos.Models.Endereco;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Repositories.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Cacheable("enderecosCache")
    public List<Endereco> mostrarTodosEnderecos(){
        return enderecoRepository.findAll();
    }

    @Cacheable(value = "enderecosCache", key = "idEndereco")
    public Endereco mostrarPeloId(Integer idEndereco) {
        Optional<Endereco> endereco = enderecoRepository.findById(idEndereco);
        return endereco.orElseThrow();
    }

    // CADASTRO DE UM NOVO ENDERECO
    //regras/ 1) para cadastrar um endereco, o coliente ja deve estar cadastrado no database
    //        2) no momento do cadastro do endereco, precisamos passar o id do cliente dono desse endereco
    //        3) o id do endereco vai ser o mesmo id do cliente
    @CachePut(value = "enderecosCache", key = "#idCliente")
    public Endereco cadastrarEnderecoDoCliente(Endereco endereco, Integer idCliente) throws Exception{
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        if(cliente.isPresent()){
            endereco.setIdEndereco(idCliente);
            enderecoRepository.save(endereco);

            cliente.get().setEndereco(endereco);
            clienteRepository.save(cliente.get());
            return endereco;
        }else{
            throw new Exception();
        }
    }

    @CachePut(value = "enderecosCache", key = "#endereco.idEndereco")
    public Endereco editarEndereco(Endereco endereco){
        return enderecoRepository.save(endereco);
    }

    @Cacheable(value = "enderecosCache", key = "#idCliente")
    public Cliente mostrarPeloCliente(Integer idCliente) {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        return cliente.orElseThrow();
    }
}
