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

import io.kotlintest.properties.Gen
import io.kotlintest.properties.generateInfiniteSequence

object ValidCpfGenerator : Gen<String> {

    override fun constants() = listOf(
        "10147788080",
        "98503877007",
        "57773940002",
        "27849112091",
        "00000000191"
    )

    // https://pt.wikipedia.org/wiki/D%C3%ADgito_verificador
    // https://www.geradorcpf.com/algoritmo_do_cpf.htm
    override fun random() = generateInfiniteSequence {
        val digits = List(9) { randomDigit() }
        val firstVerifierDigit = digits.firstVerifierDigit()
        val secondVerifierDigit = digits.secondVerifierDigit(firstVerifierDigit)

        digits.joinToString(separator = "") + "$firstVerifierDigit" + "$secondVerifierDigit"
    }

    private fun List<Int>.firstVerifierDigit(): Int {
        val weights = (10 downTo 2).toList()
        return calculateVerifierDigit(weights, this)
    }

    private fun List<Int>.secondVerifierDigit(firstVerifierDigit: Int): Int {
        val weights = (11 downTo 2).toList()
        return calculateVerifierDigit(weights, this + firstVerifierDigit)
    }

    private fun calculateVerifierDigit(weights: List<Int>, values: List<Int>): Int {
        var total = 0
        values.forEachIndexed { index, i ->
            total += i * weights[index]
        }

        val divisionRemainder = total % 11
        return if (divisionRemainder < 2) 0 else 11 - divisionRemainder
    }
}

private fun randomDigit() = Gen.from("0123456789".toList()).next().toString().toInt()
