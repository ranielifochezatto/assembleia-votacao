package com.example.assembleiavotacao.services.impl;

import com.example.assembleiavotacao.clients.userinfo.dtos.StatusVote;
import com.example.assembleiavotacao.components.Messages;
import com.example.assembleiavotacao.domains.PautaVotacao;
import com.example.assembleiavotacao.exceptions.BusinessException;
import com.example.assembleiavotacao.repositories.PautaVotacaoRepository;
import com.example.assembleiavotacao.services.PautaVotacaoService;
import com.example.assembleiavotacao.services.UserInfoApiService;
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
    private final UserInfoApiService userInfoApiService;

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

        //verificar se o associado esta habilitado para a votacao
        this.verificarAssociadoPodeVotar(pautaVotacao.getNumeroCpfAssociado());

        return repository.save(pautaVotacao);
    }

    private void verificarAssociadoPodeVotar(String numeroCpf){
        log.debug("into verificarAssociadoPodeVotar method");
        StatusVote statusVote = userInfoApiService.verifyCpf(numeroCpf);

        if(statusVote == null){//cpf invalido
            throw new BusinessException(messages.get("pauta-votacao.cpf-invalido"));
        }
        if(!StatusVote.ABLE_TO_VOTE.equals(statusVote)){
            throw new BusinessException(messages.get("pauta-votacao.cpf-nao-habilitado-para-votar"));
        }
    }

    @Override
    public Long getNumeroVotos(Long codigoPauta, Boolean voto) {
        log.debug("into getNumeroVotos method");
        if(Boolean.TRUE.equals(voto)){
            return repository.countByPautaIdAndVotoTrue(codigoPauta);
        } else{
            return repository.countByPautaIdAndVotoFalse(codigoPauta);
        }

    }

    private Boolean associadoVotouPauta(String numeroCpfAssociado, Long codigoPauta){
        log.debug("into associadoVotouPauta method");
        return repository.existsByNumeroCpfAssociadoAndPautaId(numeroCpfAssociado, codigoPauta);
    }

}
