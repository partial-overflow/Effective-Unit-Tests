package net.theresnolimits.partialoverflow.effective.unit.tests.service.item9.c;

public class MyShape {
    public static final MyShape EMPTY = new MyShape(0, 0, "none");

    private final int height;
    private final int width;
    private final String color;

    public MyShape(int height, int width, String color) {
        this.height = height;
        this.width = width;
        this.color = color;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getColor() {
        return color;
    }
}
