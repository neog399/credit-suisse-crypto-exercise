package org.credit.suisse.crypto.excercise

class CryptoOrderManager {
    val orders: MutableList<CryptoOrder> = mutableListOf()

    // Add validation? is coin type supported, is the quantity and price between accepted thresholds, etc.
    fun placeOrder(order: CryptoOrder) {
        orders.add(order)
    }
}
