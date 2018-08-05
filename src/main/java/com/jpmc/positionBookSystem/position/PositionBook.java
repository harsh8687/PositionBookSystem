package com.jpmc.positionBookSystem.position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This class manages all active positions.
 */
public class PositionBook {

    private static final List<Position> POSITIONS = new ArrayList<>();

    /**
     * This method provides the existing position if any otherwise informs the caller of no existence of such position
     *
     * @param account
     * @param securityIdentifier
     * @return
     */
    public static Position getPosition(String account, String securityIdentifier) {
        return POSITIONS.stream()
                .filter(x -> x.getAccount().equals(account) && x.getSecurityIdentifier().equals(securityIdentifier))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No position available!"));
    }

    /**
     * This method is responsible for validating the incoming trade event and registering to its corresponding position
     *
     * @param event
     * @return
     */
    public static Position registerEvent(TradeEvent event) {

        Position position = null;
        if (validateEventId(event)) {
            position = POSITIONS.stream()
                    .filter(x -> x.getAccount().equals(event.getAccount()) && x.getSecurityIdentifier().equals(event.getSecurityIdentifier()))
                    .findFirst()
                    .orElse(new Position(event.getAccount(), event.getSecurityIdentifier()));

            position.addTradeEvent(event);
            POSITIONS.add(position);
        }
        return position;
    }

    /*
    This method would remove all currently held positions
     */
    static void resetPositionBook() {
        POSITIONS.clear();
    }

    private static boolean validateEventId(TradeEvent event) {

        Supplier<Stream<TradeEvent>> supplier = () -> POSITIONS.stream()
                .map(Position::getTradeEventList)
                .flatMap(Collection::stream);
        switch (event.getTradeEventType()) {
            case BUY:
            case SELL:
                if (supplier.get().anyMatch(x -> x.getEventId().equals(event.getEventId()))) {
                    throw new IllegalArgumentException("Invalid event ID: " + event.getEventId() + ". It already exists!");
                }
                break;
            case CANCEL:
                if (supplier.get().noneMatch(x -> x.getEventId().equals(event.getEventId()))) {
                    throw new IllegalArgumentException("Invalid event ID: " + event.getEventId() + " . No event available!");
                }
                break;
        }
        return true;
    }
}