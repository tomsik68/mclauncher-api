package sk.tomsik68.mclauncher.impl.servers;

import net.minidev.json.JSONObject;

final class JSONPingedServerInfo47 extends PingedServerInfo {
    private final JSONObject jsonObject;

    JSONPingedServerInfo47(JSONObject jsonObject, String ip, String name, int port) {
        super(ip, name, null, port);
        this.jsonObject = jsonObject;
    }

    @Override
    public int getOnlinePlayers() {
        return Integer.parseInt(((JSONObject) jsonObject.get("players")).get("online").toString());
    }

    @Override
    public int getMaxPlayers() {
        return Integer.parseInt(((JSONObject) jsonObject.get("players")).get("max").toString());
    }

    @Override
    public String getMessage() {
        return ((JSONObject)jsonObject.get("description")).get("text").toString();
    }

    @Override
    public String getVersionId() {
        return ((JSONObject)jsonObject.get("version")).get("name").toString();
    }

    @Override
    public String getIcon() {
        return jsonObject.get("favicon").toString();
    }
}
