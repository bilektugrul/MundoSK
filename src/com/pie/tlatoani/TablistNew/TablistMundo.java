package com.pie.tlatoani.TablistNew;

import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.ParseContext;
import com.pie.tlatoani.Mundo;
import com.pie.tlatoani.Skin.ExprTabName;
import com.pie.tlatoani.Skin.Skin;
import com.pie.tlatoani.Skin.SkinManager;
import com.pie.tlatoani.TablistNew.Array.EffMaximizeTablist;
import com.pie.tlatoani.TablistNew.Array.EffMinimizeTablist;
import com.pie.tlatoani.TablistNew.Array.EffSetArrayTablist;
import com.pie.tlatoani.TablistNew.Simple.ExprIconOfTab;
import com.pie.tlatoani.Util.Registration;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Tlatoani on 8/12/17.
 */
public class TablistMundo {
    public static int SPAWN_REMOVE_TAB_DELAY;
    public static int RESPAWN_REMOVE_TAB_DELAY;

    public static void load(int spawnRemoveTabDelay, int respawnRemoveTabDelay) {
        Registration.registerExpression(ExprTabName.class, String.class, ExpressionType.PROPERTY, "%player%'s [mundo[sk]] tab[list] name", "[mundo[sk]] tab[list] name of %player%");
        Registration.registerType(OldTablist.class, "tablist").parser(new Registration.SimpleParser<OldTablist>() {
            @Override
            public OldTablist parse(String s, ParseContext parseContext) {
                if (s.equals("global tablist")) {
                    return OldTablist.GLOBAL;
                }
                return null;
            }
        });
        Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent event) {
                OldTablist.onJoin(event.getPlayer());
                SkinManager.onJoin(event.getPlayer());
            }
        }, Mundo.INSTANCE);
        Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onQuit(PlayerQuitEvent event) {
                OldTablist.onQuit(event.getPlayer());
                SkinManager.onQuit(event.getPlayer());
            }
        }, Mundo.INSTANCE);
        Registration.registerExpression(ExprTablist.class, OldTablist.class, ExpressionType.PROPERTY, "tablist of %player%", "%player%'s tablist");
        Registration.registerExpression(ExprTablistContainsPlayers.class, Boolean.class, ExpressionType.PROPERTY, "%tablist% contains players");
        Registration.registerExpression(ExprNewTablist.class, OldTablist.class, ExpressionType.SIMPLE, "new tablist");
        Registration.registerExpression(ExprScoresEnabled.class, Boolean.class, ExpressionType.PROPERTY, "scores enabled in %tablist%");
        Registration.registerExpression(ExprTablistName.class, String.class, ExpressionType.PROPERTY, "tablist name of %player% (in %-tablist%|for %-player%)", "%player%'s tablist name (in %-tablist%|for %-player%)");
        Registration.registerExpression(ExprTablistScore.class, Number.class, ExpressionType.PROPERTY, "tablist score of %player% (in %-tablist%|for %-player%)", "%player%'s tablist score (in %-tablist%|for %-player%)");
        Registration.registerEffect(EffChangePlayerVisibility.class, "(0¦show|1¦hide) %players% (in %-tablist%|for %-player% in tablist)");
        {
            //Simple
            Registration.registerEffect(com.pie.tlatoani.TablistNew.Simple.EffCreateNewTab.class, "create tab id %string% (in %-tablist%|for %-player%) with [display] name %string% [(ping|latency) %-number%] [(head|icon|skull) %-skin%] [score %-number%]");
            Registration.registerEffect(com.pie.tlatoani.TablistNew.Simple.EffDeleteTab.class, "delete tab id %string% (in %-tablist%|for %-player%)");
            Registration.registerEffect(com.pie.tlatoani.TablistNew.Simple.EffRemoveAllIDTabs.class, "delete all id personalTabs (in %-tablist%|for %-player%)");
            Registration.registerExpression(com.pie.tlatoani.TablistNew.Simple.ExprDisplayNameOfTab.class, String.class, ExpressionType.PROPERTY, "[display] name of tab id %string% (in %-tablist%|for %-player%)");
            Registration.registerExpression(com.pie.tlatoani.TablistNew.Simple.ExprLatencyOfTab.class, Number.class, ExpressionType.PROPERTY, "(latency|ping) of tab id %string% (in %-tablist%|for %-player%)");
            Registration.registerExpression(ExprIconOfTab.class, Skin.class, ExpressionType.PROPERTY, "(head|icon|skull) of tab id %string% (in %-tablist%|for %-player%)");
            Registration.registerExpression(com.pie.tlatoani.TablistNew.Simple.ExprScoreOfTab.class, Number.class, ExpressionType.PROPERTY, "score of tab id %string% (in %-tablist%|for %-player%)");
        } {
            //Array
            Registration.registerEffect(EffSetArrayTablist.class, "deactivate array tablist for %player%", "activate array tablist for %player% [with [%-number% columns] [%-number% rows] [initial (head|icon|skull) %-skin%]]");
            Registration.registerEffect(EffMaximizeTablist.class, "maximize array tablist %tablist%");
            Registration.registerEffect(EffMinimizeTablist.class, "minimize array tablist %tablist%");
            Registration.registerExpression(com.pie.tlatoani.TablistNew.Array.ExprDisplayNameOfTab.class, String.class, ExpressionType.PROPERTY, "[display] name of tab %number%, %number% (in %-tablist%|for %-player%)");
            Registration.registerExpression(com.pie.tlatoani.TablistNew.Array.ExprLatencyOfTab.class, Number.class, ExpressionType.PROPERTY, "(latency|ping) of tab %number%, %number% (in %-tablist%|for %-player%)");
            Registration.registerExpression(com.pie.tlatoani.TablistNew.Array.ExprIconOfTab.class, Skin.class, ExpressionType.PROPERTY, "(head|icon|skull) of tab %number%, %number% (in %-tablist%|for %-player%)", "initial icon of (%-tablist%|%player%'s [array] tablist)");
            Registration.registerExpression(com.pie.tlatoani.TablistNew.Array.ExprScoreOfTab.class, Number.class, ExpressionType.PROPERTY, "score of tab %number%, %number% (in %-tablist%|for %-player%)");
            Registration.registerExpression(com.pie.tlatoani.TablistNew.Array.ExprSizeOfTabList.class, Number.class, ExpressionType.PROPERTY, "amount of (0¦column|1¦row)s in (%-tablist%|%-player%'s [array] tablist)");
        }
    }
}
