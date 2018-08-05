package com.jpmc.positionBookDystem.position;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class PositionBookTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @After
    public void tearDown() {
        PositionBook.resetPositionBook();
    }

    @Test
    public void testGetPosition() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        PositionBook.registerEvent(event);

        Position position = PositionBook.getPosition("ACC", "SEC");
        assertEquals("ACC", position.getAccount());
        assertEquals("SEC", position.getSecurityIdentifier());
        assertEquals(100, position.getQuantity());
        assertEquals(1, position.getTradeEventList().size());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("No position available");
        PositionBook.getPosition("ACC", "SEC1");
    }

    @Test
    public void testEventIdExists() {
        long eventId = System.currentTimeMillis();
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        Position position = PositionBook.registerEvent(event);
        assertEquals(1, position.getTradeEventList().size());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid event ID: 1. It already exists");
        event = new TradeEvent.Builder("1", "SELL")
                .account("ACC1")
                .securityIdentifier("SEC1")
                .quantity("50")
                .build();
        PositionBook.registerEvent(event);
    }

    @Test
    public void testMultiplePositions() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        Position position1 = PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("2", "BUY")
                .account("ACC1")
                .securityIdentifier("SEC1")
                .quantity("90")
                .build();
        Position position2 = PositionBook.registerEvent(event);

        assertEquals("ACC", position1.getAccount());
        assertEquals("SEC", position1.getSecurityIdentifier());
        assertEquals(100, position1.getQuantity());
        assertEquals(1, position1.getTradeEventList().size());

        assertEquals("ACC1", position2.getAccount());
        assertEquals("SEC1", position2.getSecurityIdentifier());
        assertEquals(90, position2.getQuantity());
        assertEquals(1, position2.getTradeEventList().size());

    }

    @Test
    public void testCANCELEvent() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        Position position = PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("2", "SELL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("70")
                .build();
        PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("2", "CANCEL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("1100") //Does not matter
                .build();
        PositionBook.registerEvent(event);
        assertEquals(100, position.getQuantity());
        assertEquals(3, position.getTradeEventList().size());
    }

    @Test
    public void testNonExistentCANCELEvent() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        Position position = PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("2", "SELL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("70")
                .build();
        PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("2", "CANCEL")
                .account("ACC")
                .securityIdentifier("SEC1")
                .quantity("1100") //Does not matter
                .build();
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Event 2 not found");
        PositionBook.registerEvent(event);
    }

    @Test
    public void testInvalidCANCELEvent() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        Position position = PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("2", "SELL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("70")
                .build();
        PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("4", "CANCEL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("1100") //Does not matter
                .build();
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("No event available");
        PositionBook.registerEvent(event);
    }

    @Test
    public void testAlreadyCANCELLEDevent() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        Position position = PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("2", "SELL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("70")
                .build();
        PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("2", "CANCEL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("1100") //Does not matter
                .build();
        PositionBook.registerEvent(event);
        assertEquals(100, position.getQuantity());
        assertEquals(3, position.getTradeEventList().size());

        event = new TradeEvent.Builder("2", "CANCEL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("11000") //Does not matter
                .build();
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid event ID: 2 . It is already cancelled");
        PositionBook.registerEvent(event);
    }

    @Test
    public void testFirstSELLEvent() {
        TradeEvent event = new TradeEvent.Builder("1", "SELL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Not enough quantity available");
        PositionBook.registerEvent(event);
    }

    @Test
    public void testQuantityAggregation() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        Position position = PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("2", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("3", "SELL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("110")
                .build();
        PositionBook.registerEvent(event);
        assertEquals(90, position.getQuantity());

        assertEquals(3, position.getTradeEventList().size());
    }

    @Test
    public void testInsufficientQuantityWithSELL() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        Position position = PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("2", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("3", "SELL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("210")
                .build();
        assertEquals(2, position.getTradeEventList().size());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Not enough quantity available");
        PositionBook.registerEvent(event);
    }

    @Test
    public void testInsufficientQuantityWithCANCEL() {
        TradeEvent event = new TradeEvent.Builder("1", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        Position position = PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("2", "BUY")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("100")
                .build();
        PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("3", "SELL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("110")
                .build();
        PositionBook.registerEvent(event);

        event = new TradeEvent.Builder("1", "CANCEL")
                .account("ACC")
                .securityIdentifier("SEC")
                .quantity("5000") // Does not matter
                .build();
        assertEquals(3, position.getTradeEventList().size());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Not enough quantity available");
        PositionBook.registerEvent(event);
    }
}