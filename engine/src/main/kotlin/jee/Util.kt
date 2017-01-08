package jee

import java.util.*

fun <T> permute(options: List<Collection<T>>): List<List<T>> {
    return permuteMap(options, { ArrayList(it) })
}

fun <T, S> permuteMap(options: List<Collection<T>>, func: (List<T>) -> S): List<S> {
    val result: MutableList<S> = mutableListOf()
    permuteImpl(options, func, mutableListOf(), result)
    return result
}

fun <T, S> permuteImpl(options: List<Collection<T>>, func: (List<T>) -> S, current: MutableList<T>, result: MutableCollection<S>) {
    if (options.isEmpty()) {
        result.add(func(current))
    } else {
        val option = options.first()
        for (opt in option) {
            current.add(opt)
            permuteImpl(options.subList(1, options.size), func, current, result)
            current.removeAt(current.size - 1)
        }
    }
}

fun <T : Comparable<T>> isListEqual(a: List<T>, b: List<T>): Boolean {
    if (a.size != b.size) {
        return false;
    }
    val sortedA = a.sorted()
    val sortedB = b.sorted()
    for (pos in sortedA.indices) {
        if (sortedA[pos] != sortedB[pos]) {
            return false;
        }
    }
    return true;
}

fun <T : Comparable<T>> isSubList(superlist: List<T>, sublist: List<T>): Boolean {
    if (superlist.size < sublist.size) {
        return false;
    }
    val sortedSuper = superlist.sorted()
    val sortedSub = sublist.sorted()
    var subIdx = 0
    var superIdx = 0
    while (subIdx < sortedSub.size && superIdx < sortedSuper.size) {
        val superVal = sortedSuper[superIdx]
        val subVal = sortedSub[subIdx]
        if (superVal == subVal) {
            subIdx++
            superIdx++
        } else if (superVal < subVal) {
            superIdx++
        } else {
            return false
        }
    }
    return subIdx == sortedSub.size;
}