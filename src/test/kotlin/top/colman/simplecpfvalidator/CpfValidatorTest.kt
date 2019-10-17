/*
 * Copyright 2019 Leonardo Colman Lopes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.colman.simplecpfvalidator

import io.kotlintest.inspectors.forNone
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.properties.Gen
import io.kotlintest.properties.forAll
import io.kotlintest.properties.forNone
import io.kotlintest.specs.FunSpec

class CpfValidatorTest : FunSpec() {

    init {
        test("Should return false on blacklisted CPFs") {
            blacklistedCpfs.forNone { it.shouldBeCpf() }
        }

        test("Should return true on valid CPFs") {
            ValidCpfGenerator.forAll { cpf -> cpf.isCpf() }
        }

        test("Should return false on random strings") {
            Gen.string().forNone { cpf -> cpf.isCpf() }
        }

        test("Should return false on invalid but non-blacklisted CPF") {
            knownInvalidCpfs.forNone { it.shouldBeCpf() }
        }

        test("Should sanitize the String given replaceable characters and still return true on valid cpfs") {
            ValidCpfGenerator.map { "$it..--." }.forAll { cpf -> cpf.isCpf(listOf('.', '-')) }
        }

        test("Shouldn't sanitize unspecified characters") {
            ValidCpfGenerator.map { "$it++" }.forNone { cpf -> cpf.isCpf(listOf('.', '-')) }
        }
    }

    private fun String.shouldBeCpf() = this.isCpf().shouldBeTrue()
}

private val blacklistedCpfs = listOf(
    "00000000000",
    "11111111111",
    "22222222222",
    "33333333333",
    "44444444444",
    "55555555555",
    "66666666666",
    "77777777777",
    "88888888888",
    "99999999999"
)

// Generated some valid CPFs and changed the verification digits
private val knownInvalidCpfs = listOf(
    "01202301204",
    "73681243191",
    "25407714320",
    "11438273844",
    "18706863061",
    "67358678312",
    "57506620392",
    "46637587037",
    "72366272830",
    "50763321061",
    "04716481248",
    "27736741469",
    "48632488652",
    "22700081707",
    "17621402840",
    "64010063239"
)
