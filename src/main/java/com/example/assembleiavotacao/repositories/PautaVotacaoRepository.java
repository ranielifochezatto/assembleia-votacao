package com.example.assembleiavotacao.repositories;

import com.example.assembleiavotacao.domains.PautaVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaVotacaoRepository extends JpaRepository<PautaVotacao, Long> {

    Boolean existsByNumeroCpfAssociadoAndPautaId(String numeroCpfAssociado, Long codigoPauta);

    Long countByPautaIdAndVotoTrue(Long codigoPauta);

    Long countByPautaIdAndVotoFalse(Long codigoPauta);
}
