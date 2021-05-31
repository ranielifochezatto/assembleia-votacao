package com.example.assembleiavotacao.controllers.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PautaDTO {

    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String nomePauta;

    private LocalDateTime dataCadastro;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataEncerramento;
    private LocalDateTime dataApuracao;
    private Long numeroVotosFavor;
    private Long numeroVotosContra;
}
