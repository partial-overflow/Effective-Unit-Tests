package net.theresnolimits.partialoverflow.effective.unit.tests.service.item9.e;

import net.theresnolimits.partialoverflow.effective.unit.tests.service.item9.e.ColorPickerDelegate;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.item9.e.ColorPickerUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
public class ColorPickerDelegateTest {

    private static final String TEST_SOURCE_COLOR = "marine blue";
    private static final String TEST_HEX_COLOR = "#46dd202";

    @InjectMocks
    private ColorPickerDelegate colorPickerDelegate;

    @Test
    void pick() {
        try (MockedStatic<ColorPickerUtils> colorPickerUtils = Mockito.mockStatic(ColorPickerUtils.class)) {
            colorPickerUtils.when(() -> ColorPickerUtils.pick(TEST_SOURCE_COLOR))
                    .thenReturn(TEST_HEX_COLOR);

            String result = colorPickerDelegate.pick(TEST_SOURCE_COLOR);

            assertThat(result, is(TEST_HEX_COLOR));
        }
    }
}
