package com.birdbi.api;

import java.util.HashMap;
import org.springframework.jdbc.support.JdbcUtils;

public class CamelCaseMap extends HashMap<String, Object> {
    private static final long serialVersionUID = -7826221370286387542L;

    public CamelCaseMap() {
    }

    public Object put(String key, Object value) {
        return super.put(JdbcUtils.convertUnderscoreNameToPropertyName(key), value);
    }
}
