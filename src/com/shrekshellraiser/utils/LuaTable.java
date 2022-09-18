package com.shrekshellraiser.utils;

import java.util.HashMap;
import java.util.Map;

public class LuaTable {
    private final Map<Object, Object> table = new HashMap<>();
    private int lastKey = 0;
    private int lastKeySerialize = 0;

    public <KT, VT> void put(KT key, VT value) {
        table.put(key, value);
    }

    public <VT> void put(VT value) {
        table.put(++lastKey, value);
    }

    public <KT> Object get(KT key) {
        ;
        return table.get(key);
    }

    private String getLuaRep(Object obj, boolean isKey) {
        if (obj instanceof String) {
            return "\"" + obj + "\"";
        } else if (obj instanceof Integer o) {
            if (o == lastKeySerialize + 1 && isKey) {
                lastKeySerialize++;
                return null;
            }
            return o.toString();
        }
        return obj.toString();
    }

    public String toString() {
        // Iterate over each element in the hash map, building a string
        StringBuilder str = new StringBuilder();
        str.append("{");
        lastKeySerialize = 0;
        for (Map.Entry<Object, Object> entry : table.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            String keyStr = getLuaRep(key, true);
            if (keyStr != null) {
                str.append("[").append(keyStr).append("]=");
            }
            str.append(getLuaRep(value, false)).append(",");
        }
        str.append("}");
        return str.toString();
    }


}
