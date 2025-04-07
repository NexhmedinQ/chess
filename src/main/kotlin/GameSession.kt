package org.example

class GameSession {
    // on game creation send start game message to clients
    // inform which player is white/black

    // game session will then listen for messages from either player

    /*
    Client will only send move messages:
    - validate move is possible
    - update internal state
    - check for check or checkmate
    - inform both clients of move
     */
}