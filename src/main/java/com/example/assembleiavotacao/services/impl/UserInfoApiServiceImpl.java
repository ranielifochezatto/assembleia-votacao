package com.example.assembleiavotacao.services.impl;

import com.example.assembleiavotacao.clients.userinfo.UserInfoApiClient;
import com.example.assembleiavotacao.clients.userinfo.dtos.StatusVote;
import com.example.assembleiavotacao.clients.userinfo.dtos.UserCpfResponseDTO;
import com.example.assembleiavotacao.components.Messages;
import com.example.assembleiavotacao.exceptions.BusinessException;
import com.example.assembleiavotacao.services.UserInfoApiService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoApiServiceImpl implements UserInfoApiService {

    private final Messages messages;
    private final UserInfoApiClient userInfoApiClient;

    @Override
    public StatusVote verifyCpf(String numeroCpf) {
        log.debug("into verifyCpf method");
        StatusVote statusVote = null;
        try {
            UserCpfResponseDTO response = userInfoApiClient.verifyCpf(numeroCpf);
            statusVote = response.getStatus();
        } catch (FeignException e) {
            if (e.status() != HttpStatus.NOT_FOUND.value())
                throw new BusinessException((messages.get("userinfo-api.erro-ao-consultar-cpf-tente-mais-tarde", e.getMessage())));
        }

        return statusVote;
    }
}
