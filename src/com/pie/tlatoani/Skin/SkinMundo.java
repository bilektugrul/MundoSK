package com.pie.tlatoani.Skin;

import ch.njol.skript.classes.Serializer;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.ParseContext;
import ch.njol.yggdrasil.Fields;
import com.pie.tlatoani.Mundo;
import com.pie.tlatoani.Skin.MineSkin.ExprRetrievedSkin;
import com.pie.tlatoani.Util.Logging;
import com.pie.tlatoani.Registration.Registration;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;

/**
 * Created by Tlatoani on 8/8/17.
 */
public class SkinMundo {
    
    public static void load() {
        //SkinManager.loadReflectionStuff();
        //SkinManager.loadPacketEvents();
        ProfileManager.loadReflectionStuff();
        ProfileManager.loadPacketEvents();

        /*Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onJoin(PlayerJoinEvent event) {
                SkinManager.onJoin(event.getPlayer());
            }
        }, Mundo.INSTANCE);*/
        Bukkit.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onQuit(PlayerQuitEvent event) {
                //SkinManager.onQuit(event.getPlayer());
                ProfileManager.onQuit(event.getPlayer());
            }
        }, Mundo.INSTANCE);

        Registration.registerType(Skin.class, "skin", "skintexture")
                .document("Skin Texture", "1.8", "Represents a skin, possibly of a player. Write 'steve' or 'alex' for these respective skins.")
                .example("skin with name \"eyJ0aW1lc3RhbXAiOjE0NzQyMTc3NjkwMDAsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlODY2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJJbnZlbnRpdmVHYW1lcyIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWE5MmI0NTY2ZjlhMjg2OTNlNGMyNGFiMTQxNzJjZDM0MjdiNzJiZGE4ZjM0ZDRhNjEwODM3YTQ3ZGEwZGUifX19\" signature \"pRQbSEnKkNmi0uW7r8H4xzoWS3E4tkWNbiwwRYgmvITr0xHWSKii69TcaYDoDBXGBwZ525Ex5z5lYe5Xg6zb7pyBPiTJj8J0QdKenQefVnm6Vi1SAR1uN131sRddgK2Gpb2z0ffsR9USDjJAPQtQwCqz0M7sHeXUJhuRxnbznpuZwGq+B34f1TqyVH8rcOSQW9zd+RY/MEUuIHxmSRZlfFIwYVtMCEmv4SbhjLNIooGp3z0CWqDhA7GlJcDFb64FlsJyxrAGnAsUwL2ocoikyIQceyj+TVyGIEuMIpdEifO6+NkCnV7v+zTmcutOfA7kHlj4d1e5ylwi3/3k4VKZhINyFRE8M8gnLgbVxNZ4mNtI3ZMWmtmBnl9dVujyo+5g+vceIj5Admq6TOE0hy7XoDVifLWyNwO/kSlXl34ZDq1MCVN9f1ryj4aN7BB8/Tb2M4sJf3YoGi0co0Hz/A4y14M5JriG21lngw/vi5Pg90GFz64ASssWDN9gwuf5xPLUHvADGo0Bue8KPZPyI0iuIi/3sZCQrMcdyVcur+facIObTQhMut71h8xFeU05yFkQUOKIQswaz2fpPb/cEypWoSCeQV8T0w0e3YKLi4RaWWvKS1MFJDHn7xMYaTk0OhALJoV5BxRD8vJeRi5jYf3DjEgt9+xB742HrbVRDlJuTp4=\"")
                .example("player's skin")
                .example("alex")
                .example("steve")
                .parser(new Registration.SimpleParser<Skin>() {
            @Override
            public Skin parse(String s, ParseContext parseContext) {
                if (s.equalsIgnoreCase("STEVE")) {
                    return Skin.STEVE;
                }
                if (s.equalsIgnoreCase("ALEX")) {
                    return Skin.ALEX;
                }
                return null;
            }
        }).serializer(new Serializer<Skin>() {
            @Override
            public Fields serialize(Skin skin) throws NotSerializableException {
                Fields fields = new Fields();
                fields.putObject("value", skin.value);
                fields.putObject("signature", skin.signature);
                return fields;
            }

            @Override
            public void deserialize(Skin skin, Fields fields) throws StreamCorruptedException, NotSerializableException {
                throw new UnsupportedOperationException("Skin does not have a nullary constructor!");
            }

            @Override
            public Skin deserialize(Fields fields) throws StreamCorruptedException, NotSerializableException {
                try {
                    String value = (String) fields.getObject("value");
                    String signature = (String) fields.getObject("signature");
                    Logging.debug(SkinMundo.class, "value: " + value + ", signature: " + signature);
                    return new Skin(value, signature);
                } catch (StreamCorruptedException | ClassCastException e) {
                    try {
                        String value = (String) fields.getObject("value");
                        Logging.debug(SkinMundo.class, "value: " + value);
                        Object parsedObject = new JSONParser().parse(value);
                        Logging.debug(SkinMundo.class, "parsedobject: " + parsedObject);
                        JSONObject jsonObject;
                        if (parsedObject instanceof JSONObject) {
                            jsonObject = (JSONObject) parsedObject;
                        } else {
                            jsonObject = (JSONObject) ((JSONArray) parsedObject).get(0);
                        }
                        return Skin.fromJSON(jsonObject);
                    } catch (ParseException | ClassCastException e1) {
                        throw new StreamCorruptedException();
                    }
                }
            }

            @Override
            public boolean mustSyncDeserialization() {
                return false;
            }

            @Override
            protected boolean canBeInstantiated() {
                return false;
            }
        });
        Registration.registerExpression(ExprSkinWith.class, Skin.class, ExpressionType.PROPERTY, "skin [texture] (with|of) value %string% signature %string%")
                .document("Skin with Value", "1.8", "An expression for a skin with the specified value and signature.");
        Registration.registerExpression(ExprSkinOf.class, Skin.class, ExpressionType.PROPERTY, "skin [texture] of %player/itemstack%", "%player/itemstack%'s skin")
                .document("Skin of Player or Skull", "1.8", "An expression for the skin of the specified player (must be online) or skull item.");
        Registration.registerExpression(ExprDisplayedSkinOfPlayer.class, Skin.class, ExpressionType.PROPERTY, "displayed skin of %player% [(for %-players%|excluding %-players%)]", "%player%'s displayed skin [(for %-players%|excluding %-players%)]")
                .document("Displayed Skin of Player", "1.8", "An expression for the skin currently being displayed as the specified player's skin. "
                        + "If target ('for') players are specified, the expression will return a skin for each target player specified. "
                        + "Excluded players are meant to be specified only when setting the expression (for example, to prevent the original specified player from seeing a change). "
                        + "If the expression is evaluated with excluded players specified, it will act the same as if no target or excluded players had been specified.");
        Registration.registerExpression(ExprSkullFromSkin.class, ItemStack.class, ExpressionType.PROPERTY, "skull from %skin%")
                .document("Skull from Skin", "1.8", "An expression for a skull bearing the specified skin.");
        Registration.registerExpression(ExprRetrievedSkin.class, Skin.class, ExpressionType.PROPERTY, "retrieved [(4¦slim)] skin (from (0¦file|1¦url) %-string%|2¦of %-offlineplayer%) [[with] timeout %-timespan%]")
                .document("Retrieved Skin", "1.8", "An expression for a skin retrieved using the Mineskin API:"
                        , "A skin recreated from the specified image file,"
                        , "A skin recreated from the specified URL of an image, or"
                        , "The skin of the specified offline player retrieved from Mojang");
        Registration.registerExpression(ExprNameTagOfPlayer.class, String.class, ExpressionType.PROPERTY, "[mundo[sk]] %player%'s name[]tag [for %-players%]", "[mundo[sk]] name[]tag of %player% [for %-players%]")
                .document("Nametag of Player", "1.8", "An expression for the nametag (the name that appears above a player's head) of the specified player.");
        Registration.registerExpression(ExprTabName.class, String.class, ExpressionType.PROPERTY, "%player%'s [mundo[sk]] tab[list] name", "[mundo[sk]] tab[list] name of %player%");
                //.document("Tablist Name of Player", "1.8", "An expression for the tablist name of the specified player.");
    }
}
