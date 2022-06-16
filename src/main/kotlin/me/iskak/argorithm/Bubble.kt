package me.iskak.argorithm

class Bubble(array: IntArray) : Algorithm(array) {
    private val indexes: IntArray = intArrayOf(0, 1)

    override fun next(): Map<String, Any> {
        val index0 = indexes[0]
        val index1 = indexes[1]

        if (array[index1] < array[index0]) {
            val tmp = array[index0]
            array[index0] = array[index1]
            array[index1] = tmp
            anyMoved = true
        }

        nextIndexes()

        return generateMap(intArrayOf(index0, index1))
    }

    override fun nextIndexes() {
        indexes[0]++
        indexes[1]++

        if (indexes[1] == array.size) {
            indexes[0] = 0
            indexes[1] = 1

            sorted = !anyMoved
            anyMoved = false
        }
    }
}