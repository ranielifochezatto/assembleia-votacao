# Assembléia de votação

### Objetivo

Esta api tem por objetivo criar novas pautas, iniciar a sessão de votação, registrar os votos e ao final contabilizar os votos recebidos. 

### Observações

- A api foi desenvolvida em Spring Boot;
- No arquivo de configurações, está definida para iniciar na porta 8081;
- As informações são persistidas em uma base de dados H2, em ~/data/db_assembleia, que será criada assim que a aplicação iniciar;
- Para o desenvolvimento da aplicação, foi utilizado o Lombok. Para rodar a aplicação local, faça a instalação do plugin do lombok na sua IDE (https://projectlombok.org/);

### Funcionalidades

---

#### 1. Cadastrar uma nova pauta:

`POST v1/pauta` 

>Exemplo: http://localhost:8081/v1/pauta

payload (json):
```json
{
  "nomePauta": "PAUTA 01"
}
```
response:
```json
{
  "id": 1,
  "nomePauta": "PAUTA 01",
  "dataCadastro": "2021-05-31T03:10:31.395263",
  "dataAbertura": null,
  "dataEncerramento": null,
  "dataApuracao": null,
  "numeroVotosFavor": null,
  "numeroVotosContra": null
}
```

---

#### 2. Abrir uma sessão de votação:

`PUT v1/pauta/{id da pauta}/abrir-sessao?tempoDuracaoEmMinutos={tempo}`

>Exemplo: http://localhost:8081/v1/pauta/1/abrir-sessao?tempoDuracaoEmMinutos=1

O tempo de duração da sessão é opcional. Caso não seja informado, o tempo padrão é de 1 minuto

response:
```json
{
  "id": 1,
  "nomePauta": "PAUTA 01",
  "dataCadastro": "2021-05-31T03:10:31.395263",
  "dataAbertura": "2021-05-31T03:10:39.28598",
  "dataEncerramento": "2021-05-31T03:11:39.28598",
  "dataApuracao": null,
  "numeroVotosFavor": null,
  "numeroVotosContra": null
}
```

---

#### 3. Votar

`POST /v1/pauta/{id da pauta}/votacao`

>Exemplo: http://localhost:8081/v1/pauta/1/votacao

payload (json):
```json
{
    "numeroCpfAssociado": "47049947083",
    "voto": "SIM" // SIM ou NAO
}
```

- Cada associado (CPF) somente poderá votar uma unica vez em cada Pauta;
- O CPF será validado pelo serviço externo  https://user-info.herokuapp.com/users/{cpf};
- Os votos somente serão permitidos se a sessão da pauta estiver aberta e o tempo de duração não tenha se esgotado;

response:
`````json
{
    "id": 1,
    "dataCadastro": "2021-05-31T03:10:45.501856",
    "numeroCpfAssociado": "47049947083",
    "voto": "SIM",
    "pauta": {
      "id": 1,
      "nomePauta": "PAUTA 01",
      "dataCadastro": "2021-05-31T03:10:31.395263",
      "dataAbertura": "2021-05-31T03:10:39.28598",
      "dataEncerramento": "2021-05-31T03:11:39.28598",
      "dataApuracao": null,
      "numeroVotosFavor": null,
      "numeroVotosContra": null
    }
}
`````

---

#### 4. Contabilização dos votos

Os votos serão automaticamente computados em até 30 segundos após o encerramento da sessão. Foi desenvido um job que roda a cada 30 segundos verificando as pautas com sessões encerradas que precisam ser apuradas.

Para consultar a contabilizacao dos votos, deve-se consultar a pauta:

`GET /v1/pauta/{id da pauta}`

>Exemplo: http://localhost:8081/v1/pauta/1

response:

```json
{
    "id": 1,
    "nomePauta": "PAUTA 01",
    "dataCadastro": "2021-05-31T03:10:31.395263",
    "dataAbertura": "2021-05-31T03:10:39.28598",
    "dataEncerramento": "2021-05-31T03:11:39.28598",
    "dataApuracao": "2021-05-31T03:12:08.877115",
    "numeroVotosFavor": 4,
    "numeroVotosContra": 3
}
```

### Informações adicionais

- Foi adicionado o Swagger para a documentação da api, que pode ser acessado em http://localhost:8081/swagger-ui/;
