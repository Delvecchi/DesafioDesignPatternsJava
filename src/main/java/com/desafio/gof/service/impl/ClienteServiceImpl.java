package com.desafio.gof.service.impl;

import com.desafio.gof.model.ClienteModel;
import com.desafio.gof.model.EnderecoModel;
import com.desafio.gof.repository.ClienteRepository;
import com.desafio.gof.repository.EnderecoRepository;
import com.desafio.gof.service.ClienteService;
import com.desafio.gof.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    // Strategy: Implementar os métodos definidos na interface.
    // Facade: Abstrair integrações com subsistemas, provendo uma interface simples.

    @Override
    public Iterable<ClienteModel> buscarTodos() {
        // Buscar todos os Clientes.
        return clienteRepository.findAll();
    }

    @Override
    public ClienteModel buscarPorId(Long id) {
        // Buscar Cliente por ID.
        Optional<ClienteModel> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(ClienteModel cliente) {
        salvarClienteComCep(cliente);
    }

    @Override
    public void atualizar(Long id, ClienteModel cliente) {
        // Buscar Cliente por ID, caso exista:
        Optional<ClienteModel> clienteBd = clienteRepository.findById(id);
        if (clienteBd.isPresent()) {
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
        // Deletar Cliente por ID.
        clienteRepository.deleteById(id);
    }

    private void salvarClienteComCep(ClienteModel cliente) {
        // Verificar se o Endereco do Cliente já existe (pelo CEP).
        String cep = cliente.getEndereco().getCep();
        EnderecoModel endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            // Caso não exista, integrar com o ViaCEP e persistir o retorno.
            EnderecoModel novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        // Inserir Cliente, vinculando o Endereco (novo ou existente).
        clienteRepository.save(cliente);
    }

}
