# Simple Cpf Validator


[![Build Status](https://travis-ci.com/Kerooker/SimpleCpfValidator.svg?branch=master)](https://travis-ci.com/Kerooker/SimpleCpfValidator) [![GitHub](https://img.shields.io/github/license/Kerooker/SimpleCpfValidator.svg)](https://github.com/Kerooker/SimpleCpfValidator/blob/master/LICENSE) [![Maven Central](https://img.shields.io/maven-central/v/top.colman.simplecpfvalidator/simple-cpf-validator.svg)](https://search.maven.org/search?q=g:top.colman.simplecpfvalidator)


O conceito de validação de CPF existe desde a criação do próprio documento. No entanto, observa-se que a validação deste documento é replicada em várias aplicações, em classes idênticas, copiadas e coladas.

Com o objetivo de simplificar esse tipo de validação (seja em casos de teste ou em cenários de verificação de cadastro), a biblioteca **Simple CPF Validator** traz essa funcionalidade de uma vez, evitando assim boilerplate e possibilidade de erros no reuso de classe.


# Utilizando
Para utilizar é bem simples. Primeiro importe no seu Gradle:

`implementation("top.colman.simplecpfvalidator:simple-cpf-validator:{version}`

E utilize a função em qualquer String de seu código:

`"12345678911".isCpf()`

Por padrão, os caracteres `.` e `-` são retirados da String (permitindo o formato `123.456.789-11`, por exemplo), mas isso pode ser modificado através do parâmetro `charactersToIgnore`:

`"123.456.789/11".isCpf(charactersToIgnore = listOf('.', '/'))`

## CPFs inválidos

Por definição, os CPFs `111.111.111-11`, `222.222.222-22`, `...`, `999.999.999-99` são inválidos, e seu retorno será `falso`.

O CPF `000.000.001-91` em teoria representa apenas pessoas sem CPF, no entanto este validador considerará que o CPF é válido.

## Contribuindo

Sinta-se livre para abrir um pull request ou uma issue para contribuir com este projeto.
