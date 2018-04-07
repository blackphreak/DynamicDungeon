package me.blackphreak.dynamicdungeon.Command.Admin;

import com.caxerx.mc.dynamicdungeon.command.CommandNode;
import com.caxerx.mc.dynamicdungeon.command.DefaultCommand;

public class DefaultAdminCommand extends DefaultCommand {
    public DefaultAdminCommand(CommandNode parent) {
        super(parent, "admin", "dynamicdungeon.admin", "Admin commands", null);
        addSub(new BuildAdminCommand(this));
        addSub(new DestroyAdminCommand(this));
        addSub(new EditAdminCommand(this));
        addSub(new JoinAdminCommand(this));
        addSub(new KillSessionAdminCommand(this));
        addSub(new ListSessionAdminCommand(this));
        addSub(new SaveAdminCommand(this));
    }
}
