package com.example.assembleiavotacao.controllers;

import com.example.assembleiavotacao.controllers.dtos.PautaDTO;
import com.example.assembleiavotacao.controllers.dtos.PautaVotacaoDTO;
import com.example.assembleiavotacao.controllers.mappers.PautaVotacaoMapper;
import com.example.assembleiavotacao.domains.Pauta;
import com.example.assembleiavotacao.domains.PautaVotacao;
import com.example.assembleiavotacao.services.PautaService;
import com.example.assembleiavotacao.services.PautaVotacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("v1/pauta/{codigoPauta}/votacao")
public class PautaVotacaoController {

    private final PautaService pautaService;
    private final PautaVotacaoMapper pautaVotacaoMapper;
    private final PautaVotacaoService pautaVotacaoService;


    @GetMapping(value = "/{codigoPautaVotacao}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PautaVotacaoDTO> findById(@PathVariable Long codigoPauta,
                                                    @PathVariable Long codigoPautaVotacao){
        log.debug("into findById method");

        Optional<PautaVotacao> optionalPautaVotacao = pautaVotacaoService.findById(codigoPauta);
        PautaVotacao pautaVotacao = optionalPautaVotacao.orElseThrow(()-> new EmptyResultDataAccessException(1));

        //verificando se o voto pertence a pauta informada no path
        if(!pautaVotacao.getPauta().getId().equals(codigoPauta)){
            throw new EmptyResultDataAccessException(1);
        }

        return ResponseEntity.ok(pautaVotacaoMapper.toDto(pautaVotacao));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PautaVotacaoDTO> insert(@PathVariable Long codigoPauta,
                                                  @Validated @RequestBody PautaVotacaoDTO pautaVotacaoDTO){
        log.debug("into insert method");

        Optional<Pauta> optionalPauta = pautaService.findById(codigoPauta);
        Pauta pauta = optionalPauta.orElseThrow(()-> new EmptyResultDataAccessException(1));

        PautaVotacao pautaVotacao = pautaVotacaoMapper.toEntity(pautaVotacaoDTO);
        pautaVotacao.setId(null);
        pautaVotacao.setPauta(pauta);
        pautaVotacao = pautaVotacaoService.save(pautaVotacao);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(pautaVotacao.getId()).toUri();
        return ResponseEntity.created(uri).body(pautaVotacaoMapper.toDto(pautaVotacao));
    }

}
