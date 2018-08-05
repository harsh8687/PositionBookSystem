package com.jpmc.user;

import com.jpmc.positionBookSystem.position.Position;
import com.jpmc.positionBookSystem.position.PositionBook;
import com.jpmc.positionBookSystem.position.TradeEvent;

import java.util.Scanner;

public class PositionBookCmdLineUser {

    public static void main(String[] args) {
        printUsage();
        try (Scanner scanner = new Scanner(System.in)) {
            int operation = -1;
            while (operation != 3) {
                try {
                    System.out.println("Enter mode of operation:");
                    operation = scanner.nextInt();

                    String account;
                    String secIdentifier;

                    switch (operation) {
                        case 1:
                            System.out.println("Enter event ID:");
                            String eventId = scanner.next();

                            System.out.println("Enter event type (BUY/SELL/CANCEL):");
                            String eventType = scanner.next();

                            System.out.println("Enter account:");
                            account = scanner.next();

                            System.out.println("Enter security identifier:");
                            secIdentifier = scanner.next();

                            System.out.println("Enter trade quantity (numerical value):");
                            String quantity = scanner.next();

                            TradeEvent event = new TradeEvent.Builder(eventId, eventType)
                                    .account(account)
                                    .securityIdentifier(secIdentifier)
                                    .quantity(quantity)
                                    .build();
                            Position position = PositionBook.registerEvent(event);
                            System.out.println(position);
                            break;
                        case 2:
                            System.out.println("Enter account:");
                            account = scanner.next();

                            System.out.println("Enter security identifier:");
                            secIdentifier = scanner.next();
                            System.out.println(PositionBook.getPosition(account, secIdentifier));
                            break;
                        case 3:
                            System.out.println("Thank you!");
                            break;
                        default:
                            System.out.println("Error:: Invalid operation!");
                            printUsage();
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error:: " + e.getMessage());
                }
            }
        }
    }

    private static void printUsage() {
        System.out.println("Available features");
        System.out.println("---------------------");
        System.out.println("1. New Trade Event");
        System.out.println("2. View Position");
        System.out.println("3. Exit");
        System.out.println();
    }
}