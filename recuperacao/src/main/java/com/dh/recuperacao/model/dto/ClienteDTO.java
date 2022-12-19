package com.dh.recuperacao.model.dto;
import com.dh.recuperacao.model.Endereco;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClienteDTO {
    @JsonIgnore
    private int Id;
    private String nome;
    private String cpf;
    private String rg;
    private LocalDate dataCadastro;
    private Endereco endereco;
}
