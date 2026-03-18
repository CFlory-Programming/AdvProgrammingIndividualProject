import processing.core.PApplet;

public class StatsScreen {
    private PApplet sketch;
    private Button goHomeButton;
    private int deletedFileCount;
    private long freedStorage;
    
    public StatsScreen(PApplet sketch) {
        this.sketch = sketch;
        this.deletedFileCount = 0;
        this.freedStorage = 0;

        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = (sketch.width / 2) - (buttonWidth / 2);
        int buttonY = sketch.height - 100;
        goHomeButton = new Button(sketch, centerX, buttonY, buttonWidth, buttonHeight, "Go Home");
    }

    public void setStats(int deletedFileCount, long freedStorage) {
        this.deletedFileCount = deletedFileCount;
        this.freedStorage = freedStorage;
    }

    // 1: Go Home button clicked, 0: Not clicked
    public int handleMousePressed() {
        return goHomeButton.isMouseHovering() ? 1 : 0;
    }

    public void display() {
        sketch.background(255);

        sketch.fill(50, 0, 0);
        sketch.textAlign(PApplet.CENTER, PApplet.TOP);
        sketch.textSize(20);
        sketch.text("Delete Summary", sketch.width / 2, 40);

        sketch.textSize(14);
        sketch.fill(0);
        sketch.text("Deleted Files: " + deletedFileCount, sketch.width / 2, 90);
        
        double freedGB = freedStorage / 1024.0 / 1024.0 / 1024.0;
        String freedStorageText = String.format("Freed Storage: %.2f GB", freedGB);
        sketch.text(freedStorageText, sketch.width / 2, 120);

        goHomeButton.display();
    }
}
