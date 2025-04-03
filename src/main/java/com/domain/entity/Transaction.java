package com.domain.entity;

import java.math.BigDecimal;

public record Transaction(
    Long id,
    Long walletId,
    BigDecimal amount,
    String createdAt
) {
}
