package net.theresnolimits.partialoverflow.effective.unit.tests.service.item9.e;


import static net.theresnolimits.partialoverflow.effective.unit.tests.service.item9.e.ColorPickerUtils.BLANK_VALUE;

public class ColorService {

    ColorPickerDelegate colorPickerDelegate;

    public MyShape draw(int height, int width, String color) {
        return drawRectangle(height, width, colorPickerDelegate.pick(color));
    }

    private MyShape drawRectangle(int height, int width, String color) {
        MyShape myShape;
        if(color.equals(BLANK_VALUE)) {
            myShape = MyShape.EMPTY;
        } else {
            myShape = new MyShape(height, width, color);
        }
        return myShape;
    }
}
