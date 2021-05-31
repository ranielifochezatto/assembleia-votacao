package com.example.assembleiavotacao.services;

import com.example.assembleiavotacao.clients.userinfo.dtos.StatusVote;

public interface UserInfoApiService {

    StatusVote verifyCpf(String numeroCpf);

}
