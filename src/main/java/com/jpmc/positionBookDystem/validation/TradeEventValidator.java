package com.jpmc.positionBookDystem.validation;

import com.jpmc.positionBookDystem.position.Position;
import com.jpmc.positionBookDystem.position.TradeEvent;

@FunctionalInterface
public interface TradeEventValidator {
    static boolean validatePositionQuantity(TradeEvent event, Position position) {
        long existingQuantity = position.getTradeEventList().stream()
                .map(TradeEvent::getQuantity)
                .reduce(0L, Long::sum);
        if (existingQuantity + event.getQuantity() < 0) {
            throw new IllegalArgumentException("Not enough quantity available to transact for this position!");
        }
        return true;
    }

    boolean isValid(TradeEvent e, Position position);
}
