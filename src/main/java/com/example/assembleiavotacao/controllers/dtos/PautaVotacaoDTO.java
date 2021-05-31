package com.example.assembleiavotacao.controllers.dtos;

import com.example.assembleiavotacao.enumeration.EnumVoto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PautaVotacaoDTO {

    @EqualsAndHashCode.Include
    private Long id;

    private LocalDateTime dataCadastro;

    @NotBlank
    private String numeroCpfAssociado;

    @NotNull
    private EnumVoto voto;

    private PautaDTO pauta;

}
