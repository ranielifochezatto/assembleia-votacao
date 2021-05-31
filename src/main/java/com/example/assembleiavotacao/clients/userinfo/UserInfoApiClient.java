package com.example.assembleiavotacao.clients.userinfo;

import com.example.assembleiavotacao.clients.userinfo.dtos.UserCpfResponseDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface UserInfoApiClient {

    @Headers("Content-Type: application/json")
    @RequestLine("GET /users/{cpf}")
    UserCpfResponseDTO verifyCpf(@Param("cpf") String numeroCpf);

}
