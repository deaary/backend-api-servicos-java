package com.soulcode.Servicos.Services;


import com.soulcode.Servicos.Models.Cliente;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Services.Exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// quando se fala em servicos estamos falando dos metodos do crud da tabela
@Service
public class ClienteService {

    //aqui se faz a injecao de dependencia
    @Autowired
    ClienteRepository clienteRepository;

    // primeiro servico na tabela de cliente vai ser a leitura de todos os clientes cadastrado
    //findALL -> metodo do spring Data JPA -> busca todos os registros de uma tabela
    @Cacheable("clientesCache") // so chama o return se o cache expirar / clientesCache::[]
    public List<Cliente> mostrarTodosClientes(){

        return clienteRepository.findAll();
    }


    // vamos criar mais um servico relacionado ao cliente
    //criar um servico de buscar apenas um cliente pelo seu id(chave primaria)
    @Cacheable(value = "clientesCache", key = "#idCliente") // clientesCache::1
    public Cliente mostrarUmClientePeloId(Integer idCliente){
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        return cliente.orElseThrow(
                () -> new EntityNotFoundException("Nao tem cliente cadastrado com o id: "+idCliente)
        );
    }

    // vamos criar mais um servico pra buscar um cliente pelo seu email

    @Cacheable(value = "clientesCache", key = "#email")
    public Cliente mostrarUmClientePeloEmail(String email){
        Optional<Cliente> cliente = clienteRepository.findByEmail(email);
        return cliente.orElseThrow();
    }

    //vamos criar um servico para cadastrar um novo cliente
    @CachePut(value = "clientesCache", key = "#cliente.idCliente")
    public Cliente cadastrarCliente(Cliente cliente){
        // só por precaucao nos vamos colocar o id do cliente como nulo
        cliente.setIdCliente(null);
        return clienteRepository.save(cliente);
    }

    @CacheEvict(value = "clientesCache", key = "#idCliente", allEntries = true)
    public void excluirCliente(Integer idCliente){
        mostrarUmClientePeloId(idCliente);
        clienteRepository.deleteById(idCliente);
    }

    @CachePut(value = "clientesCache", key = "#cliente.idCliente")
    public Cliente editarCliente(Cliente cliente){
        mostrarUmClientePeloId(cliente.getIdCliente());
        return clienteRepository.save(cliente);
    }



}
