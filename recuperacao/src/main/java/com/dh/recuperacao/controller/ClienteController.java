package com.dh.recuperacao.controller;

import com.dh.recuperacao.exception.CadastroInvalidoException;
import com.dh.recuperacao.exception.ResourceNotFoundException;
import com.dh.recuperacao.model.Cliente;
import com.dh.recuperacao.model.dto.ClienteDTO;
import com.dh.recuperacao.service.impl.ClienteService;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@Log4j2
@CrossOrigin("*")
@RestController
@RequestMapping("/cliente")
public class ClienteController implements Serializable {

    @Autowired
    private ClienteService clienteService;

    @PostMapping()
    public ResponseEntity salvar(@RequestBody @Valid ClienteDTO cliente) throws ResourceNotFoundException, CadastroInvalidoException {
        try{
            Cliente clienteSalvo = clienteService.salvar(cliente);
            return new ResponseEntity(clienteSalvo, HttpStatus.CREATED);
        } catch (CadastroInvalidoException e) {
            throw new CadastroInvalidoException("Erro ao cadastrar Cliente: " + e.getMessage());
            log.info("[ClienteController] [salvar]");
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity buscar(@PathVariable Long id) throws ResourceNotFoundException {
        ClienteDTO clienteDTO = clienteService.buscar(id);
        return new ResponseEntity(clienteDTO, HttpStatus.FOUND);

    }
    @GetMapping("/buscarRg/{rg}")
    public ResponseEntity buscarPorRG(@PathVariable String rg) throws ResourceNotFoundException {
        ClienteDTO clienteDTO = clienteService.buscarPorRg(rg);
        return new ResponseEntity(clienteDTO, HttpStatus.FOUND);
    }

    @GetMapping("/buscarTodos")
    public List<ClienteDTO> getAllClientes(){
        return clienteService.buscarTodos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletar(@PathVariable Long id) throws ResourceNotFoundException {
        return clienteService.deletar(id);
    }

    @PutMapping
    public ResponseEntity alteracaoCompleta(@RequestBody ClienteDTO clienteDTO) throws ResourceNotFoundException {
        ClienteDTO clienteDTOChange = clienteService.alteracaoTotal(clienteDTO);
        return new ResponseEntity(clienteDTOChange, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity alteracaoParcial(@RequestBody @Valid ClienteDTO clienteDTO){
        ClienteDTO clienteDTOChange = clienteService.alteracaoPacial(clienteDTO);
        if(clienteDTOChange == null){
            return new ResponseEntity("Erro ao alterar Cliente", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("Cliente alterado com sucesso!", HttpStatus.OK);
    }
}