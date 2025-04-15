package com.effective.unit.tests.service.item9.b;

import java.util.HashMap;
import java.util.Map;

public class ColorPickerUtils {
    public static final String BLANK_VALUE = "blank";

    private static final Map<String, String> COLORS = new HashMap<>();
    static {
        COLORS.put("red", "#9f1111");
        COLORS.put("green", "#39ff141");
        COLORS.put("blue", "#0000ff");
        COLORS.put("purple", "#0000ff");
    }

    public String pick(String colorName) {
        return COLORS.getOrDefault(colorName, BLANK_VALUE);
    }
}
