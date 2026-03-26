import processing.core.PApplet;
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

    private Map<String, List<FileInfo>> duplicateFiles;
    private float scrollOffset = 0;
    private float scrollSpeed = 15;
    private int iconSize = 30;
    private float scrollSize = 35;

    // Visual settings
    private int textColor = 0;
    private int titleColor = 50;
    private int subtextColor = 100;
    private float sectionSpacing = 10f;
    private float groupSpacing = 10f;

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

    public void setDuplicateFiles(Map<String, List<FileInfo>> duplicateFiles) {
        this.duplicateFiles = duplicateFiles;
        this.scrollOffset = 0; // Reset scroll position when new files are loaded (top of the page)
    }

    public void handleMouseWheel(float direction) {
        if (duplicateFiles == null) {
            return;
        }

        int scrollAreaTop = 40;
        int scrollAreaBottom = sketch.height - 140;
        float scrollAreaHeight = scrollAreaBottom - scrollAreaTop;

        float totalContentHeight = calculateTotalContentHeight();
        float maxOffset = PApplet.max(0f, totalContentHeight - scrollAreaHeight);

        // TOP guard: When exactly at top, block UP scrolling
        if (direction < 0 && scrollOffset <= 0f) {
            return;
        }

        // BOTTOM guard: When exactly at bottom, block DOWN scrolling
        if (direction > 0 && scrollOffset >= maxOffset) {
            return;
        }

        scrollOffset += direction * scrollSpeed;
        scrollOffset = PApplet.constrain(scrollOffset, 0f, maxOffset);
    }

    private float calculateTotalContentHeight() {
        if (duplicateFiles == null) {
            return 0f;
        }

        float totalHeight = 0f;

        // Duplicate section header
        totalHeight += scrollSize;

        for (List<FileInfo> group : duplicateFiles.values()) {
            if (group.size() > 1) {
                totalHeight += scrollSize; // Group header
                totalHeight += group.size() * (iconSize + 26);
                totalHeight += groupSpacing;
            }
        }

        // Thick divider before the non-duplicate section
        totalHeight += sectionSpacing;

        // Non-duplicate section header
        totalHeight += scrollSize;

        for (List<FileInfo> group : duplicateFiles.values()) {
            if (group.size() == 1) {
                totalHeight += iconSize + 26;
            }
        }
        return totalHeight;
    }

    private int getDuplicateGroupCount() {
        if (duplicateFiles == null) {
            return 0;
        }
        int count = 0;
        for (List<FileInfo> group : duplicateFiles.values()) {
            if (group.size() > 1) {
                count++;
            }
        }
        return count;
    }

    private int getUniqueFileCount() {
        if (duplicateFiles == null) {
            return 0;
        }
        int count = 0;
        for (List<FileInfo> group : duplicateFiles.values()) {
            if (group.size() == 1) {
                count++;
            }
        }
        return count;
    }

    private float drawWrappedText(String text, float x, float y, float maxWidth, float lineHeight) {
        String[] words = text.split(" ");
        String line = "";

        for (String word : words) {
            if (sketch.textWidth(word) > maxWidth && word.length() > 1) {
                if (!line.isEmpty()) {
                    sketch.text(line, x, y);
                    y+= lineHeight;
                    line = "";
                }
                String remaining = word;
                while (!remaining.isEmpty()) {
                    int length = 1;
                    while (length <= remaining.length() && sketch.textWidth(remaining.substring(0, length)) <= maxWidth) {
                        length++;
                    }
                    length = Math.max(1, length - 1);
                    sketch.text(remaining.substring(0, length), x, y);
                    y += lineHeight;
                    remaining = remaining.substring(length);
                }
                continue;
            }

            String testLine = line.isEmpty() ? word : line + " " + word;
            float testLineWidth = sketch.textWidth(testLine);

            if (testLineWidth <= maxWidth) {
                line = testLine;
            } else {
                if (!line.isEmpty()) {
                    sketch.text(line, x, y);
                    y += lineHeight;
                }
                line = word;
            }
        }

        if (!line.isEmpty()) {
            sketch.text(line, x, y);
            y += lineHeight;
        }

        return y;
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

        // Display summary right after Results title
        int duplicateGroupCount = getDuplicateGroupCount();
        int uniqueFileCount = getUniqueFileCount();
        sketch.fill(textColor);
        sketch.textAlign(PApplet.LEFT, PApplet.TOP);
        sketch.textSize(10);
        sketch.text("Showing " + duplicateGroupCount + " duplicate groups and " + uniqueFileCount + " unique files", sideMargin, 30);

        // Show the results if duplicate files
        if (duplicateFiles != null) {
            sketch.fill(0, 0, 0);
            sketch.textSize(11);
            sketch.textAlign(PApplet.CENTER, PApplet.TOP);

            // Define scrollable area
            int scrollAreaTop = 60;
            int scrollAreaBottom = sketch.height - 140;
            int scrollAreaHeight = scrollAreaBottom - scrollAreaTop;

            // Clip the drawing to the scrollable area
            sketch.clip(sideMargin, scrollAreaTop, textBoxWidth, scrollAreaHeight);

            float yPosition = scrollAreaTop - scrollOffset;

            // Display the duplicates section
            sketch.fill(titleColor);
            sketch.textAlign(PApplet.LEFT, PApplet.TOP);
            sketch.textSize(11);
            sketch.text("Duplicate Files:", sideMargin, yPosition);
            yPosition += 22;

            boolean hasDuplicateGroups = duplicateGroupCount > 0;
            for (List<FileInfo> group : duplicateFiles.values()) {
                if (group.size() > 1) {
                    hasDuplicateGroups = true;
                    break;
                }
            }

            int groupNumber = 1;

            if (!hasDuplicateGroups) {
                sketch.textAlign(PApplet.CENTER, PApplet.TOP);
                sketch.textSize(12);
                sketch.text("No duplicate files found in the selected folder.", sketch.width / 2, yPosition);
                yPosition += 30;
                sketch.textAlign(PApplet.LEFT, PApplet.TOP);
                sketch.textSize(11);
            } else {
                for (List<FileInfo> group : duplicateFiles.values()) {
                    if (group.size() > 1) {
                        sketch.fill(0, 0, 0);
                        sketch.textAlign(PApplet.LEFT, PApplet.TOP);
                        sketch.textSize(10);
                        sketch.text("Group " + groupNumber + ":", sideMargin, yPosition);
                        yPosition += 22;

                        for (FileInfo fileInfo : group) {
                            // Draw rounded square file icon
                            sketch.fill(240, 240, 240);
                            sketch.stroke(200);
                            sketch.strokeWeight(1);
                            sketch.rect(sideMargin, yPosition, iconSize, iconSize, 5);

                            processing.core.PImage icon = sketch.loadImage("../images/IconImage.png");
                            if (icon != null) {
                                sketch.image(icon, sideMargin, yPosition, iconSize, iconSize);
                            }

                            // Display file name and metadata next to the icon
                            sketch.noStroke();
                            sketch.textAlign(PApplet.LEFT, PApplet.TOP);
                            sketch.textSize(10);
                            sketch.fill(textColor);
                            float fileNameStartX = sideMargin + iconSize + 10;
                            float fileNameMaxWidth = textBoxWidth - (fileNameStartX - sideMargin);
                            float wrappedTextY = drawWrappedText(fileInfo.file.getName(), fileNameStartX, yPosition + 2, fileNameMaxWidth, 12);

                            sketch.textSize(8);
                            sketch.fill(subtextColor);
                            sketch.text("Reason: " + fileInfo.duplicateReason, fileNameStartX, wrappedTextY);
                            sketch.text("Modified: " + formatDate(fileInfo.lastModified), fileNameStartX, wrappedTextY + 14);
                            sketch.text("Action: " + fileInfo.actionLabel, fileNameStartX, wrappedTextY + 28);

                            float usedHeight = (wrappedTextY - yPosition) + 28; // Calculate the height used by the wrapped text
                            float blankHeight = Math.max(iconSize + 26, usedHeight + 15); // Ensure enough space for the icon and metadata
                            yPosition += blankHeight; // Move down for the next file
                            continue;
                        }

                        // Visual seperator line between duplicate groups
                        sketch.stroke(180);
                        sketch.line(sideMargin, yPosition, sideMargin + textBoxWidth, yPosition);
                        sketch.noStroke();

                        yPosition += 10; // Extra space between groups
                        groupNumber++;
                    }
                }
            }

            // Thick divider before non-duplicate section
            sketch.stroke(180);
            sketch.strokeWeight(3);
            sketch.line(sideMargin, yPosition, sideMargin + textBoxWidth, yPosition);
            sketch.strokeWeight(1);
            sketch.noStroke();
            yPosition += sectionSpacing; // Space after the divider

            // Display the non-duplicates section
            sketch.fill(0, 0, 0);
            sketch.textAlign(PApplet.LEFT, PApplet.TOP);
            sketch.textSize(11);
            sketch.text("Non-Duplicate Files:", sideMargin, yPosition);
            yPosition += 22;

            for (List<FileInfo> group : duplicateFiles.values()) {
                if (group.size() == 1) {
                    FileInfo fileInfo = group.get(0);

                    // Draw rounded square file icon
                    sketch.fill(240, 240, 240);
                    sketch.stroke(200);
                    sketch.strokeWeight(1);
                    sketch.rect(sideMargin, yPosition, iconSize, iconSize, 5);

                    processing.core.PImage icon = sketch.loadImage("../images/IconImage.png");
                    if (icon != null) {
                        sketch.image(icon, sideMargin, yPosition, iconSize, iconSize);
                    }

                    // Display file name and metadata underneath the icon with wrap check
                    sketch.noStroke();
                    sketch.textAlign(PApplet.LEFT, PApplet.TOP);
                    sketch.textSize(10);
                    sketch.fill(textColor);
                    float fileNameStartX = sideMargin + iconSize + 10;
                    float fileNameMaxWidth = textBoxWidth - (fileNameStartX - sideMargin);
                    float wrappedTextY = drawWrappedText(fileInfo.file.getName(), fileNameStartX, yPosition + 2, fileNameMaxWidth, 12);

                    sketch.textSize(8);
                    sketch.fill(subtextColor);
                    sketch.text("Modified: " + formatDate(fileInfo.lastModified), fileNameStartX, wrappedTextY);
                    sketch.text("Action: " + fileInfo.actionLabel, fileNameStartX, wrappedTextY + 14);

                    float usedHeight = (wrappedTextY - yPosition) + 14; // Calculate the height used by the wrapped text
                    float usedEntryHeight = Math.max(iconSize + 26, usedHeight + 8); // Ensure enough space for the icon and metadata

                    yPosition += usedEntryHeight; // Move down for the next file
                }
            }

            // Disable clipping after drawing the scrollable content
            sketch.noClip();

            // Constrain scroll offset based on real content height
            float expectedContentHeight = calculateTotalContentHeight();
            scrollOffset = PApplet.constrain(scrollOffset, 0f, PApplet.max(0f, expectedContentHeight - scrollAreaHeight));
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
