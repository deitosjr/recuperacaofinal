package com.dh.recuperacao.service.impl;

import com.dh.recuperacao.exception.CadastroInvalidoException;
import com.dh.recuperacao.exception.ResourceNotFoundException;
import com.dh.recuperacao.model.dto.ClienteDTO;
import com.dh.recuperacao.repository.ClienteRepository;
import com.dh.recuperacao.model.Cliente;
import com.dh.recuperacao.service.IService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Log4j2
@Service
public class ClienteService implements IService<Cliente, ClienteDTO> {

    @Autowired
    ClienteRepository clienteRepository;

    public ClienteDTO buscar(Long id) throws ResourceNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("RG inexistente na base de dados, verifique."));
        return mapper.convertValue(cliente, ClienteDTO.class);
    }
    @Override
    public List<ClienteDTO> buscarTodos() {
        List<Cliente> clienteList = clienteRepository.findAll();
        List<ClienteDTO> clienteDTOList = new ArrayList<>();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        for(Cliente p : clienteList){
            clienteDTOList.add(mapper.convertValue(p, ClienteDTO.class));
        }
        return clienteDTOList;
    }
    @Override
    public ResponseEntity deletar(Long id) throws ResourceNotFoundException {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id não encontrado, verifique."));
        clienteRepository.deleteById(id);
        return new ResponseEntity("Cliente "+ cliente.getNome() + "excluído com sucesso.", HttpStatus.OK);
    }
    public Cliente salvar(ClienteDTO cliente) throws CadastroInvalidoException, ResourceNotFoundException {
        Optional<Cliente> clienteExists = clienteRepository.findByRg(cliente.getRg());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Cliente clienteSalvo = null;
        if(clienteExists.isEmpty()){
            Cliente cliente1 = mapper.convertValue(cliente, Cliente.class);
            clienteSalvo = clienteRepository.save(cliente1);
        }else{
            throw new CadastroInvalidoException("Rg já existente na base de dados.");
        }
            return clienteSalvo;
    }

    public ClienteDTO buscarPorRg(String rg) throws ResourceNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Optional<Cliente> cliente = clienteRepository.findByRg(rg);

        if(cliente.isEmpty()){
            return null;
        }
        return mapper.convertValue(cliente.get(), ClienteDTO.class);
    }

    public ClienteDTO alteracaoPacial(ClienteDTO clienteDTO){
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Optional<Cliente> clienteOptional = clienteRepository.findByRg(clienteDTO.getRg());
        if(clienteOptional.isEmpty()){
            return null;
        }
        Cliente cliente = clienteOptional.get();

        if(clienteDTO.getNome() != null){
            cliente.setNome(clienteDTO.getNome());
        }
        if(clienteDTO.getRg() != null){
            cliente.setRg(clienteDTO.getRg());
        }
        if(clienteDTO.getEndereco() != null){
            cliente.setEndereco(clienteDTO.getEndereco());
        }
        if(clienteDTO.getCpf() != null){
            cliente.setCpf(clienteDTO.getCpf());
        }

        return mapper.convertValue(clienteRepository.save(cliente), ClienteDTO.class);
    }

    public ClienteDTO alteracaoTotal(ClienteDTO clienteDTO) throws ResourceNotFoundException {

        Cliente cliente = clienteRepository.findByRg(clienteDTO.getRg()).orElseThrow(() -> {return new ResourceNotFoundException("RG inexistente na base de dados, verifique.");});
        Cliente clienteUpdate = cliente;
        clienteUpdate.setNome(clienteDTO.getNome());
        clienteUpdate.setCpf(clienteDTO.getCpf());
        clienteUpdate.setEndereco(clienteDTO.getEndereco());
        clienteUpdate.setRg(clienteDTO.getRg());
        clienteRepository.save(clienteUpdate);
        return clienteDTO;
    }
}
