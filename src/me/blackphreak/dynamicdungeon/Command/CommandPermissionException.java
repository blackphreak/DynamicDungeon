package me.blackphreak.dynamicdungeon.Command;

public class CommandPermissionException extends RuntimeException {
    public CommandPermissionException(String msg) {
        super(msg);
    }
}
