package sk.tomsik68.mclauncher.api.json;

import net.minidev.json.JSONObject;

/**
 * Anything that can be converted to a JSONObject implements this!
 *
 * @author Tomsik68
 */
public interface IJSONSerializable {
    /**
     * @return JSONObject which represents this object.
     */
    public JSONObject toJSON();
}
