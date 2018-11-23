package com.ceru.framework.boss.util;

import java.util.regex.Pattern;

public enum Regex {

    SUPER_RESTORE(Pattern.compile("^Super restore( flask)? \\([1-6]\\)$")),
    SHIELD(Pattern.compile(".*([Ww]ard|[Ss]hield|[Dd]ark bow).*")),
    MELEE_WEAPONS(Pattern.compile(".*([Ss]cythe|[Ll]ance|[Aa]ttuned).*"));

    private final Pattern pattern;

    Regex(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern get() {
        return pattern;
    }
}
