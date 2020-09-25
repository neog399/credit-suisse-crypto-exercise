package org.credit.suisse.crypto.exercise

import org.javamoney.moneta.format.CurrencyStyle
import java.math.BigDecimal
import java.util.*
import javax.money.MonetaryAmount
import javax.money.format.AmountFormatQueryBuilder
import javax.money.format.MonetaryAmountFormat
import javax.money.format.MonetaryFormats

class CryptoOrderManager {
    private val monetaryFormat: MonetaryAmountFormat = MonetaryFormats.getAmountFormat(
        AmountFormatQueryBuilder.of(Locale.UK)
            .set(CurrencyStyle.SYMBOL)
            .set("pattern", "¤####.##")
            .build()
    )

    // Change this to a thread-safe map (e.g. ConcurrentHashMap) for multi-threaded access
    private val orders: MutableMap<UUID, CryptoOrder> = mutableMapOf()

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

    // From the exercise, it was unclear whether SELL and BUY orders should be displayed together
    // or whether orders for different coin types should be merged together
    // For the sake of simplicity, I chose the conservative approach and displayed them separately
    fun liveOrderSummaries(orderType: CryptoOrderType, coinType: String): List<String> {
        val quantitiesPrice: MutableMap<MonetaryAmount, QuantityPerPrice> = mutableMapOf()

        // The merging and sorting could also be done when placing the order (synchronously or asynchronously, depending on specifics)
        // That would significantly speed up and simplify this method
        // This would be especially beneficial in a system where we have a lot more reads than writes
        for ((_, order) in orders) {
            if (order.type !== orderType || order.coinType !== coinType) {
                continue
            }

            quantitiesPrice.compute(order.pricePerCoin) { _, quantityPerPrice ->
                if (quantityPerPrice == null) {
                    QuantityPerPrice(order.quantity, order.pricePerCoin)
                } else {
                    QuantityPerPrice(quantityPerPrice.quantity.plus(order.quantity), quantityPerPrice.price)
                }
            }
        }

        val sortedQuantitiesPerPrice = if (orderType == CryptoOrderType.SELL) {
            quantitiesPrice.values.sortedBy { it.price }
        } else {
            quantitiesPrice.values.sortedByDescending { it.price }
        }

        // In a real-life system we'd have pagination here, but again, tried to keep things simple
        return sortedQuantitiesPerPrice.map { "${it.quantity} for ${monetaryFormat.format(it.price)}" }
    }

    internal data class QuantityPerPrice(val quantity: BigDecimal, val price: MonetaryAmount)
}
