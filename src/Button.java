import processing.core.PApplet;

public class Button {
    private PApplet sketch;

    private int horizontalPosition;
    private int verticalPosition;
    private int buttonWidth;
    private int buttonHeight;
    private String label;
    private boolean isEnabled;

    private int normalRGB = 200; // Default gray
    private int hoverRGB = 150; // Default darker gray
    private int disabledRGB = 100; // Default deep gray

    public Button(PApplet sketch, int x, int y, int width, int height, String label) {
        this.sketch = sketch;
        this.horizontalPosition = x;
        this.verticalPosition = y;
        this.buttonWidth = width;
        this.buttonHeight = height;
        this.label = label;
        this.isEnabled = true;
    }

    public void setNormalColor(int r, int g, int b) {
        this.normalRGB = sketch.color(r, g, b);
    }

    public void setHoverColor(int r, int g, int b) {
        this.hoverRGB = sketch.color(r, g, b);
    }

    public void setDisabledColor(int r, int g, int b) {
        this.disabledRGB = sketch.color(r, g, b);
    }

    public void display() {
        if (!isEnabled) {
            sketch.fill(disabledRGB);
        } else if (isMouseHovering()) {
            sketch.fill(hoverRGB);
        } else {
            sketch.fill(normalRGB);
        }

        sketch.stroke(0);
        sketch.strokeWeight(1);
        sketch.rect(horizontalPosition, verticalPosition, buttonWidth, buttonHeight);

        sketch.fill(0, 0, 0); // Black
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.text(label, horizontalPosition + (buttonWidth / 2), verticalPosition + (buttonHeight / 2));
    }

    public boolean isMouseHovering() {
        return sketch.mouseX >= horizontalPosition && sketch.mouseX <= horizontalPosition + buttonWidth &&
               sketch.mouseY >= verticalPosition && sketch.mouseY <= verticalPosition + buttonHeight;
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }
}