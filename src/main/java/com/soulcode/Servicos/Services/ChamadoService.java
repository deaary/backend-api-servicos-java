package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Chamado;
import com.soulcode.Servicos.Models.Cliente;
import com.soulcode.Servicos.Models.Funcionario;
import com.soulcode.Servicos.Models.StatusChamado;
import com.soulcode.Servicos.Repositories.ChamadoRepository;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Repositories.FuncionarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {

    @Autowired
    ChamadoRepository chamadoRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Cacheable("chamadosCache")
    public List<Chamado> mostrarTodosChamados(){
        return chamadoRepository.findAll();
    }

    @Cacheable(value = "chamadosCache", key = "#idChamado")
    public Chamado mostrarUmChamadoPeloId(Integer idChamado){
        Optional<Chamado> chamado = chamadoRepository.findById(idChamado);
        return chamado.orElseThrow();
    }

    @Cacheable(value = "chamadosCache", key = "#idFuncionario")
    public List<Chamado> buscarChamadosPeloFuncionario(Integer idFuncionario) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        return chamadoRepository.findByFuncionario(funcionario);
    }

    @Cacheable(value = "chamadosCache", key = "#idCliente")
    public List<Chamado> buscarChamadosPeloCliente(Integer idCliente) {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        return chamadoRepository.findByCliente(cliente);
    }

    @Cacheable(value = "chamadosCache", key = "#status")
    public List<Chamado>buscarChamadosPeloStatus(String status){
        return chamadoRepository.findByStatus(status);
    }

    @Cacheable(value = "chamadosCache", key = "T(java.util.Objects).hash(#data1, #data2)")
    public List<Chamado> buscarPorIntervaloData(Date data1, Date data2){
        return chamadoRepository.findByIntervaloData(data1, data2);
    }

    // cadastrar um novo chamado, temos 3 regras
    // 1- no momento do cadastro do chamado, ja devemos informar de qual cliente que é
    // 2- no momento do cadastro do chamado, a principio vamos fazer esse cadastro sem estar atribuido a um funcionario
    // 3- no momento do cadastro do chamado, o status desse chamado deve ser RECEBIDO

    // servico para cadastro de novo chamado

    @CachePut(value = "chamadosCache", key = "#idCliente")
    public Chamado cadastrarChamado(Chamado chamado, Integer idCliente) {
        chamado.setStatus(StatusChamado.RECEBIDO); // regra 3 - atribuicao do status recebido para o chamado que esta sendo cadastrado
        chamado.setFuncionario(null); // regra 2 - dizer que ainda nao atribuimos esse chamado pra nenhum funcionario
        Optional<Cliente> cliente = clienteRepository.findById(idCliente); // regra 1 - buscando os dados do cliente dono do chamado
        chamado.setCliente(cliente.get());
        return chamadoRepository.save(chamado);
    }

    // metodo para atribuir um funcionario para um determidado chamado
    // ou trocar o funcionario de determinado chamado
    // -> regra -> no momento em que um determinado chamado é atribuido a um funcionario
    // o status do chamado precisa ser alterado para ATRIBUIDO
        @CachePut(value = "chamadosCache", key = "#idFuncionario")
        public Chamado atribuirFuncionario(Integer idChamado, Integer idFuncionario){
        //buscar os dados do funcionario que vai ser atribuido a esse chamado
        Optional<Funcionario> funcionario = funcionarioRepository.findById(idFuncionario);
        // buscar o chamado para o qual vai ser especificado o funcionario escolhido
        Chamado chamado = mostrarUmChamadoPeloId(idChamado);
        chamado.setFuncionario(funcionario.get());
        chamado.setStatus(StatusChamado.ATRIBUIDO);

        return chamadoRepository.save(chamado);
    }


    // Metodo para exclusao de um chamado
    @CacheEvict(value = "chamadosCache", key = "#idChamado", allEntries = true)
    public void excluirChamado(Integer idChamado) {
        chamadoRepository.deleteById(idChamado);
    }

    @CachePut(value = "chamadosCache", key = "#idChamado")
    public Chamado editarChamado(Chamado chamado, Integer idChamado){
        Chamado chamadoSemNovasAlteracoes = mostrarUmChamadoPeloId(idChamado);
        Funcionario funcionario = chamadoSemNovasAlteracoes.getFuncionario();
        Cliente cliente = chamadoSemNovasAlteracoes.getCliente();
        chamado.setCliente(cliente);
        chamado.setFuncionario(funcionario);
        return chamadoRepository.save(chamado);
    }

    // metodo para modificar o status de um chamado
    @Cacheable(value = "chamadosCache", key = "#idChamado")
    public Chamado modificarStatus(Integer idChamado, String status) {
        Chamado chamado = mostrarUmChamadoPeloId(idChamado);

        if (chamado.getFuncionario() != null) {
            switch (status) {
                case "ATRIBUIDO":
                    chamado.setStatus(StatusChamado.ATRIBUIDO);
                    break;

                case "CONCLUIDO":
                    chamado.setStatus(StatusChamado.CONCLUIDO);
                    break;


                case "ARQUIVO":
                    chamado.setStatus(StatusChamado.ARQUIVADO);
                    break;
            }
        }

                switch (status) {
                    case "RECEBIDO":
                        chamado.setStatus(StatusChamado.RECEBIDO);
                        break;

                }
                return chamadoRepository.save(chamado);
            }
}
