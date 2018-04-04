package com.spatil.common.util;

public enum Errors {
    SUCCESS(000,"SUCCESS"),
    NOTSTARTED(101, "Game not started"),
    MOREUSERSAGAINSTCOMPUTER(102, "More than one user playing against computer"),
    OTHERUSERTURN(103,"It's not your turn"),
    ALREADYWON(104,"Its in won state"),
    ALREADYDRAWN(105,"Its in lost state"),
    ALREADYPRESENT(106,"Position is not vacant"),
    OUTOFBOUNDLOC(106,"Position is out of the board");

    private final int code;
    private final String description;

    private Errors(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
