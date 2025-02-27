# Simple Cpf Validator

[![Build](https://github.com/LeoColman/SimpleCpfValidator/workflows/Build/badge.svg)](https://github.com/LeoColman/SimpleCpfValidator/actions/workflows/build.yaml)
[![GitHub](https://img.shields.io/github/license/LeoColman/SimpleCpfValidator.svg)](https://github.com/LeoColman/SimpleCpfValidator/blob/master/LICENSE) 
[![Maven Central](https://img.shields.io/maven-central/v/br.com.colman.simplecpfvalidator/simple-cpf-validator.svg)](https://search.maven.org/search?q=g:br.com.colman.simplecpfvalidator)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin/tree/readme#validation-back-)
[![Zero Dependencies Badge](https://img.shields.io/badge/Dependencies-0-brightgreen)](build.gradle.kts)
![Maintenance](https://img.shields.io/maintenance/yes/2025)



A validação de CPF sempre existiu, mas ainda é feita de forma repetitiva em várias aplicações. O mesmo código acaba sendo copiado e colado em diferentes lugares.

O Simple CPF Validator resolve esse problema ao oferecer uma validação pronta para uso, tanto em testes quanto no cadastro de usuários. Isso evita código duplicado e reduz erros no reuso.

# Utilizando
Usar o Simple CPF Validator é simples. Primeiro, adicione a dependência ao seu projeto no Gradle:

`implementation("br.com.colman.simplecpfvalidator:simple-cpf-validator:{version}")`

Depois, basta chamar a função em qualquer `String`:

`"12345678911".isCpf()`

Por padrão, os caracteres `.` e `-` são ignorados, permitindo o uso de formatos como `123.456.789-11`. Se precisar modificar quais caracteres devem ser removidos, use o parâmetro `charactersToIgnore`:

`"123.456.789/11".isCpf(charactersToIgnore = listOf('.', '/'))`

## CPFs inválidos

Os CPFs com todos os dígitos iguais (`111.111.111-11`, `222.222.222-22`, ..., `999.999.999-99`) são considerados inválidos e retornarão `false`.

Já o CPF `000.000.001-91`, que teoricamente representa pessoas sem CPF, será tratado como válido por este validador.

## Contribuindo

Contribuições são bem-vindas! Se tiver sugestões, abra uma _issue_ ou envie um _pull request_.
