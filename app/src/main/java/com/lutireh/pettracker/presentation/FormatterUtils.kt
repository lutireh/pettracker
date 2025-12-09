package com.lutireh.pettracker.presentation

fun isNumberInputNotValid(input: String?): Boolean {
    return try {
        input?.toDouble()
        false
    } catch (e: NumberFormatException) {
        true
    } catch (e: NullPointerException) {
        true
    }
}
