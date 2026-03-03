package model;

import org.junit.platform.commons.util.StringUtils;

public record AuthData(String username, String authToken) {
    public boolean validate() {
        return username != null && authToken != null;
    }
}
