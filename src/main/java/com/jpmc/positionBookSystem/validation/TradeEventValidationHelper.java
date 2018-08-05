package com.jpmc.positionBookSystem.validation;

import com.jpmc.positionBookSystem.position.Position;
import com.jpmc.positionBookSystem.position.TradeEvent;

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