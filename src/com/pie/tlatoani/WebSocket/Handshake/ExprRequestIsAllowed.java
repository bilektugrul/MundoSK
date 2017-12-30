package com.pie.tlatoani.WebSocket.Handshake;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.pie.tlatoani.Util.MundoUtil;
import com.pie.tlatoani.WebSocket.Events.WebSocketHandshakeEvent;
import org.bukkit.event.Event;

/**
 * Created by Tlatoani on 12/30/17.
 */
public class ExprRequestIsAllowed extends SimpleExpression<Boolean> {
    private boolean allowed;

    @Override
    protected Boolean[] get(Event event) {
        if (event instanceof WebSocketHandshakeEvent.Server) {
            return new Boolean[]{((WebSocketHandshakeEvent.Server) event).allowed == allowed};
        }
        throw new IllegalArgumentException("Illegal class of event: " + event);
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    public String toString(Event event, boolean b) {
        return "websocket handshake request is " + (allowed ? "allowed" : "refused");
    }

    @Override
    public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
        allowed = parseResult.mark == 0;
        if (!MundoUtil.isAssignableFromCurrentEvent(WebSocketHandshakeEvent.Server.class)) {
            Skript.error("The '" + toString(null, false) + "' expression can only be used in the 'on handshake' section of a 'websocket server' template");
            return false;
        }
        return true;
    }

    @Override
    public void change(Event event, Object[] delta, Changer.ChangeMode mode) {
        if (event instanceof WebSocketHandshakeEvent.Server) {
            Boolean value = (Boolean) delta[0];
            ((WebSocketHandshakeEvent.Server) event).allowed = (value == allowed);
        } else {
            throw new IllegalArgumentException("Illegal class of event: " + event);
        }
    }

    @Override
    public Class<?>[] acceptChange(Changer.ChangeMode mode) {
        if (mode == Changer.ChangeMode.SET) return CollectionUtils.array(Boolean.class);
        return null;
    }
}
