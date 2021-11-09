package dev.llun.practice

import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

fun main(args: Array<String>) {
    var beers : List<String> = listOf("Heineken", "Guinness", "Tiger", "Sapporo", "Asahi", "Kronenbourg", "Carlsberg")
    var flux:Flux<String> = Flux.fromIterable(beers)

    flux
        .buffer(3)
        .flatMap { beers ->
            beers.slice(0..if (beers.size > 2) 1 else 0).toMono()
        }
        .reduce{ acc, s -> s?.let { acc?.plus(it) } }
        .map { println(it) }
        .subscribe()
}