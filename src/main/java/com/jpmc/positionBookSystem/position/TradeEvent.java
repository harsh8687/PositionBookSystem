package com.jpmc.positionBookSystem.position;

public class TradeEvent extends Event {

    private TradeEventType tradeEventType;
    private long quantity;

    private TradeEvent(Builder builder) {
        super(builder.eventId, builder.account, builder.securityIdentifier);
        this.tradeEventType = builder.tradeEventType;
        long quantityLong;
        try {
            quantityLong = Long.parseLong(builder.quantity);
            switch (this.tradeEventType) {
                case SELL:
                    this.quantity = Math.negateExact(quantityLong);
                    break;
                default:
                    this.quantity = quantityLong;
                    break;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid quantity!");
        }
    }

    public TradeEventType getTradeEventType() {
        return tradeEventType;
    }

    public long getQuantity() {
        return quantity;
    }

    void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return getEventId() + ", "
                + tradeEventType + ", "
                + getAccount() + ", "
                + getSecurityIdentifier() + ", "
                + Math.abs(quantity);
    }

    /**
     * Builder class to construct TradeEvent object
     */
    public static class Builder {
        private String eventId;
        private TradeEventType tradeEventType;
        private String account;
        private String securityIdentifier;
        private String quantity;

        public Builder(String eventId, String eventType) {
            this.eventId = eventId;
            try {
                this.tradeEventType = TradeEventType.valueOf(eventType);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid event type!");
            }
        }

        public Builder account(String account) {
            this.account = account;
            return this;
        }

        public Builder securityIdentifier(String secIdentifier) {
            this.securityIdentifier = secIdentifier;
            return this;
        }

        public Builder quantity(String quantity) {
            this.quantity = quantity;
            return this;
        }

        public TradeEvent build() {
            return new TradeEvent(this);
        }
    }
}