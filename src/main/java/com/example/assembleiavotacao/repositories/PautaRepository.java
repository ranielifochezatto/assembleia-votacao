package com.example.assembleiavotacao.repositories;

import com.example.assembleiavotacao.domains.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

    List<Pauta> findByDataAberturaIsNotNullAndDataApuracaoIsNullAndDataEncerramentoLessThan(LocalDateTime now);


    @Modifying
    @Query("update Pauta P " +
            "set P.dataApuracao = :dataApuracao" +
            ", P.numeroVotosFavor = :numeroVotosFavor " +
            ", P.numeroVotosContra = :numeroVotosContra " +
            "where P.id = :codigoPauta")
    void saveApuracao(@Param("codigoPauta") Long codigoPauta,
                      @Param("dataApuracao") LocalDateTime dataApuracao,
                      @Param("numeroVotosFavor") Long numeroVotosFavor,
                      @Param("numeroVotosContra") Long numeroVotosContra);

}
