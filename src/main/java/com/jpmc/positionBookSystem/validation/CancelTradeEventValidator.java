package com.jpmc.positionBookSystem.validation;

import com.jpmc.positionBookSystem.position.Position;
import com.jpmc.positionBookSystem.position.TradeEvent;
import com.jpmc.positionBookSystem.position.TradeEventType;

public class CancelTradeEventValidator implements TradeEventValidator {

    @Override
    public boolean isValid(TradeEvent event, Position position) {
        return validateCancelEvent(event, position) && validatePositionQuantity(event, position);
    }

    boolean validatePositionQuantity(TradeEvent event, Position position) {
        long existingQuantity = position.getTradeEventList().stream()
                .filter(x -> !x.getEventId().equals(event.getEventId()))
                .map(TradeEvent::getQuantity)
                .reduce(0L, Long::sum);
        if (existingQuantity < 0) {
            throw new IllegalArgumentException("Not enough quantity available to transact for this position!");
        }
        return true;
    }

    boolean validateCancelEvent(TradeEvent event, Position position) {
        if (position.getTradeEventList().stream()
                .filter(x -> x.getEventId().equals(event.getEventId()))
                .noneMatch(y -> y.getEventId().equals(event.getEventId()))) {
            throw new IllegalArgumentException("Event " + event.getEventId() + " not found!");
        }

        if (position.getTradeEventList().stream()
                .filter(x -> x.getEventId().equals(event.getEventId())
                        && TradeEventType.CANCEL == x.getTradeEventType())
                .anyMatch(y -> y.getEventId().equals(event.getEventId()))) {
            throw new IllegalArgumentException("Invalid event ID: " + event.getEventId() + " . It is already cancelled!");
        }
        return true;
    }
}
