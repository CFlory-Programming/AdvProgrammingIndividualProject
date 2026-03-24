import processing.core.PApplet;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultsScreen {
    private PApplet sketch;
    private Button deleteButton;
    private Button confirmYesButton;
    private Button confirmNoButton;
    private boolean awaitingDeleteConfirmation;

    private Map<String, List<FileScanner.FileInfo>> duplicateFiles;
    private float scrollOffset = 0;
    private float scrollSpeed = 15;
    private int iconSize = 30;

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

    public void setDuplicateFiles(Map<String, List<FileScanner.FileInfo>> duplicateFiles) {
        this.duplicateFiles = duplicateFiles;
        this.scrollOffset = 0; // Reset scroll position when new files are loaded (top of the page)
    }

    public void handleMouseWheel(float direction) {
        // Positive direction means scrolling down, negative means scrolling up
        scrollOffset += direction * scrollSpeed;
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(timestamp));
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
        if (duplicateFiles != null) {
            sketch.fill(0, 0, 0);
            sketch.textSize(11);
            sketch.textAlign(PApplet.CENTER, PApplet.TOP);

            // Define scrollable area
            int scrollAreaTop = 40;
            int scrollAreaBottom = sketch.height - 140;
            int scrollAreaHeight = scrollAreaBottom - scrollAreaTop;

            // Clip the drawing to the scrollable area
            sketch.clip(sideMargin, scrollAreaTop, textBoxWidth, scrollAreaHeight);

            float yPosition = scrollAreaTop - scrollOffset;

            // Display the duplicates section
            sketch.fill(0, 0, 0);
            sketch.textAlign(PApplet.LEFT, PApplet.TOP);
            sketch.textSize(11);
            sketch.text("Duplicate Files:", sideMargin, yPosition);
            yPosition += 22;

            // Display duplicates in groups
            int groupNumber = 1;
            float totalContentHeight = 0f;
            for (List<FileScanner.FileInfo> group : duplicateFiles.values()) {
                if (group.size() > 1) {
                    sketch.fill(0, 0, 0);
                    sketch.textAlign(PApplet.LEFT, PApplet.TOP);
                    sketch.textSize(10);
                    sketch.text("Group " + groupNumber + ":", sideMargin, yPosition);
                    yPosition += 22;
                    totalContentHeight += 22;

                    for (FileScanner.FileInfo fileInfo : group) {
                        // Draw rounded square file icon
                        sketch.fill(240, 240, 240);
                        sketch.stroke(200);
                        sketch.strokeWeight(1);
                        sketch.rect(sideMargin, yPosition, iconSize, iconSize, 5);

                        processing.core.PImage icon = sketch.loadImage("images/IconImage.png");
                        if (icon != null) {
                            sketch.image(icon, sideMargin, yPosition, iconSize, iconSize);
                        }

                        // Display file name underneath the icon
                        sketch.noStroke();
                        sketch.textAlign(PApplet.LEFT, PApplet.TOP);
                        sketch.textSize(10);
                        sketch.fill(0, 0, 0);
                        sketch.text(fileInfo.file.getName(), sideMargin + iconSize + 10, yPosition + 2);

                        sketch.textSize(8);
                        sketch.fill(100, 100, 100);
                        sketch.text("Reason: " + fileInfo.duplicateReason, sideMargin + iconSize + 10, yPosition + 16);
                        sketch.text("Modified: " + formatDate(fileInfo.lastModified), sideMargin + iconSize + 10, yPosition + 30);
                        sketch.text("Action: " + fileInfo.actionLabel, sideMargin + iconSize + 10, yPosition + 44);

                        yPosition += iconSize + 26; // Move down for the next file in the group
                        totalContentHeight += iconSize + 26;
                    }

                    yPosition += 10; // Extra space between groups
                    totalContentHeight += 10;
                    groupNumber++;
                }
            }

        // Display the non-duplicates section
        sketch.fill(0, 0, 0);
        sketch.textAlign(PApplet.LEFT, PApplet.TOP);
        sketch.textSize(11);
        sketch.text("Non-Duplicate Files:", sideMargin, yPosition);
        yPosition += 22;

        for (List<FileScanner.FileInfo> group : duplicateFiles.values()) {
            if (group.size() == 1) {
                FileScanner.FileInfo fileInfo = group.get(0);

                    // Draw rounded square file icon
                    sketch.fill(240, 240, 240);
                    sketch.stroke(200);
                    sketch.strokeWeight(1);
                    sketch.rect(sideMargin, yPosition, iconSize, iconSize, 5);

                    processing.core.PImage icon = sketch.loadImage("images/IconImage.png");
                    if (icon != null) {
                        sketch.image(icon, sideMargin, yPosition, iconSize, iconSize);
                    }

                    // Display file name underneath the icon
                    sketch.noStroke();
                    sketch.textAlign(PApplet.LEFT, PApplet.TOP);
                    sketch.textSize(10);
                    sketch.fill(0, 0, 0);
                    sketch.text(fileInfo.file.getName(), sideMargin + iconSize + 10, yPosition + 2);

                    sketch.textSize(8);
                    sketch.fill(100, 100, 100);
                    sketch.text("Modified: " + formatDate(fileInfo.lastModified), sideMargin + iconSize + 10, yPosition + 16);
                    sketch.text("Action: " + fileInfo.actionLabel, sideMargin + iconSize + 10, yPosition + 30);

                    yPosition = yPosition + iconSize + 26; // Move down for the next file
                    totalContentHeight += iconSize + 26;
                }
            }

            // Disable clipping after drawing the scrollable content
            sketch.noClip();

            // Constrain scroll offset based on real content height
            scrollOffset = PApplet.constrain(scrollOffset, 0, Math.max(0f, totalContentHeight - scrollAreaHeight + 40f));
        }

        if (awaitingDeleteConfirmation) {
            // Draw slightly not transparent black overlay
            sketch.fill(0, 0, 0, 150); // 150 is the alpha value for transparency (magic)
            sketch.rect(0, 0, sketch.width, sketch.height);

            sketch.fill(255);
            sketch.textSize(18);
            sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
            sketch.text("Are you sure you want to delete the duplicate files?", sketch.width / 2, sketch.height / 2 - 40);
            sketch.textSize(14);
            sketch.fill(200, 200, 200);
            sketch.text("(The least recently modified files in each duplicate group will be deleted)", sketch.width / 2, sketch.height / 2 - 10);

            confirmYesButton.display();
            confirmNoButton.display();
        }
    }
}