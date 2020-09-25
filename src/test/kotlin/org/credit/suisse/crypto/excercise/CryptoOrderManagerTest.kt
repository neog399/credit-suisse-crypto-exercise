package org.credit.suisse.crypto.excercise

import org.javamoney.moneta.Money
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class CryptoOrderManagerTest {
    private val cryptoOrderManager = CryptoOrderManager()

    private val order = CryptoOrder(
        CryptoOrderType.SELL,
        "user",
        "Ethereum",
        BigDecimal("10.25"),
        Money.of(BigDecimal("123.68"), "GBP")
    )

    @Test
    fun placesNewOrder() {
        val orderId = cryptoOrderManager.placeOrder(order)

        val savedOrder = cryptoOrderManager.orders[orderId]!!
        assertEquals(order.type, savedOrder.type)
        assertEquals(order.userId, savedOrder.userId)
        assertEquals(order.coinType, savedOrder.coinType)
        assertEquals(order.quantity, savedOrder.quantity)
        assertEquals(order.pricePerCoin, savedOrder.pricePerCoin)
    }

    @Test
    fun cancelsExistingOrder() {
        val orderId = cryptoOrderManager.placeOrder(order)

        cryptoOrderManager.cancelOrder(orderId)

        assertFalse(cryptoOrderManager.orders.containsKey(orderId))
    }
}