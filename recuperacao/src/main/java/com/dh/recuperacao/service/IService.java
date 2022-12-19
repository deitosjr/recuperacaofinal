package com.dh.recuperacao.service;
import com.dh.recuperacao.exception.CadastroInvalidoException;
import com.dh.recuperacao.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface IService<T, A> {

    public T salvar (A t) throws CadastroInvalidoException, ResourceNotFoundException;

    public A buscar (Long id) throws ResourceNotFoundException;

//    public A alteracaoParcial(A a);

    public A alteracaoTotal(A a) throws ResourceNotFoundException;

    public List<A> buscarTodos();

    public ResponseEntity deletar(Long id) throws ResourceNotFoundException;
}