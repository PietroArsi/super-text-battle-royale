package org.supertextbattleroyale.players.statuses;

public abstract class Status {

    private final Type type;

    public Status(Type type) {
        this.type = type;
    }

    public enum Type {
        MOVING("in movimento"),
        COMBAT("in combattimento"),
        FLEEING("in fuga"),
        DEAD("morto"),
        ON_DOOR("alla porta");

        String label;

        Type(String label) {
            label = label;
        }
    }

    /**
     * Implement task
     *
     * @return the new status of the player
     */
    public abstract Status doStatusAction();

}
