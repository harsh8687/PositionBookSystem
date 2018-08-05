package com.jpmc.positionBookDystem.position;

import com.jpmc.positionBookDystem.validation.ValidationUtil;

public abstract class Event {

    private String eventId;
    private String account;
    private String securityIdentifier;

    Event(String eventId, String account, String securityIdentifier) {
        ValidationUtil.validateNotNullAndEmpty(eventId, "Invalid event id!");
        this.eventId = eventId;
        ValidationUtil.validateNotNullAndEmpty(account, "Invalid account!");
        this.account = account;
        ValidationUtil.validateNotNullAndEmpty(securityIdentifier, "Invalid security identifier!");
        this.securityIdentifier = securityIdentifier;
    }

    public String getEventId() {
        return eventId;
    }

    public String getAccount() {
        return account;
    }

    public String getSecurityIdentifier() {
        return securityIdentifier;
    }
}
