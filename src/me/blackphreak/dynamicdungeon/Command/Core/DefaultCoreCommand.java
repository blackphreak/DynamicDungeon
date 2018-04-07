package me.blackphreak.dynamicdungeon.Command.Core;

import me.blackphreak.dynamicdungeon.Command.Admin.DefaultAdminCommand;
import com.caxerx.mc.dynamicdungeon.command.DefaultCommand;

public class DefaultCoreCommand extends DefaultCommand {
    public DefaultCoreCommand() {
        super(null, "dd", null, "Main command of Dynamic Dungeon", null);
        addAlias("dynamicdungeon");
        addSub(new DefaultAdminCommand(this));
    }
}
