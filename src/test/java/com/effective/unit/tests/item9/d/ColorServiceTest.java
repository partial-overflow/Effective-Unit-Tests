package com.effective.unit.tests.service.item9.d;

import com.effective.unit.tests.service.item9.a.ColorPickerUtils;
import com.effective.unit.tests.service.item9.a.ColorService;
import com.effective.unit.tests.service.item9.a.MyShape;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

public class ColorServiceTest {

    private static final int TEST_HEIGHT = 2;
    private static final int TEST_WIDTH = 1;
    private static final String TEST_SOURCE_COLOR = "marine blue";
    private static final String TEST_HEX_COLOR = "#46dd202";
    private static final String TEST_DEFAULT_HEX_COLOR = "blank";

    private final ColorService colorService = new ColorService();

    @Test
    void drawSuccess() {
        try (MockedStatic<ColorPickerUtils> colorPickerUtils = Mockito.mockStatic(ColorPickerUtils.class)) {
            colorPickerUtils.when(() -> ColorPickerUtils.pick(TEST_SOURCE_COLOR))
                    .thenReturn(TEST_HEX_COLOR);

            MyShape result = colorService.draw(TEST_HEIGHT, TEST_WIDTH, TEST_SOURCE_COLOR);

            assertThat(result.getHeight(), is(TEST_HEIGHT));
            assertThat(result.getWidth(), is(TEST_WIDTH));
            assertThat(result.getColor(), is(TEST_HEX_COLOR));
        }
    }

    @Test
    void drawOrangeShouldBeBlank() {
        try (MockedStatic<ColorPickerUtils> colorPickerUtils = Mockito.mockStatic(ColorPickerUtils.class)) {
            colorPickerUtils.when(() -> ColorPickerUtils.pick(TEST_SOURCE_COLOR))
                    .thenReturn(TEST_DEFAULT_HEX_COLOR);

            MyShape result = colorService.draw(TEST_HEIGHT, TEST_WIDTH, TEST_SOURCE_COLOR);

            assertThat(result, is(sameInstance(MyShape.EMPTY)));
        }
    }
}
