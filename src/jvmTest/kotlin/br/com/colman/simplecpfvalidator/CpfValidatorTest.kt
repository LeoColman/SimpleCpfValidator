/*
 * Copyright 2024 Leonardo Colman Lopes
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
package br.com.colman.simplecpfvalidator

import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.property.Arb
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll


class CpfValidatorTest : FunSpec({
  test("Should return false on invalid CPFs") {
    invalidCpfs.forAll { it.shouldNotBeCpf() }
  }

  test("Should return true on valid CPFs") {
    ValidCpfGenerator.checkAll { it.shouldBeCpf() }
  }

  test("Should return false on random strings") {
    Arb.string().checkAll { it.shouldNotBeCpf() }
  }

  test("Should return false on known invalid CPFs") {
    knownInvalidCpfs.forAll { it.shouldNotBeCpf() }
  }

  test("Should sanitize the String given replaceable characters and still return true on valid CPFs") {
    ValidCpfGenerator.map { "..--.$it..--." }.checkAll { it.shouldBeCpf() }
  }

  test("Shouldn't sanitize unspecified characters") {
    ValidCpfGenerator.map { "$it++" }.checkAll { it.shouldNotBeCpf() }
  }

  test("Should return true on valid Long typed CPF input") {
    24865482385.shouldBeCpf()
  }

  test("Should return false on invalid Long typed CPF input") {
    11111111111.shouldNotBeCpf()
  }

  test("Should return false on invalid length of Long typed CPF input") {
    999L.shouldNotBeCpf()
  }
})

private fun String.shouldBeCpf() { this.isCpf().shouldBeTrue() }
private fun String.shouldNotBeCpf() { this.isCpf().shouldBeFalse() }
private fun Long.shouldBeCpf() { this.isCpf().shouldBeTrue() }
private fun Long.shouldNotBeCpf() { this.isCpf().shouldBeFalse() }


private val invalidCpfs = listOf(
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
