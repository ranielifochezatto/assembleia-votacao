package com.example.assembleiavotacao.domains;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(schema = "PUBLIC", name = "PAUTA_VOTACAO")
public class PautaVotacao {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "CD_PAUTA_VOTACAO", nullable = false, unique = true, length = 9, updatable = false)
    private Long id;

    @CreationTimestamp
    @Column(name = "DT_CADASTRO", nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "NR_CPF_ASSOCIADO", nullable = false, length = 11, updatable = false)
    private String numeroCpfAssociado;

    @Column(name = "BL_VOTO", nullable = false)
    private Boolean voto;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CD_PAUTA", referencedColumnName = "CD_PAUTA", nullable = false, updatable = false)
    private Pauta pauta;

}
