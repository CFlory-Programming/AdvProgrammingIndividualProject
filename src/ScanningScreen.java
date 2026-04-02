import processing.core.PApplet;
import java.util.List;
import java.util.Map;

public class ScanningScreen {
    private PApplet sketch;
    private Button seeResultsButton;
    private ProgressBar progressBar;
    private String selectedFolderPath;
    private Map<String, List<FileInfo>> duplicateFiles;
    private float scrollOffset = 0;
    private float scrollSpeed = 15;
    private float lineHeight = 18f;
    private float entryHeight = 22f;

    public ScanningScreen(PApplet sketch, Button seeResultsButton, ProgressBar progressBar) {
        this.sketch = sketch;
        this.seeResultsButton = seeResultsButton;
        this.progressBar = progressBar;
        this.selectedFolderPath = "No folder selected";
        this.duplicateFiles = null;
    }

    // Reset this in Main under function GoHome()
    public void setSelectedFolderPath(String path) {
        this.selectedFolderPath = path;
    }

    public void setDuplicateFiles(Map<String, List<FileInfo>> duplicateFiles) {
        this.duplicateFiles = duplicateFiles;
        this.scrollOffset = 0; // Reset scroll position when new files are loaded
    }

    private float calculateTotalContentHeight() {
        if (duplicateFiles == null) {
            return 0f;
        }

        float totalContentHeight = 0f;
        float fileNameMaxWidth = sketch.width - 110;

        for (List<FileInfo> group : duplicateFiles.values()) {
            for (FileInfo fileInfo : group) {
                float estimatedHeight = estimateWrappedTextHeight(fileInfo.file.getName(), fileNameMaxWidth, lineHeight);
                totalContentHeight += Math.max(entryHeight, estimatedHeight);
            }
        }
        return totalContentHeight;
    }

    public void handleMouseWheel(float direction) {
        if (duplicateFiles == null) {
            return;
        }

        int listGap = 120;
        int scrollAreaTop = listGap + 15;
        int scrollAreaBottom = sketch.height - 180;
        float scrollAreaHeight = scrollAreaBottom - scrollAreaTop;

        float totalContentHeight = calculateTotalContentHeight();
        float maxOffset = PApplet.max(0f, totalContentHeight - scrollAreaHeight + 10f);

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

    private float estimateWrappedTextHeight(String text, float maxWidth, float lineHeight) {
        if (text == null || text.isEmpty()) {
            return lineHeight;
        }

        String[] words = text.split(" ");
        String line = "";
        float height = 0f;

        for (String word : words) {
            if (sketch.textWidth(word) > maxWidth && word.length() > 1) {
                // Count how many chunks the long word needs
                String remaining = word;
                while (!remaining.isEmpty()) {
                    int length = 1;
                    while (length <= remaining.length() && sketch.textWidth(remaining.substring(0, length)) <= maxWidth) {
                        length++;
                    }
                    length = Math.max(1, length - 1);
                    height += lineHeight;
                    remaining = remaining.substring(length);
                }
                line = "";
                continue;
            }

            String testLine = line.isEmpty() ? word : line + " " + word;
            if (sketch.textWidth(testLine) <= maxWidth) {
                line = testLine;
            } else {
                height += lineHeight;
                line = word;
            }
        }

        if (!line.isEmpty() || height == 0) {
            height += lineHeight; // Last line
        }

        return height;
    }

    public void display() {
        sketch.background(255);

        // Enable/disable the see results button based on progress bar completion
        seeResultsButton.setEnabled(progressBar.isComplete());
        seeResultsButton.display();

        int sideMargin = 25;
        int topMargin = 50;
        int textBoxWidth = sketch.width - (sideMargin * 2);

        sketch.fill(50, 0, 0);
        sketch.textAlign(PApplet.CENTER, PApplet.TOP);
        sketch.textSize(14);

        String displayMessage = "Scanning Folder:\n" + selectedFolderPath;
        sketch.text(displayMessage, sideMargin, topMargin, textBoxWidth, 100);

        // Show all files in the directory that will be scanned
        if (duplicateFiles != null) {
            sketch.fill(0, 0, 0);
            sketch.textSize(11);
            sketch.textAlign(PApplet.LEFT, PApplet.TOP);

            int listGap = 120;
            sketch.text("Scanning Files:", sideMargin, listGap);
            listGap += 15;

            // Define scrollable area
            int scrollAreaTop = listGap;
            int scrollAreaBottom = sketch.height - 180;
            int scrollAreaHeight = scrollAreaBottom - scrollAreaTop;

            // Clip the drawing to the scrollable area
            sketch.clip(sideMargin, scrollAreaTop, textBoxWidth, scrollAreaHeight);

            float yPosition = scrollAreaTop - scrollOffset;
            float fileNameStartX = sideMargin + 20;
            float fileNameMaxWidth = textBoxWidth - 50;

             for (List<FileInfo> group : duplicateFiles.values()) {
                for (FileInfo fileInfo : group) {
                    String prefix = "  - ";
                    String fileName = fileInfo.file.getName();

                    sketch.text(prefix, fileNameStartX, yPosition);
                    float prefixWidth = sketch.textWidth(prefix);
                    float remainingWidth = fileNameMaxWidth - prefixWidth;

                    float wrappedTextY = drawWrappedText(fileName, fileNameStartX + prefixWidth, yPosition, remainingWidth, lineHeight);

                    float usedHeight = wrappedTextY - yPosition;
                    float blankHeight = Math.max(entryHeight, usedHeight);
                    
                    yPosition += blankHeight;
                }
            }

            // Disable clipping after drawing the scrollable content
            sketch.noClip();

            // Constrain scroll offset based on real content height
            float totalContentHeight = calculateTotalContentHeight();
            float maxScrollOffset = PApplet.max(0f, totalContentHeight - scrollAreaHeight + 10f);
            scrollOffset = PApplet.constrain(scrollOffset, 0, maxScrollOffset);

            // Draw little scroll bar based on content height
            if (totalContentHeight > scrollAreaHeight && maxScrollOffset > 0) {
                float scrollBarHeight = PApplet.max(20f, (scrollAreaHeight / totalContentHeight) * scrollAreaHeight);
                float scrollBarY = scrollAreaTop + (scrollOffset / maxScrollOffset) * (scrollAreaHeight - scrollBarHeight);
                float scrollBarX = sketch.width - sideMargin;
                float scrollBarWidth = 6;

                sketch.fill(230);
                sketch.noStroke();
                sketch.rect(scrollBarX, scrollAreaTop, scrollBarWidth, scrollAreaHeight, 3);

                sketch.fill(120, 120, 240);
                sketch.rect(scrollBarX, scrollBarY, scrollBarWidth, scrollBarHeight, 3);
            }
        }

        // Display scanning status text and progress bar percentage text
        String progressBarText = String.format("Progress: %.0f%%", progressBar.getProgress());
        sketch.fill(0);
        sketch.textSize(12);
        sketch.textAlign(PApplet.CENTER, PApplet.TOP);
        sketch.text(progressBarText, sketch.width / 2, sketch.height - 210);
        
        if (progressBar.isComplete()) {
            sketch.fill(0, 200, 0); // Green
            sketch.text("COMPLETED", sketch.width / 2, sketch.height - 180);
        } else {
            sketch.fill(255, 204, 0); // Yellow
            sketch.text("IN PROGRESS", sketch.width / 2, sketch.height - 180);
        }

        progressBar.display();
    }
}