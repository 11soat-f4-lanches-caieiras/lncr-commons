#language: pt

Funcionalidade: IntegrationUtil - Utilitário de integração
  Como desenvolvedor
  Quero utilizar métodos utilitários para integração
  Para facilitar chamadas HTTP e conversão de objetos

  Cenário: Converter objeto para JSON com sucesso
    Dado um objeto válido para conversão
    Quando converto o objeto para JSON
    Então o JSON deve ser retornado corretamente

  Cenário: Definir headers HTTP padrão
    Quando defino headers HTTP padrão
    Então o header Content-Type deve ser application/json

  Cenário: Realizar requisição POST sem retorno
    Dado uma URL válida para requisição POST
    E um objeto DTO válido
    Quando realizo uma requisição POST sem retorno
    Então a requisição deve ser executada de forma assíncrona

  Cenário: Realizar requisição POST com retorno
    Dado uma URL válida para requisição POST
    E um objeto DTO válido
    Quando realizo uma requisição POST com retorno
    Então devo receber uma resposta em String

  Cenário: Realizar requisição GET retornando objeto
    Dado uma URL válida para requisição GET
    Quando realizo uma requisição GET
    Então devo receber um objeto do tipo esperado

  Cenário: Tratar erro ao converter objeto para JSON
    Dado um objeto inválido para conversão
    Quando tento converter o objeto para JSON
    Então deve lançar IntegrationException

  Cenário: Realizar requisição PATCH
    Dado uma URL válida para requisição PATCH
    E um objeto DTO válido
    Quando realizo uma requisição PATCH
    Então devo receber uma resposta do servidor

  Cenário: Extrair conteúdo de integração
    Dado um corpo de resposta com campo content
    Quando extraio o conteúdo da integração
    Então devo receber o objeto mapeado corretamente

