package org.credit.suisse.crypto.excercise

import org.javamoney.moneta.Money
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class CryptoOrderManagerTest {
    private val cryptoOrderManager = CryptoOrderManager()

    @Test
    fun placesNewOrder() {
        val order = CryptoOrder(
            CryptoOrderType.SELL,
            "user",
            "Ethereum",
            BigDecimal("10.25"),
            Money.of(BigDecimal("123.68"), "GBP")
        )

        cryptoOrderManager.placeOrder(order)

        assertTrue(cryptoOrderManager.orders.contains(order))
    }
}