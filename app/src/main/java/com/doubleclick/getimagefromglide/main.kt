package com.doubleclick.getimagefromglide

/**
 * Created By Eslam Ghazy on 1/6/2023
 */
fun main() {
//    val lambdaName:(InputType) -> ReturnType = {args:InputType -> body}

    FUN("Hi" ,::buz)

}

fun buz(input: String) {
    println("another message: $input")
}

fun FUN(msg: String, block: (input: String) -> Unit) {
    block(msg)
}