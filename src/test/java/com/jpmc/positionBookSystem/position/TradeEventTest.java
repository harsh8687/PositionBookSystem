package com.jpmc.positionBookSystem.position;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class TradeEventTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetEventId() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        assertEquals("1", event.getEventId());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid event id");
        new TradeEvent.Builder("", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
    }

    @Test
    public void testGetEventIdNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid event id");
        new TradeEvent.Builder(null, "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
    }

    @Test
    public void testGetTradeEventType() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        assertEquals(TradeEventType.BUY, event.getTradeEventType());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid event type");
        new TradeEvent.Builder("1", "")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
    }

    @Test
    public void testGetTradeEventTypeNull() {
        exception.expect(NullPointerException.class);
        new TradeEvent.Builder("1", null)
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
    }

    @Test
    public void testGetAccount() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        assertEquals("ACC", event.getAccount());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid account");
        new TradeEvent.Builder("1", "BUY")
                .account("")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
    }

    @Test
    public void testGetAccountNull() {
        exception.expect(IllegalArgumentException.class);
        new TradeEvent.Builder("1", "BUY")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
    }

    @Test
    public void testGetSecurityIdentifier() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        assertEquals("ACC", event.getAccount());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid security identifier");
        new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("")
                .quantity("100")
                .build();
    }

    @Test
    public void testGetSecurityIdentifierNull() {
        exception.expect(IllegalArgumentException.class);
        new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .quantity("100")
                .build();
    }

    @Test
    public void testGetQuantity() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        assertEquals(100, event.getQuantity());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid quantity");
        new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100.6")
                .build();
    }

    @Test
    public void testGetQuantityNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid quantity");
        new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity(null)
                .build();
    }

    @Test
    public void tetSetQuantity() {

        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        event.setQuantity(200);
        assertEquals(200, event.getQuantity());
    }
}