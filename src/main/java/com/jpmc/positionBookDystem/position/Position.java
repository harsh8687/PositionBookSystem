package com.jpmc.positionBookDystem.position;

import com.jpmc.positionBookDystem.validation.TradeEventValidationHelper;
import com.jpmc.positionBookDystem.validation.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class Position {

    private String account;
    private String securityIdentifier;
    private long quantity;
    private List<TradeEvent> tradeEventList = new ArrayList<TradeEvent>() {

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (TradeEvent event : this) {
                sb.append("[id:");
                sb.append(event);
                sb.append("]");
                sb.append(System.lineSeparator());
            }
            return sb.toString();
        }
    };

    Position(String account, String securityIdentifier) {
        ValidationUtil.validateNotNullAndEmpty(account, "Invalid account!");
        this.account = account;
        ValidationUtil.validateNotNullAndEmpty(securityIdentifier, "Invalid security identifier!");
        this.securityIdentifier = securityIdentifier;
    }

    public String getAccount() {
        return account;
    }

    public String getSecurityIdentifier() {
        return securityIdentifier;
    }

    public long getQuantity() {
        return quantity;
    }

    private void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public List<TradeEvent> getTradeEventList() {
        return new ArrayList<>(tradeEventList);
    }

    /**
     * This method adds new trade event for a particular position.
     * In case of CANCEL event, its quantity is set to negation of that of the event which is to be cancelled
     * so that the latter is considered void
     *
     * @param event
     */
    void addTradeEvent(TradeEvent event) {
        if (TradeEventValidationHelper.isTradeEventValid(event, this)) {
            if (TradeEventType.CANCEL == event.getTradeEventType()) {
                TradeEvent eventToBeCancelled = tradeEventList
                        .stream()
                        .filter(x -> x.getEventId().equals(event.getEventId()))
                        .findFirst().get();
                event.setQuantity(Math.negateExact(eventToBeCancelled.getQuantity()));
            }
            tradeEventList.add(event);
            updatePosition();
        }
    }

    /*
    Once a new event is registered, the total quantity of the position needs to be updated.
     */
    private void updatePosition() {
        this.setQuantity(this.getTradeEventList()
                .stream()
                .map(TradeEvent::getQuantity)
                .reduce(0L, Long::sum));
    }

    @Override
    public String toString() {
        return "Position: "
                + account + " "
                + securityIdentifier + " "
                + quantity + "\n" +
                tradeEventList;
    }
}