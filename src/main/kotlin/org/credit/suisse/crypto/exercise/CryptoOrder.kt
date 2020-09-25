package org.credit.suisse.crypto.exercise

import java.math.BigDecimal
import java.util.*
import javax.money.MonetaryAmount

open class CryptoOrder(
    val type: CryptoOrderType,
    val userId: String,
    val coinType: String,   // Could use an enum here if there's a fixed list of supported coin types
    val quantity: BigDecimal,
    val pricePerCoin: MonetaryAmount
)

// This class is not strictly necessary (ID could be stored outside the order, see CryptoOrderManager#orders)
// However, it's good design to keep related properties together and the ID is essential for tracking orders
class IdentifiableCryptoOrder(
    val id: UUID,
    type: CryptoOrderType,
    userId: String,
    coinType: String,
    quantity: BigDecimal,
    pricePerCoin: MonetaryAmount
) : CryptoOrder(type, userId, coinType, quantity, pricePerCoin) {
    constructor(id: UUID, order: CryptoOrder) : this(
        id,
        order.type,
        order.userId,
        order.coinType,
        order.quantity,
        order.pricePerCoin
    )

    constructor(order: CryptoOrder): this(UUID.randomUUID(), order)
}

enum class CryptoOrderType {
    BUY, SELL
}
