package com.example.assembleiavotacao.domains;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(schema = "PUBLIC", name = "PAUTA")
public class Pauta implements Serializable {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CD_PAUTA", nullable = false, unique = true, length = 9, updatable = false)
    private Long id;

    @Column(name = "NM_PAUTA", nullable = false, length = 100)
    private String nomePauta;

    @CreationTimestamp
    @Column(name = "DT_CADASTRO", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "DT_ABERTURA")
    private LocalDateTime dataAbertura;

    @Column(name = "DT_ENCERRAMENTO")
    private LocalDateTime dataEncerramento;

    @Column(name = "DT_APURACAO", updatable = false, insertable = false)
    private LocalDateTime dataApuracao;

    @Column(name = "NR_VOTOS_FAVOR", length = 9, updatable = false, insertable = false)
    private Long numeroVotosFavor;

    @Column(name = "NR_VOTOS_CONTRA", length = 9, updatable = false, insertable = false)
    private Long numeroVotosContra;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_PAUTA", referencedColumnName = "CD_PAUTA", insertable = false, updatable = false)
    private PautaVotacao votacao;


}
