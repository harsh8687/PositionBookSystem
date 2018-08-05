# PositionBookSystem
A Position Book system that can process trade events and from which a user can retrieve a particular position identified by a trading account and security

Note:-
1. This application runs in a single-threaded environment.
2. Since the data is maintained in-memory, stored data remains in the scope until the main method is running.
3. PositionBookCmdLineUser is the main method allowing the user to add new trade events and view existing positions.
