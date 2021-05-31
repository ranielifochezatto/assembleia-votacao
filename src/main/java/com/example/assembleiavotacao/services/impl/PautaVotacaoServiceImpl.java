package com.example.assembleiavotacao.services.impl;

import com.example.assembleiavotacao.components.Messages;
import com.example.assembleiavotacao.domains.PautaVotacao;
import com.example.assembleiavotacao.exceptions.BusinessException;
import com.example.assembleiavotacao.repositories.PautaVotacaoRepository;
import com.example.assembleiavotacao.services.PautaVotacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PautaVotacaoServiceImpl implements PautaVotacaoService {

    private final Messages messages;
    private final PautaVotacaoRepository repository;

    @Override
    public Optional<PautaVotacao> findById(Long id) {
        log.debug("into findById method");
        return repository.findById(id);
    }

    @Override
    public PautaVotacao save(PautaVotacao pautaVotacao) {
        log.debug("into save method");

        //verificar se a sessao da pauta ja foi iniciada
        if(pautaVotacao.getPauta().getDataAbertura() == null){
            throw new BusinessException(messages.get("pauta-votacao.sessao-desta-pauta-ainda-nao-foi-aberta"));
        }

        //verificar se a sessao ainda esta ativa
        if(pautaVotacao.getPauta().getDataEncerramento().compareTo(LocalDateTime.now()) < 0){
            throw new BusinessException(messages.get("pauta-votacao.sessao-desta-pauta-ja-foi-encerrada"));
        }

        //verificar se o associado ja votou nessa pauta
        if(Boolean.TRUE.equals(associadoVotouPauta(pautaVotacao.getNumeroCpfAssociado(), pautaVotacao.getPauta().getId()))){
            throw new BusinessException(messages.get("pauta-votacao.associado-ja-votou-nessa-pauta"));
        }

        return repository.save(pautaVotacao);
    }

    private Boolean associadoVotouPauta(String numeroCpfAssociado, Long codigoPauta){
        log.debug("into associadoVotouPauta method");
        return repository.existsByNumeroCpfAssociadoAndPautaId(numeroCpfAssociado, codigoPauta);
    }

}
