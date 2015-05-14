package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;
import sk.tomsik68.mclauncher.api.json.IJSONSerializable;
import sk.tomsik68.mclauncher.api.login.ISession;

import java.util.UUID;

final class YDLogoutRequest implements IJSONSerializable {
    private final ISession session;
    private final UUID clientToken;

    public YDLogoutRequest(ISession session, UUID clientToken) {
        this.session = session;
        this.clientToken = clientToken;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("accessToken", session.getSessionID());
        jsonObj.put("clientToken", clientToken.toString());
        return jsonObj;
    }

}
