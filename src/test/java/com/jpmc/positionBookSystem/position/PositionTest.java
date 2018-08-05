package com.jpmc.positionBookSystem.position;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class PositionTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetAccount() {

        Position position = new Position("ACC", "SEC");
        assertEquals("ACC", position.getAccount());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid account");
        new Position("", "SEC");
    }

    @Test
    public void testGetSecurityIdentifier() {
        Position position = new Position("ACC", "SEC");
        assertEquals("SEC", position.getSecurityIdentifier());

        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Invalid security identifier");
        new Position("ACC", "");
    }
}