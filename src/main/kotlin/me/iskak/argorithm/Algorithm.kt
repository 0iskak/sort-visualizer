package me.iskak.argorithm

abstract class Algorithm(
    protected val array: IntArray
) {
    protected var anyMoved = false
    var sorted = false

    abstract fun next(): Map<String, Any>
    abstract fun nextIndexes()

    protected fun generateMap(indexes: IntArray): Map<String, Any> {
        return mapOf(
            "indexes" to indexes,
            "array" to array
        )
    }
}