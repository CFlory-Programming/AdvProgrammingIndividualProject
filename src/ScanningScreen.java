import processing.core.PApplet;
import java.util.List;
import java.util.Map;

public class ScanningScreen {
    private PApplet sketch;
    private Button seeResultsButton;
    private ProgressBar progressBar;
    private String selectedFolderPath;
    private Map<String, List<FileScanner.FileInfo>> duplicateFiles;
    private float scrollOffset = 0;
    private float scrollSpeed = 15;

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

    public void setDuplicateFiles(Map<String, List<FileScanner.FileInfo>> duplicateFiles) {
        this.duplicateFiles = duplicateFiles;
        this.scrollOffset = 0; // Reset scroll position when new files are loaded
    }

    public void handleMouseWheel(float direction) {
        // direction is positive for scrolling up and negative for scrolling down
        scrollOffset += direction * scrollSpeed;
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

            float currentY = scrollAreaTop - scrollOffset;
            float totalContentHeight = 0;

             for (List<FileScanner.FileInfo> group : duplicateFiles.values()) {
                for (FileScanner.FileInfo fileInfo : group) {
                    sketch.text("  - " + fileInfo.file.getName(), sideMargin, currentY);

                    currentY += 18f;
                    totalContentHeight += 18f;
                }
            }

            // Disable clipping after drawing the scrollable content
            sketch.noClip();

            // Constrain scroll offset based on real content height
            scrollOffset = PApplet.constrain(scrollOffset, 0, Math.max(0f, totalContentHeight - scrollAreaHeight + 10f));
        }

        // Display scanning status text
        if (progressBar.isComplete()) {
            sketch.fill(0, 200, 0); // Green
            sketch.text("COMPLETED", sketch.width / 2 - 30, sketch.height - 200);
        } else {
            sketch.fill(255, 204, 0); // Yellow
            sketch.text("IN PROGRESS", sketch.width / 2 - 30, sketch.height - 200);
        }

        progressBar.display();
    }
}