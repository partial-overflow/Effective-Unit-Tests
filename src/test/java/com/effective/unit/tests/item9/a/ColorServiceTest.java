package com.effective.unit.tests.service.item9.a;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class ColorServiceTest {

    private static final int TEST_HEIGHT = 2;
    private static final int TEST_WIDTH = 1;
    private static final String TEST_VALID_SOURCE_COLOR = "green";
    private static final String TEST_INVALID_SOURCE_COLOR = "orange";
    private static final String TEST_VALID_HEX_COLOR = "#39ff141";

    private final ColorService colorService = new ColorService();

    @Test
    void drawSuccess() {
        MyShape result = colorService.draw(TEST_HEIGHT, TEST_WIDTH, TEST_VALID_SOURCE_COLOR);

        assertThat(result.getHeight(), is(TEST_HEIGHT));
        assertThat(result.getWidth(), is(TEST_WIDTH));
        assertThat(result.getColor(), is(TEST_VALID_HEX_COLOR));
    }

    @Test
    void drawOrangeShouldBeBlank() {
        MyShape result = colorService.draw(TEST_HEIGHT, TEST_WIDTH, TEST_INVALID_SOURCE_COLOR);

        assertThat(result, is(sameInstance(MyShape.EMPTY)));
    }
}
