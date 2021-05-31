package com.example.assembleiavotacao.controllers;

import com.example.assembleiavotacao.controllers.dtos.PautaDTO;
import com.example.assembleiavotacao.controllers.mappers.PautaMapper;
import com.example.assembleiavotacao.domains.Pauta;
import com.example.assembleiavotacao.services.PautaService;
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
@RequestMapping("v1/pauta")
public class PautaController {

    private final PautaMapper pautaMapper;
    private final PautaService pautaService;

    @GetMapping(value = "/{codigoPauta}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PautaDTO> findById(@PathVariable Long codigoPauta){
        log.debug("into findById method");

        Optional<Pauta> optionalPauta = pautaService.findById(codigoPauta);
        Pauta pauta = optionalPauta.orElseThrow(()-> new EmptyResultDataAccessException(1));

        return ResponseEntity.ok(pautaMapper.toDto(pauta));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PautaDTO> insert(@Validated @RequestBody PautaDTO pautaDto){
        log.debug("into insert method");

        Pauta pauta = pautaMapper.toEntity(pautaDto);
        pauta.setId(null);
        pauta = pautaService.save(pauta);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(pauta.getId()).toUri();
        return ResponseEntity.created(uri).body(pautaMapper.toDto(pauta));
    }

    @PutMapping(value = "/{codigoPauta}/abrir-sessao", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<PautaDTO> abrirSessao(@PathVariable Long codigoPauta,
                                                @RequestParam(value = "tempoDuracaoEmMinutos", required = false) Long tempoDuracaoEmMinutos){
        log.debug("into abrirSessao method");

        Optional<Pauta> optionalPauta = pautaService.findById(codigoPauta);
        if(!optionalPauta.isPresent()){
            throw new EmptyResultDataAccessException(1);
        }

        Pauta pauta = pautaService.abrirSessao(codigoPauta, tempoDuracaoEmMinutos);

        return ResponseEntity.ok(pautaMapper.toDto(pauta));
    }

}
