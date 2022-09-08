package com.reddevx.quizin.data.models

data class Inventory(
    val allItems: List<ShopItem>,
    val inUseItems: Array<ShopItem>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Inventory

        if (allItems != other.allItems) return false
        if (!inUseItems.contentEquals(other.inUseItems)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = allItems.hashCode()
        result = 31 * result + inUseItems.contentHashCode()
        return result
    }
}
