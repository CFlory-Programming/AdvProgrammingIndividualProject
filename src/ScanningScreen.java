import processing.core.PApplet;
import java.io.File;
import java.util.List;
import java.util.Map;

public class ScanningScreen {
    private PApplet sketch;
    private Button seeResultsButton;
    private ProgressBar progressBar;
    private String selectedFolderPath;
    private Map<String, List<File>> duplicateFiles;

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

    public void setDuplicateFiles(Map<String, List<File>> duplicateFiles) {
        this.duplicateFiles = duplicateFiles;
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
        sketch.text(displayMessage, sideMargin, topMargin, textBoxWidth, 60);

        // Show all files in the directory that will be scanned
        if (duplicateFiles != null) {
            sketch.fill(0, 0, 0);
            sketch.textSize(11);
            sketch.textAlign(PApplet.LEFT, PApplet.TOP);

            int listGap = 120;
            sketch.text("Scanning Files:", sideMargin, listGap);
            listGap += 15;

             for (List<File> group: duplicateFiles.values()) {
                for (File file: group) {
                    sketch.text("  - " + file.getName(), sideMargin, listGap);
                    listGap += 15;
                }
            }
        }

        // Display scanning status text
        if (progressBar.isComplete()) {
            sketch.fill(0, 200, 0); // Green
            sketch.text("COMPLETED", sketch.width / 2 - 40, sketch.height - 90);
        } else {
            sketch.fill(255, 204, 0); // Yellow
            sketch.text("IN PROGRESS", sketch.width / 2 - 50, sketch.height - 90);
        }

        progressBar.display();
    }
}
