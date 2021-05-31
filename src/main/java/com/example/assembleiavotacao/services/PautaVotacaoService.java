package com.example.assembleiavotacao.services;

import com.example.assembleiavotacao.domains.PautaVotacao;

import java.util.Optional;

public interface PautaVotacaoService {

    Optional<PautaVotacao> findById(Long id);

    PautaVotacao save(PautaVotacao pautaVotacao);

}
