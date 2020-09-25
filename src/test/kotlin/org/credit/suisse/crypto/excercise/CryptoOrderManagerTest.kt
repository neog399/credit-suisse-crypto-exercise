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
        cryptoOrderManager.placeOrder(order)

        assertEquals(
            listOf("10.25 for £123.68"),
            cryptoOrderManager.liveOrderSummaries(CryptoOrderType.SELL, "Ethereum")
        )
    }

    @Test
    fun cancelsExistingOrder() {
        val orderId = cryptoOrderManager.placeOrder(order)

        cryptoOrderManager.cancelOrder(orderId)

        assertEquals(
            emptyList<String>(),
            cryptoOrderManager.liveOrderSummaries(CryptoOrderType.SELL, "Ethereum")
        )
    }

    @Test
    fun returnsSummaryOfLiveEthereumSellOrders() {
        // The test data provided in the exercise was missing the coin type for most inputs
        // so I assumed 'Ethereum' for each input
        val ethereumSellOrder1 = CryptoOrder(
            CryptoOrderType.SELL,
            "user1",
            "Ethereum",
            BigDecimal("350.1"),
            Money.of(BigDecimal("13.6"), "GBP")
        )
        val ethereumSellOrder2 = CryptoOrder(
            CryptoOrderType.SELL,
            "user2",
            "Ethereum",
            BigDecimal("50.5"),
            Money.of(BigDecimal("14"), "GBP")
        )
        val ethereumSellOrder3 = CryptoOrder(
            CryptoOrderType.SELL,
            "user3",
            "Ethereum",
            BigDecimal("441.8"),
            Money.of(BigDecimal("13.9"), "GBP")
        )
        val ethereumSellOrder4 = CryptoOrder(
            CryptoOrderType.SELL,
            "user4",
            "Ethereum",
            BigDecimal("3.5"),
            Money.of(BigDecimal("13.6"), "GBP")
        )
        val buyOrder = CryptoOrder(
            CryptoOrderType.BUY,
            "user2",
            "Ethereum",
            BigDecimal("10.25"),
            Money.of(BigDecimal("13.9"), "GBP")
        )
        val otherCoinOrder = CryptoOrder(
            CryptoOrderType.SELL,
            "user1",
            "Litecoin",
            BigDecimal("2.89"),
            Money.of(BigDecimal("12.5"), "GBP")
        )

        cryptoOrderManager.placeOrder(ethereumSellOrder1)
        cryptoOrderManager.placeOrder(ethereumSellOrder2)
        cryptoOrderManager.placeOrder(ethereumSellOrder3)
        cryptoOrderManager.placeOrder(ethereumSellOrder4)
        cryptoOrderManager.placeOrder(buyOrder)
        cryptoOrderManager.placeOrder(otherCoinOrder)

        val summaries: List<String> = cryptoOrderManager.liveOrderSummaries(CryptoOrderType.SELL, "Ethereum")

        assertEquals(3, summaries.size)
        assertEquals("353.6 for £13.6", summaries[0])
        assertEquals("441.8 for £13.9", summaries[1])
        assertEquals("50.5 for £14", summaries[2])
    }

    @Test
    fun returnsSummaryOfLiveBitcoinBuyOrders() {
        val bitcoinBuyOrder1 = CryptoOrder(
            CryptoOrderType.BUY,
            "user1",
            "Bitcoin",
            BigDecimal("350.1"),
            Money.of(BigDecimal("13.6"), "GBP")
        )
        val bitcoinBuyOrder2 = CryptoOrder(
            CryptoOrderType.BUY,
            "user2",
            "Bitcoin",
            BigDecimal("50.5"),
            Money.of(BigDecimal("14"), "GBP")
        )
        val bitcoinBuyOrder3 = CryptoOrder(
            CryptoOrderType.BUY,
            "user3",
            "Bitcoin",
            BigDecimal("441.8"),
            Money.of(BigDecimal("13.9"), "GBP")
        )
        val bitcoinBuyOrder4 = CryptoOrder(
            CryptoOrderType.BUY,
            "user4",
            "Bitcoin",
            BigDecimal("3.5"),
            Money.of(BigDecimal("13.6"), "GBP")
        )
        val sellOrder = CryptoOrder(
            CryptoOrderType.SELL,
            "user2",
            "Bitcoin",
            BigDecimal("10.25"),
            Money.of(BigDecimal("13.9"), "GBP")
        )
        val otherCoinOrder = CryptoOrder(
            CryptoOrderType.BUY,
            "user1",
            "Litecoin",
            BigDecimal("2.89"),
            Money.of(BigDecimal("12.5"), "GBP")
        )

        cryptoOrderManager.placeOrder(bitcoinBuyOrder1)
        cryptoOrderManager.placeOrder(bitcoinBuyOrder2)
        cryptoOrderManager.placeOrder(bitcoinBuyOrder3)
        cryptoOrderManager.placeOrder(bitcoinBuyOrder4)
        cryptoOrderManager.placeOrder(sellOrder)
        cryptoOrderManager.placeOrder(otherCoinOrder)

        val summaries: List<String> = cryptoOrderManager.liveOrderSummaries(CryptoOrderType.BUY, "Bitcoin")

        assertEquals(3, summaries.size)
        assertEquals("50.5 for £14", summaries[0])
        assertEquals("441.8 for £13.9", summaries[1])
        assertEquals("353.6 for £13.6", summaries[2])
    }

    @Test
    fun returnEmptySummaryIfNotResults() {
        val bitcoinBuyOrder1 = CryptoOrder(
            CryptoOrderType.BUY,
            "user",
            "Bitcoin",
            BigDecimal("3.5"),
            Money.of(BigDecimal("13.6"), "GBP")
        )
        val bitcoinBuyOrder2 = CryptoOrder(
            CryptoOrderType.BUY,
            "user",
            "Bitcoin",
            BigDecimal("10.25"),
            Money.of(BigDecimal("13.9"), "GBP")
        )

        cryptoOrderManager.placeOrder(bitcoinBuyOrder1)
        cryptoOrderManager.placeOrder(bitcoinBuyOrder2)

        val summaries: List<String> = cryptoOrderManager.liveOrderSummaries(CryptoOrderType.BUY, "Ethereum")

        assertEquals(emptyList<String>(), summaries)
    }
}