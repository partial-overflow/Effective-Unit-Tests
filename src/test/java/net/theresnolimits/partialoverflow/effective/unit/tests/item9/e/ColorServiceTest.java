package net.theresnolimits.partialoverflow.effective.unit.tests.item9.e;

import net.theresnolimits.partialoverflow.effective.unit.tests.service.item9.e.ColorPickerDelegate;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.item9.e.ColorService;
import net.theresnolimits.partialoverflow.effective.unit.tests.service.item9.e.MyShape;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ColorServiceTest {

    private static final int TEST_HEIGHT = 2;
    private static final int TEST_WIDTH = 1;
    private static final String TEST_SOURCE_COLOR = "marine blue";
    private static final String TEST_HEX_COLOR = "#46dd202";
    private static final String TEST_DEFAULT_HEX_COLOR = "blank";

    @InjectMocks
    private final ColorService colorService = new ColorService();

    @Mock
    private ColorPickerDelegate colorPickerDelegate;

    @Test
    void drawSuccess() {
        given(colorPickerDelegate.pick(TEST_SOURCE_COLOR)).willReturn(TEST_HEX_COLOR);

        MyShape result = colorService.draw(TEST_HEIGHT, TEST_WIDTH, TEST_SOURCE_COLOR);

        assertThat(result.getHeight(), is(TEST_HEIGHT));
        assertThat(result.getWidth(), is(TEST_WIDTH));
        assertThat(result.getColor(), is(TEST_HEX_COLOR));
    }

    @Test
    void drawOrangeShouldBeBlank() {
        given(colorPickerDelegate.pick(TEST_SOURCE_COLOR)).willReturn(TEST_DEFAULT_HEX_COLOR);

        MyShape result = colorService.draw(TEST_HEIGHT, TEST_WIDTH, TEST_SOURCE_COLOR);

        assertThat(result, is(sameInstance(MyShape.EMPTY)));
    }
}
