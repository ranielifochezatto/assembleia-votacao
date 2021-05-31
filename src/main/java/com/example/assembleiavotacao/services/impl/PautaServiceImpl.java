package com.example.assembleiavotacao.services.impl;

import com.example.assembleiavotacao.components.Messages;
import com.example.assembleiavotacao.domains.Pauta;
import com.example.assembleiavotacao.exceptions.BusinessException;
import com.example.assembleiavotacao.repositories.PautaRepository;
import com.example.assembleiavotacao.services.PautaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PautaServiceImpl implements PautaService {

    private final Messages messages;
    private final PautaRepository repository;

    @Override
    public Optional<Pauta> findById(Long id) {
        log.debug("into findById method");
        return repository.findById(id);
    }

    @Override
    public Pauta save(Pauta pauta) {
        log.debug("into findById method");
        return repository.save(pauta);
    }

    @Override
    public Pauta abrirSessao(Long codigoPauta, Long tempoDuracaoEmMinutos) {
        log.debug("into abrirSessao method");

        Optional<Pauta> optionalPauta = this.findById(codigoPauta);
        Pauta pauta = optionalPauta.orElseThrow(()-> new EmptyResultDataAccessException(1));

        if(pauta.getDataAbertura() != null){
            throw new BusinessException(messages.get("pauta.sessao-desta-pauta-ja-foi-aberta"));
        }

        if(tempoDuracaoEmMinutos == null){
            //caso nao seja informado a duracao, o default sera 1 minuto
            tempoDuracaoEmMinutos = 1L;
        }

        LocalDateTime dataAbertura = LocalDateTime.now();
        pauta.setDataAbertura(dataAbertura);
        pauta.setDataEncerramento(dataAbertura.plusMinutes(tempoDuracaoEmMinutos));

        return this.save(pauta);
    }
}
