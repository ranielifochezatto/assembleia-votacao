package com.example.assembleiavotacao.services;

import com.example.assembleiavotacao.domains.Pauta;

import java.util.Optional;

public interface PautaService {

    Optional<Pauta> findById(Long id);

    Pauta save(Pauta pauta);

    Pauta abrirSessao(Long codigoPauta, Long tempoDuracaoEmMinutos);
}
