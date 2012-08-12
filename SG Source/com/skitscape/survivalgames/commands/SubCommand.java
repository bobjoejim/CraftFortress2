package com.skitscape.survivalgames.commands;

import org.bukkit.entity.Player;

public abstract interface SubCommand
{
  public abstract boolean onCommand(Player paramPlayer, String[] paramArrayOfString);

  public abstract String help(Player paramPlayer);
}

/* Location:           C:\Users\Alex\Desktop\SurvivalGames B 0.4.10\SurvivalGames.jar
 * Qualified Name:     com.skitscape.survivalgames.commands.SubCommand
 * JD-Core Version:    0.6.0
 */