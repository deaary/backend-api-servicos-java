package com.soulcode.Servicos.Services;


import ch.qos.logback.core.encoder.EchoEncoder;
import com.soulcode.Servicos.Models.Cargo;
import com.soulcode.Servicos.Models.Funcionario;
import com.soulcode.Servicos.Repositories.CargoRepository;
import com.soulcode.Servicos.Repositories.FuncionarioRepository;
import com.soulcode.Servicos.Services.Exceptions.DataIntegrityViolationException;
import com.soulcode.Servicos.Services.Exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// quando se fala em servicos estamos falando dos metodos do crud da tabela
@Service
public class FuncionarioService {

    //aqui se faz a injecao de dependencia
    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    CargoRepository cargoRepository;

    // primeiro servico na tabela de funcionario vai ser a leitura de todos os funcionarios cadastrado
    //findALL -> metodo do spring Data JPA -> busca todos os registros de uma tabela

    public List<Funcionario> mostrarTodosFuncionarios(){

        return funcionarioRepository.findAll();
    }


    // vamos criar mais um servico relacionado ao funcionario
    //criar um servico de buscar apenas um funcionario pelo seu id(chave primaria)

    public Funcionario mostrarUmFuncionarioPeloId(Integer idFuncionario){
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        return funcionario.orElseThrow(
        () -> new EntityNotFoundException("Nao tem funcionario cadastrado com o id: "+idFuncionario)
        );
    }

    // vamos criar mais um servico pra buscar um funcionario pelo seu email

    public Funcionario mostrarUmFuncionarioPeloEmail(String email){
        Optional<Funcionario> funcionario = funcionarioRepository.findByEmail(email);
                return funcionario.orElseThrow();
    }

    //vamos criar um servico para cadastrar um novo funcionario
    public Funcionario cadastrarFuncionario(Funcionario funcionario, Integer idCargo){
        // só por precaucao nos vamos colocar o id do funcionario como nulo
        try {
            funcionario.setIdFuncionario(null);
            Optional<Cargo> cargo = cargoRepository.findById(idCargo);
            funcionario.setCargo(cargo.get());
            return funcionarioRepository.save(funcionario);
        }catch (Exception e){
            throw new DataIntegrityViolationException("Erro ao cadastrar Funcionario");
        }
    }

    public List<Funcionario> mostrarTodosFuncionariosDeUmCargo(Integer idCargo){
        Optional<Cargo> cargo = cargoRepository.findById(idCargo);
        return funcionarioRepository.findByCargo(cargo);
    }

    public void excluirFuncionario(Integer idFuncionario){
        funcionarioRepository.deleteById(idFuncionario);
    }

    public void editarFuncionario(Funcionario funcionario){

        funcionarioRepository.save(funcionario);
    }

    public Funcionario salvarFoto(Integer idFuncionario, String caminhoFoto){
        Funcionario funcionario = mostrarUmFuncionarioPeloId(idFuncionario);
        funcionario.setFoto(caminhoFoto);
        return  funcionarioRepository.save(funcionario);
    }

}
