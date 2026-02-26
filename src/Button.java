import processing.core.PApplet;

public class Button {
    private PApplet sketch;

    private int horizontalPosition;
    private int verticalPosition;
    private int buttonWidth;
    private int buttonHeight;
    private String label;

    public Button(PApplet sketch, int x, int y, int width, int height, String label) {
        this.sketch = sketch;
        this.horizontalPosition = x;
        this.verticalPosition = y;
        this.buttonWidth = width;
        this.buttonHeight = height;
        this.label = label;
    }

    public void display() {
        if (isMouseHovering()) {
            sketch.fill(200, 255, 255); // Light blue color for when hovering over the button
        } else {
            sketch.fill(255, 255, 255);
        }

        sketch.rect(horizontalPosition, verticalPosition, buttonWidth, buttonHeight);

        sketch.fill(0, 0, 0); // Black
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.text(label, horizontalPosition + (buttonWidth / 2), verticalPosition + (buttonHeight / 2));
    }

    public boolean isMouseHovering() {
        return sketch.mouseX > horizontalPosition && sketch.mouseX < horizontalPosition + buttonWidth && sketch.mouseY > verticalPosition && sketch.mouseY < verticalPosition + buttonHeight; // Check that mouse position is within bounds of button
    }
}