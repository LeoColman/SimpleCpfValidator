package top.colman.simplecpfvalidator

/**
 * Verifies that this String is a CPF
 *
 * This function checks if a given string is a CPF (Cadastro de Pessoa Fisica in Portuguese), which is the Brazilian
 * individual taxpayer registry identification.
 *
 * All strings will first be sanitized, with [charactersToIgnore] chars removed from it (it's usual for the document
 * to come in the form of xxx.xxx.xxx-yy, which isn't a valid cpf number), and then they'll be validated according to
 * the CPF specification.
 *
 * The CPFs 111.111.111-11, 222.222.222-22, ..., 999.999.999-99 although numeric valid are considered invalid CPFs as
 * per the specification.
 *
 * *ATTENTION*: Although the CPF 000.000.001-91 is supposed to be used only for representing people without a CPF
 * document, it will be considered valid.
 *
 * @see [https://pt.wikipedia.org/wiki/Cadastro_de_pessoas_f%C3%ADsicas]
 * @see [http://normas.receita.fazenda.gov.br/sijut2consulta/link.action?visao=anotado&idAto=1893]
 */
fun String.isCpf(charactersToIgnore: List<Char> = listOf('.', '-')): Boolean {
    val sanitizedCpf = this.filterNot { it in charactersToIgnore }
    if (sanitizedCpf.containsInvalidCPFChars() || sanitizedCpf.isInvalidCpfSize() || sanitizedCpf.isBlacklistedCpf()) return false
    return this.hasValidVerificationDigits()
}

private fun String.containsInvalidCPFChars() = this.any { !it.isDigit() }
private fun String.isInvalidCpfSize() = this.length != 11
private fun String.isBlacklistedCpf() = this in blacklistedCpfs

// Algorithm from https://www.somatematica.com.br/faq/cpf.php
private fun String.hasValidVerificationDigits(): Boolean {
    val firstNineDigits = substring(0..8)
    val digits = substring(9..10)
    
    return firstNineDigits.calculateDigits() == digits
}

private fun String.calculateDigits(): String {
    val numbers = map { it.toString().toInt() }
    val firstDigit = numbers.calculateFirstVerificationDigit()
    val secondDigit = numbers.calculateSecondVerificationDigit(firstDigit)
    
    return "$firstDigit$secondDigit"
}

private fun List<Int>.calculateFirstVerificationDigit(): Int {
    /* Given 9 CPF numbers, the first digit calculation works this way:
    
    There is a weight associated to each number index:
    CPF first nine digits - | A  | B | C | D | E | F | G | H | I |
    CPF index multiplier  - | 10 | 9 | 8 | 7 | 6 | 5 | 4 | 3 | 2 |
  
    We will then sum all the digits with their multiplier: A * 10 + B * 9 + C * 8 ...
    With that result, the first verifier digit will be calculated with the remainder of (SUM / 11)
    If the remainder is 0 or 1, the digit is ZERO. If it's >=2, the digit is (11 - remainder)
     */
    val firstNineDigits = this
    val weights = (10 downTo 2).toList()
    val sum = firstNineDigits.withIndex().sumBy { (index, element) -> weights[index] * element }
    
    val remainder = sum % 11
    return if(remainder < 2) 0 else 11 - remainder
}

private fun List<Int>.calculateSecondVerificationDigit(firstDigit: Int): Int {
    /*
    In a similar way to calculating the first digit, the second digit also works with a table of weights and the numbers
    
    However, the last digit is added to the digits
    
    CPF first nine digits + first verification digit - | A  | B  | C | D | E | F | G | H | I | 1st v.d. |
    CPF Index multiplier                             - | 11 | 10 | 9 | 8 | 7 | 6 | 5 | 4 | 3 | 2        |
    
    And in the same fashion, the sum will be calculated as A * 11 + B * 10 + ... + 1st vd * 2
    The second verification digit uses the same formula:
    remainder = (SUM / 11)
    2nd digit = ZERO if remainder is 0 or 1, 11 - remainder otherwise
     */
    
    val firstTenDigits = this + firstDigit
    val weights = (11 downTo 2).toList()
    val sum = firstTenDigits.withIndex().sumBy { (index, element) -> weights[index] * element }
    
    val remainder = sum % 11
    return if (remainder < 2) 0 else 11 - remainder
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