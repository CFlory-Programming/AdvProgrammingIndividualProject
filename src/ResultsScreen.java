import processing.core.PApplet;
import java.io.File;
import java.util.List;
import java.util.Map;

public class ResultsScreen {
    private PApplet sketch;
    private Button deleteButton;
    private Button confirmYesButton;
    private Button confirmNoButton;
    private boolean awaitingDeleteConfirmation;

    private Map<String, List<File>> duplicateFiles;

    public ResultsScreen(PApplet sketch, Button deleteButton) {
        this.sketch = sketch;
        this.deleteButton = deleteButton;
        this.awaitingDeleteConfirmation = false;
        this.duplicateFiles = null;

        int buttonWidth = 120;
        int buttonHeight = 40;
        int centerX = (sketch.width / 2) - (buttonWidth / 2);

        confirmYesButton = new Button(sketch, centerX - 80, sketch.height - 120, buttonWidth, buttonHeight, "YES");
        confirmYesButton.setNormalColor(0, 200, 0);
        confirmYesButton.setHoverColor(0, 255, 0);

        confirmNoButton = new Button(sketch, centerX + 80, sketch.height - 120, buttonWidth, buttonHeight, "NO");
        confirmNoButton.setNormalColor(200, 0, 0);
        confirmNoButton.setHoverColor(255, 0, 0);
    }
    
    public void drawDeleteConfirmation() {
        awaitingDeleteConfirmation = true;
    }

    // Return 1 for YES, -1 for NO, and 0 for no button clicked
    public int handleMousePressed() {
        if (!awaitingDeleteConfirmation) {
            return 0;
        }

        if (confirmYesButton.isMouseHovering()) {
            awaitingDeleteConfirmation = false;
            return 1;
        }

        if (confirmNoButton.isMouseHovering()) {
            awaitingDeleteConfirmation = false;
            return -1;
        }

        return 0;
    }

    public void setDuplicateFiles(Map<String, List<File>> duplicateFiles) {
        this.duplicateFiles = duplicateFiles;
    }

    public void display() {
        sketch.background(255);

        deleteButton.display();

        int sideMargin = 25;
        int topMargin = 50;
        int textBoxWidth = sketch.width - (sideMargin * 2);
        int textBoxHeight = sketch.height - (topMargin * 2);

        sketch.fill(50, 0, 0);
        sketch.textAlign(PApplet.CENTER, PApplet.TOP);
        sketch.textSize(14);

        String displayMessage = "Results";
        sketch.text(displayMessage, sideMargin, 10, textBoxWidth, textBoxHeight);

        // Show the results if duplicate files
        int yPosition = 40;
        if (duplicateFiles != null) {
            sketch.fill(0, 0, 0);
            sketch.textSize(11);
            sketch.textAlign(PApplet.CENTER, PApplet.TOP);

            int iconSize = 30;
            int spacing = 10;

            // Display duplicate files
            sketch.text("Duplicate Files:", sideMargin, yPosition, textBoxWidth, textBoxHeight);

            // Display duplicates in groups
            int groupNumber = 1;
            for (List<File> group: duplicateFiles.values()) {
                if (group.size() > 1) {
                    sketch.text("Group " + groupNumber + ":", sideMargin, yPosition);
                    yPosition += 20;

                    int xPosition = sideMargin;
                    for (File file: group) {
                        // Draw rounded square file icon
                        sketch.fill(240, 240, 240);
                        sketch.stroke(200);
                        sketch.strokeWeight(1);
                        sketch.rect(xPosition, yPosition, iconSize, iconSize, 5);

                        processing.core.PImage icon = sketch.loadImage("images/IconImage.png");
                        if (icon != null) {
                            sketch.image(icon, xPosition, yPosition, iconSize, iconSize);
                        }

                        // Display file name underneath the icon
                        sketch.noStroke();
                        sketch.textAlign(PApplet.CENTER, PApplet.TOP);
                        sketch.textSize(9);
                        sketch.fill(0, 0, 0);
                        sketch.text(file.getName(), xPosition + iconSize / 2, yPosition + iconSize + 2);

                        xPosition += iconSize + spacing + 5; // Move to the right for the next file icon
                    }

                    yPosition += iconSize + spacing + 25; // Move down for the next group
                    groupNumber++;
                }
            }

            // Display non-duplicates
            sketch.text("Non-Duplicate Files:", sideMargin, yPosition + 100, textBoxWidth, textBoxHeight);
            yPosition += 20;

            int xPosition = sideMargin;
            for (List<File> group: duplicateFiles.values()) {
                if (group.size() == 1) {
                    for (File file: group) {
                        // Draw rounded square file icon
                        sketch.fill(240, 240, 240);
                        sketch.stroke(200);
                        sketch.strokeWeight(1);
                        sketch.rect(xPosition, yPosition, iconSize, iconSize, 5);

                        processing.core.PImage icon = sketch.loadImage("images/IconImage.png");
                        if (icon != null) {
                            sketch.image(icon, xPosition, yPosition, iconSize, iconSize);
                        }

                        // Display file name underneath the icon
                        sketch.noStroke();
                        sketch.textAlign(PApplet.CENTER, PApplet.TOP);
                        sketch.textSize(9);
                        sketch.fill(0, 0, 0);
                        sketch.text(file.getName(), xPosition + iconSize / 2, yPosition + iconSize + 2);

                        xPosition += iconSize + spacing + 5; // Move to the right for the next file icon
                    }
                }
            }
        }

        if (awaitingDeleteConfirmation) {
            // Draw slightly not transparent black overlay
            sketch.fill(0, 0, 0, 150); // 150 is the alpha value for transparency (magic)
            sketch.rect(0, 0, sketch.width, sketch.height);

            sketch.fill(255);
            sketch.textSize(18);
            sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
            sketch.text("Are you sure you want to delete the duplicate files?", sketch.width / 2, sketch.height / 2 - 40);

            confirmYesButton.display();
            confirmNoButton.display();
        }
    }
}