package com.example.assembleiavotacao.controllers.mappers;

import com.example.assembleiavotacao.controllers.dtos.PautaVotacaoDTO;
import com.example.assembleiavotacao.domains.PautaVotacao;
import com.example.assembleiavotacao.enumerations.EnumVoto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = BeanMapper.SPRING, uses = {
        PautaMapper.class,
        EnumVoto.class
})
public interface PautaVotacaoMapper extends BeanMapper<PautaVotacao, PautaVotacaoDTO> {

    @Override
    @Mapping(target = "voto", ignore = true)
    PautaVotacaoDTO toDto(PautaVotacao pautaVotacao);

    @AfterMapping
    default void toDtoAfter(PautaVotacao source, @MappingTarget PautaVotacaoDTO target){
        if(source.getVoto() != null){
            target.setVoto(Boolean.TRUE.equals(source.getVoto()) ? EnumVoto.SIM : EnumVoto.NAO);
        }
    }

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "pauta", ignore = true)
    @Mapping(target = "voto", ignore = true)
    PautaVotacao toEntity(PautaVotacaoDTO pautaVotacaoDTO);

    @AfterMapping
    default void toEntity(PautaVotacaoDTO source, @MappingTarget PautaVotacao target){
        if(source.getVoto() != null){
            target.setVoto(EnumVoto.SIM.equals(source.getVoto()));
        }
    }

}
