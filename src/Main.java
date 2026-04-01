import processing.core.PApplet;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main extends PApplet {

    public static PApplet sketch;

    private boolean deleteFunctionality = false; // TESTING!!!!!!!!!!!!!!

    // Export log
    private Path folderPath = Path.of("exports");
    private Path filePath = folderPath.resolve("");

    private Button folderSelectButton;
    private Button seeResultsButton;
    private Button deleteButton;
    private ProgressBar progressBar;
    private String selectedFolderPath = "No folder selected";

    // Screen state tracking
    private enum ScreenState {WARNING, MAIN, SCANNING, RESULTS, STATS} // Enumeration
    private ScreenState currentScreen = ScreenState.WARNING;

    private FileScanner fileScanner;
    private Map<String, java.util.List<FileInfo>> duplicateFiles;
    private StringBuilder stringBuilder = new StringBuilder();

    private WarningScreen warningScreen;
    private MainScreen mainScreen;
    private ScanningScreen scanningScreen;
    private ResultsScreen resultsScreen;
    private StatsScreen statsScreen;

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
        sketch = this;

        // Set initial background color
        background(255);

        // Initialize the FileScanner
        fileScanner = new FileScanner();

        progressBar = new ProgressBar(50, height - 60, width - 100, 50);

        // Create the "Choose a Folder" button
        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = (width / 2) - (buttonWidth / 2);
        int centerY = (height / 2) - (buttonHeight / 2);

        folderSelectButton = new Button(this, centerX, centerY, buttonWidth, buttonHeight, "Choose a Folder");
        seeResultsButton = new Button(this, centerX, centerY + 120, buttonWidth, buttonHeight + 30, "See Results");
        deleteButton = new Button(this, centerX, centerY + 180, buttonWidth, buttonHeight, "DELETE");
        deleteButton.setNormalColor(200, 0, 0);
        deleteButton.setHoverColor(255, 0, 0);

        // Initialize the screen objects
        warningScreen = new WarningScreen(this);
        mainScreen = new MainScreen(this, folderSelectButton);
        scanningScreen = new ScanningScreen(this, seeResultsButton, progressBar);
        resultsScreen = new ResultsScreen(this, deleteButton);
        statsScreen = new StatsScreen(this);
    }

    @Override
    public void draw() {
        // Window should begin with a blank white screen
        background(255);

        switch (currentScreen) {
            case WARNING:
                warningScreen.display();
                break;
            case MAIN:
                mainScreen.display();
                break;
            case SCANNING:
                scanningScreen.display();
                break;
            case RESULTS:
                resultsScreen.display();
                break;
            case STATS:
                statsScreen.display();
                break;
        }
    }

    private String formatDate(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(new Date(timestamp));
    }

    @Override
    public void mousePressed() {
        switch (currentScreen) {
            case WARNING:
                if (warningScreen.handleMousePressed()) {
                    currentScreen = ScreenState.MAIN;
                }
                break;
            case MAIN:
                if (folderSelectButton.isMouseHovering()) {
                    if (duplicateFiles != null) {
                        duplicateFiles = null; // Clear the previous results if the user clicks the button again to select a new folder
                        selectedFolderPath = "No folder selected"; // Reset the displayed folder path
                    }
                    openFileBrowser();
                }
                break;
            case SCANNING:
                if (seeResultsButton.isMouseHovering() && progressBar.isComplete()) {
                    resultsScreen.setDuplicateFiles(duplicateFiles); // Pass the duplicate files to the results screen

                    long freedStorage = 0;
                    for (List<FileInfo> group : duplicateFiles.values()) {
                        if (group.size() > 1) {
                            // Find the file with the oldest modification time
                            FileInfo leastRecentFile = group.get(0);
                            for (FileInfo fileInfo : group) {
                                if (fileInfo.lastModified < leastRecentFile.lastModified) {
                                    leastRecentFile = fileInfo;
                                }
                            }

                            freedStorage += leastRecentFile.fileSize;
                        }
                    }
                    resultsScreen.setStats(freedStorage);
                    currentScreen = ScreenState.RESULTS;
                }
                break;
            case RESULTS:
                int NUKEOFDOOMResult = resultsScreen.handleMousePressed();
                if (NUKEOFDOOMResult == 1) {
                    // YES pressed: Get stats and show stats screen
                    int deletedCount = 0;
                    long freedStorage = 0;


                    // Create export log
                    long currentTime = System.currentTimeMillis();
                    String fileName = "deleted_files_" + formatDate(currentTime) + ".txt";
                    filePath = folderPath.resolve(fileName);
                    // Create exports directory if it doesn't exist
                    try {
                        Files.createDirectories(folderPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                    stringBuilder.append("Deleted files: (").append(formatDate(currentTime)).append(")\n");

                    for (List<FileInfo> group : duplicateFiles.values()) {
                        if (group.size() > 1) {
                            // Find the file with the oldest modification time
                            FileInfo leastRecentFile = group.get(0);
                            for (FileInfo fileInfo : group) {
                                if (fileInfo.lastModified < leastRecentFile.lastModified) {
                                    leastRecentFile = fileInfo;
                                }
                            }

                            // Delete only the least recently modified file
                            deletedCount++;
                            freedStorage += leastRecentFile.fileSize;

                            System.out.println("Deleting file: " + leastRecentFile.file.getAbsolutePath());

                            stringBuilder.append(leastRecentFile.file).append("\n");

                            if (deleteFunctionality) {
                                leastRecentFile.file.delete();
                            }
                        }
                    }

                    // Write to the export log
                    try {
                        Files.writeString(filePath, stringBuilder.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    statsScreen.setStats(deletedCount, freedStorage);
                    currentScreen = ScreenState.STATS;
                } else if (NUKEOFDOOMResult == -1) {
                    // NO pressed: Go back to main screen
                    goHome();
                } else if (deleteButton.isMouseHovering()) {
                    resultsScreen.drawDeleteConfirmation();
                }
                break;
            case STATS:
                if (statsScreen.handleMousePressed() == 1) {
                    goHome();
                }
                break;
        }
    }

    @Override
    public void mouseWheel(processing.event.MouseEvent event) {
        float direction = event.getCount();
        switch (currentScreen) {
            case SCANNING:
                scanningScreen.handleMouseWheel(direction);
                break;
            case RESULTS:
                resultsScreen.handleMouseWheel(direction);
                break;
            default:
                break;
        }   
    }

    private void openFileBrowser() {
        // Launces the file browser for any platform (Processing's built in selectInput) - ALSO THIS LOOKS WEIRD ON WINDOWS, BUT FINE ON MAC
        selectFolder("Select a folder to scan for duplicates:", "folderSelected");
    }

    public void folderSelected(File selectedFolder) {
        selectedFolderPath = selectedFolder.getAbsolutePath(); // The user/desktop/AdvProgrammingIndividualProject thing, it can get LONG
        System.out.println("Folder path selected: " + selectedFolderPath);

        // Call the method in FileScanner.java
        duplicateFiles = fileScanner.scanForDuplicates(selectedFolder);

        // Set the duplicate files in the scanning screen
        scanningScreen.setDuplicateFiles(duplicateFiles);

        // Switch to the scanning screen
        currentScreen = ScreenState.SCANNING;

        scanningScreen.setSelectedFolderPath(selectedFolderPath); // Pass the selected folder path to the scanning screen
    }

    private void goHome() {
        // Reset state so user can start over
        duplicateFiles = null;
        selectedFolderPath = "No folder selected";

        scanningScreen.setSelectedFolderPath(selectedFolderPath);
        scanningScreen.setDuplicateFiles(null);

        resultsScreen.setDuplicateFiles(null);

        progressBar.reset();

        statsScreen.setStats(0, 0);

        currentScreen = ScreenState.MAIN;
    }
}