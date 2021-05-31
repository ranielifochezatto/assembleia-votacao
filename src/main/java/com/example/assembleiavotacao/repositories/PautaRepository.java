package com.example.assembleiavotacao.repositories;

import com.example.assembleiavotacao.domains.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {

}
