package de.uol.swp.server.game.player;

import de.uol.swp.common.user.User;

/**
 * Der InGame Spieler
 */
public class Player {

    private String playerName;
    private User theUserInThePlayer;
    private Deck playerDeck = new Deck();
    private int availableActions;
    private int availableBuys;
    private int additionalMoney;

    /**
     * Erstellt einen neuen Spieler
     *
     * @param playerName Der Spielername
     */
    public Player(String playerName) {
        this.playerName = playerName;
        availableActions = 1;
        availableBuys = 1;
        additionalMoney = 0;
    }

    /*
    Nachfolgende Methoden speichern den zugehörigen User zum Player im Player, da bei der Konvertierung User->Player ein Späterer vergleich
    bzgl. der SessionId verloren geht.
    TODO: Nach der nächsten Absprache mit der Gruppe evtl. restrukturierung?
     */
    public User getTheUserInThePlayer() {
        return theUserInThePlayer;
    }

    public void setTheUserInThePlayer(User theUserInThePlayer) {
        this.theUserInThePlayer = theUserInThePlayer;
    }

    /**
     * Macht das PlayerDeck des Spielers zugreifbar.
     *
     * @return Das Deck mit der Hand.
     */
    public Deck getPlayerDeck() {
        return playerDeck;
    }

    /**
     * Gibt den Spielernamen zurück
     *
     * @return Der Spielername
     */
    public String getPlayerName() {
        return playerName;
    }

    public int getAvailableActions() {
        return availableActions;
    }

    public void setAvailableActions(int availableActions) {
        this.availableActions = availableActions;
    }

    public int getAvailableBuys() {
        return availableBuys;
    }

    public void setAvailableBuys(int availableBuys) {
        this.availableBuys = availableBuys;
    }

    public int getAdditionalMoney() {
        return additionalMoney;
    }

    public void setAdditionalMoney(int additionalMoney) {
        this.additionalMoney = additionalMoney;
    }
}
