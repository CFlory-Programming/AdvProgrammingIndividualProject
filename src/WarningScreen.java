import processing.core.PApplet;

public class WarningScreen {
    private PApplet sketch;
    private Button agreeButton;

    public WarningScreen(PApplet sketch) {
        this.sketch = sketch;

        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = (sketch.width / 2) - (buttonWidth / 2);
        int buttonY = sketch.height - 100;
        this.agreeButton = new Button(sketch, centerX, buttonY, buttonWidth, buttonHeight, "AGREE");
        agreeButton.setNormalColor(0, 200, 100); // Green
        agreeButton.setHoverColor(0, 255, 0); // Lighter green
    }

    public void display() {
        sketch.background(255);

        sketch.fill(255, 204, 0); // Yellow text
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.textSize(24);

        int sideMargin = 40;
        int topMargin = 50;
        int textBoxWidth = sketch.width - (sideMargin * 2); // 40 px on each side margin
        int textboxHeight = sketch.height - (topMargin * 2); // 50 px on top and bottom margin

        String warningMessage = "WARNING: USE THIS PROGRAM WITH CAUTION AS IT HAS THE ABILITY TO DELETE ANY FILE ON YOUR COMPUTER!";

        sketch.text(warningMessage, sideMargin, topMargin, textBoxWidth, textboxHeight);

        agreeButton.display();
    }
    
    public boolean handleMousePressed() {
        // Return true when the agree button is clicked (HAHAHAHAHA)
        return agreeButton.isMouseHovering();
    }
}