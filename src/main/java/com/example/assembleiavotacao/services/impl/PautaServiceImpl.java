package com.example.assembleiavotacao.services.impl;

import com.example.assembleiavotacao.components.Messages;
import com.example.assembleiavotacao.domains.Pauta;
import com.example.assembleiavotacao.exceptions.BusinessException;
import com.example.assembleiavotacao.repositories.PautaRepository;
import com.example.assembleiavotacao.services.PautaService;
import com.example.assembleiavotacao.services.PautaVotacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PautaServiceImpl implements PautaService {

    private final Messages messages;
    private final PautaRepository repository;
    private final PautaVotacaoService pautaVotacaoService;

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

    @Override
    public List<Pauta> getPautasParaApurar() {
        log.debug("into getPautasParaApurar method");
        return repository.findByDataAberturaIsNotNullAndDataApuracaoIsNullAndDataEncerramentoLessThan(LocalDateTime.now());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void apurarPautaByIdNewTransaction(Long id) {
        log.debug("into apurarPautaByIdNewTransaction method");

        Optional<Pauta> optionalPauta = this.findById(id);
        Pauta pauta = optionalPauta.orElseThrow(()-> new EmptyResultDataAccessException(1));

        if(pauta.getDataApuracao() != null){
            throw new BusinessException(messages.get("pauta.ja-foi-apurada"));
        }

        Long votosFavor = pautaVotacaoService.getNumeroVotos(pauta.getId(), Boolean.TRUE);
        Long votosContra = pautaVotacaoService.getNumeroVotos(pauta.getId(), Boolean.FALSE);
        saveApuracao(pauta.getId(), votosFavor, votosContra);

    }

    private void saveApuracao(Long codigoPauta, Long votosFavor, Long votosContra){
        log.debug("into saveApuracao method");
        repository.saveApuracao(codigoPauta, LocalDateTime.now(), votosFavor, votosContra);
    }
}
