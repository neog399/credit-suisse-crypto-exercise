package org.credit.suisse.crypto.excercise

import java.math.BigDecimal
import javax.money.MonetaryAmount

data class CryptoOrder(
        val type: CryptoOrderType,
        val userId: String,
        val coinType: String,   // Could use an enum here if there's a fixed list of supported coin types
        val quantity: BigDecimal,
        val pricePerCoin: MonetaryAmount
)

enum class CryptoOrderType {
    BUY, SELL
}
