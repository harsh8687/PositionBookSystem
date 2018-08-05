package com.jpmc.positionBookDystem.validation;

import com.jpmc.positionBookDystem.position.Position;
import com.jpmc.positionBookDystem.position.TradeEvent;

/**
 * Utility class to validate trade events
 */
public class TradeEventValidationHelper {

    public static boolean isTradeEventValid(TradeEvent event, Position position) {
        switch (event.getTradeEventType()) {
            case CANCEL:
                new CancelTradeEventValidator().isValid(event, position);
                break;
            default:
                TradeEventValidator.validatePositionQuantity(event, position);
                break;
        }
        return true;
    }
}