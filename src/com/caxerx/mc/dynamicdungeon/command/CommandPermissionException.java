package com.caxerx.mc.dynamicdungeon.command;

public class CommandPermissionException extends RuntimeException {
    public CommandPermissionException(String msg) {
        super(msg);
    }
}
