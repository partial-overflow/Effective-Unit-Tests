package net.theresnolimits.partialoverflow.effective.unit.tests.item9.c;

import net.theresnolimits.partialoverflow.effective.unit.tests.service.item9.c.ColorPickerDelegate;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ColorPickerDelegateTest {

    private static final String TEST_SOURCE_COLOR = "green";
    private static final String TEST_EXPECTED_HEX_COLOR = "#39ff141";

    private final ColorPickerDelegate colorPickerDelegate = new ColorPickerDelegate();

    @Test
    void pick() {
        String result = colorPickerDelegate.pick(TEST_SOURCE_COLOR);

        assertThat(result, is(TEST_EXPECTED_HEX_COLOR));
    }
}
