import processing.core.PApplet;
import java.io.File;
import java.util.List;
import java.util.Map;

public class MainScreen {
    private PApplet sketch;
    private Button folderSelectButton;
    private String selectedFolderPath;
    private Map<String, List<File>> duplicateFiles;

    public MainScreen(PApplet sketch, Button folderSelectButton) {
        this.sketch = sketch;
        this.folderSelectButton = folderSelectButton;
        this.selectedFolderPath = "No folder selected";
        this.duplicateFiles = null;
    }

    public void display() {
        sketch.background(255);

        folderSelectButton.display();

        int textYPosition = sketch.height / 2 + 80;
        int sideMargin = 25;
        int topMargin = 50;
        int textBoxWidth = sketch.width - (sideMargin * 2); // 25 px on each side margin
        int textboxHeight = sketch.height - (topMargin * 2); // 50 px on top and bottom margin

        sketch.fill(50, 0, 0);
        sketch.textAlign(PApplet.CENTER, PApplet.TOP); // Center top of the screen, growing down
        sketch.textSize(14);

        // Use fake bold effect by drawing twice and change x by 1
        sketch.text("CarbonCopy", sketch.width / 2, 40);
        sketch.text("CarbonCopy", sketch.width / 2 + 1, 40);

        drawCarbonCopyLogo();

        String displayMessage = "Current Folder:\n" + selectedFolderPath;
        sketch.text(displayMessage, sideMargin, textYPosition, textBoxWidth, textboxHeight);

        //Show the results if the analysis has been done
        if (duplicateFiles != null) {
            sketch.fill(0, 0, 0);
            sketch.textSize(12);
            sketch.textAlign(PApplet.LEFT, PApplet.TOP);

            // Auto format the possible duplicate files on the y axis so they don't overlap
            int listGap = 100;
            // Groups will be the files that have the same content
            boolean hasDuplicates = false;

            for (java.util.List<File> group: duplicateFiles.values()) {
                if (group.size() > 1) { // Only draw the group if it has more than 1 file
                    hasDuplicates = true;
                    break;
                }
            }

            if (!hasDuplicates) {
                sketch.text("No duplicate .txt files found.", sideMargin, listGap);
            }
        }
    }

    private void drawCarbonCopyLogo() {
        int logoX = sketch.width / 2;
        int logoY = 100;
        int logoSize = 50;

        // Display the CarbonCopyLogo.png image
        processing.core.PImage logo = sketch.loadImage("../images/CarbonCopyLogo.png");
        if (logo != null) {
            sketch.image(logo, logoX - logoSize / 2, logoY - logoSize / 2, logoSize, logoSize);
        }
    }
    
}
