package org.credit.suisse.crypto.excercise

import java.util.*

class CryptoOrderManager {
    val orders: MutableMap<UUID, CryptoOrder> = mutableMapOf()

    // Add validation? is coin type supported, is the quantity and price between accepted thresholds, etc.
    fun placeOrder(order: CryptoOrder): UUID {
        val identifiableOrder = IdentifiableCryptoOrder(order)
        orders[identifiableOrder.id] = identifiableOrder

        return identifiableOrder.id
    }

    // Could return a boolean / the deleted order here to indicate if anything was deleted
    fun cancelOrder(orderId: UUID) {
        orders.remove(orderId)
    }
}
