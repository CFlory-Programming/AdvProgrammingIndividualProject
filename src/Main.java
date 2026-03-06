import processing.core.PApplet;
import java.io.File;
import java.util.Map;

public class Main extends PApplet {

    private Button folderSelectButton;
    private String selectedFolderPath = "No folder selected";

    // Boolean to indicate if the start screen is activated or not
    private boolean isStartScreenVisible = true;

    private FileScanner fileScanner;
    private Map<String, java.util.List<File>> duplicateFiles;

    public static void main(String[] args) {
        // Start the PApplet in a new window
        PApplet.main("Main");
    }

    @Override
    public void settings() {
        // Set up the window size
        size(500, 500);
    }

    @Override
    public void setup() {
        // Set initial background color
        background(255);

        // Initialize the FileScanner
        fileScanner = new FileScanner();


        // Creating the "Choose a Folder" button
        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = (width / 2) - (buttonWidth / 2);
        int centerY = (height / 2) - (buttonHeight / 2);

        folderSelectButton = new Button(this, centerX, centerY, buttonWidth, buttonHeight, "Choose a Folder");
    }

    @Override
    public void draw() {
        // Window should begin with a blank white screen
        background(255);


        if (isStartScreenVisible) {
            drawWarningScreen();
        } else {
            drawMainScreen();
        }
    }


    private void drawWarningScreen() {
            // Display the start screen
            background(255);
            fill(255, 204, 0); // Yellow text
            textAlign(CENTER, CENTER);
            textSize(24);

            int sideMargin = 40;
            int topMargin = 50;
            int textBoxWidth = width - (sideMargin * 2); // 40 px on each side margin
            int textboxHeight = height - (topMargin * 2); // 50 px on top and bottom margin

            String warningMessage = "WARNING: USE THIS PROGRAM WITH CAUTION AS IT HAS THE ABILITY TO DELETE ANY FILE ON YOUR COMPUTER!";

            text(warningMessage, sideMargin, topMargin, textBoxWidth, textboxHeight); // Warning Text
        }

    private void drawMainScreen() {
        folderSelectButton.display();

        int textYPosition = height / 2 + 80;
        int sideMargin = 25;
        int topMargin = 50;
        int textBoxWidth = width - (sideMargin * 2); // 25 px on each side margin
        int textboxHeight = height - (topMargin * 2); // 50 px on top and bottom margin

        fill(50, 0, 0);
        textAlign(CENTER, TOP); // Center top of the screen, growing down
        textSize(14);

        // Use fake bold effect by drawing twice and change x by 1
        text("CarbonCopy", width / 2, 40);
        text("CarbonCopy", width / 2 + 1, 40);

        String displayMessage = "Current Folder:\n" + selectedFolderPath;
        text(displayMessage, sideMargin, textYPosition, textBoxWidth, textboxHeight);

        //Show the results if the analysis has been done
        if (duplicateFiles != null) {
            fill(0, 0, 0);
            textSize(12);
            textAlign(LEFT, TOP);

            // Auto format the possible duplicate files on the y axis so they don't overlap
            int listGap = 100;
            // Groups will be the files that have the same content
            int groupNumber = 1;
            boolean hasDuplicates = false;

            for (java.util.List<File> group: duplicateFiles.values()) {
                if (group.size() > 1) {
                    hasDuplicates = true;
                    text("Group " + groupNumber + ":", sideMargin, listGap);
                    listGap += 20;

                    for (File file: group) {
                        text("  - " + file.getName(), sideMargin, listGap);
                        listGap += 20;
                    }

                    listGap += 10;
                    groupNumber++;
                }
            }

            if (!hasDuplicates) {
                text("No duplicate .txt files found.", sideMargin, listGap);
            }
        }
    }

    @Override
    public void mousePressed() {
        if (isStartScreenVisible) {
            isStartScreenVisible = false;
        } else if (folderSelectButton.isMouseHovering()) {
            if (duplicateFiles != null) {
                duplicateFiles = null; // Clear previous results when selecting a new folder
                selectedFolderPath = "No folder selected"; // Reset the displayed folder path
            } else {
                openFileBrowser(); // This would be the first time opening the program
            }
        }
    }

    private void openFileBrowser() {
        // Launces the file browser for any platform (Processing's built in selectInput) - ALSO THIS LOOKS WEIRD ON WINDOWS, BUT FINE ON MAC
        selectFolder("Select a folder to scan for duplicates:", "folderSelected");
    }

    public void folderSelected(File selectedFolder) {
        selectedFolderPath = selectedFolder.getAbsolutePath();
        System.out.println("Folder path selected: " + selectedFolderPath);

        // Scan for duplicate files
        duplicateFiles = fileScanner.scanForDuplicates(selectedFolder);
    }


}