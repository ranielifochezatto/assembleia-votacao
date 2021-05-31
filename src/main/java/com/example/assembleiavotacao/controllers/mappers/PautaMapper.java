package com.example.assembleiavotacao.controllers.mappers;

import com.example.assembleiavotacao.controllers.dtos.PautaDTO;
import com.example.assembleiavotacao.domains.Pauta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = BeanMapper.SPRING)
public interface PautaMapper extends BeanMapper<Pauta, PautaDTO> {

    @Override
    PautaDTO toDto(Pauta pauta);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCadastro", ignore = true)
    @Mapping(target = "dataAbertura", ignore = true)
    @Mapping(target = "dataEncerramento", ignore = true)
    @Mapping(target = "dataApuracao", ignore = true)
    @Mapping(target = "numeroVotosFavor", ignore = true)
    @Mapping(target = "numeroVotosContra", ignore = true)
    Pauta toEntity(PautaDTO pautaDTO);
}
