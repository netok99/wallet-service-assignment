package com.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Transaction(
    Long id,
    Long walletId,
    BigDecimal amount,
    LocalDateTime createdAt
) {
}
