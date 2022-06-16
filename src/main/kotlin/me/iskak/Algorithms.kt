package me.iskak

enum class Algorithms {
    BUBBLE_SORT;

    override fun toString(): String {
        return super.toString()
            .replace('_', ' ')
            .lowercase()
            .replaceFirstChar { char -> char.uppercase() }
    }

    companion object {
        fun find(value: String): Algorithms {
            return Algorithms.valueOf(
                value.replace(' ', '_')
                    .uppercase()
            )
        }
    }
}