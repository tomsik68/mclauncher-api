package sk.tomsik68.mclauncher.impl.login.yggdrasil;

import net.minidev.json.JSONObject;

/**
 * Any response we can get from YggDrassil login system
 */
class YDResponse {
    private String error, message;

    public YDResponse(JSONObject json) {
        if (json.containsKey("error"))
            setError(json.get("error").toString());
        if (json.containsKey("errorMessage"))
            setMessage(json.get("errorMessage").toString());
    }

    final String getError() {
        return error;
    }

    final void setError(String error) {
        this.error = error;
    }

    final String getMessage() {
        return message;
    }

    final void setMessage(String message) {
        this.message = message;
    }
}
