import processing.core.PApplet;
import javax.swing.JFileChooser;
import java.io.File;

public class Main extends PApplet {

    private Button fileSelectButton;
    private String selectedFilePath = "No file selected";

    // Boolean to indicate if the start screen is activated or not
    private boolean isStartScreenVisible = true;

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

        int buttonWidth = 200;
        int buttonHeight = 50;
        int centerX = (width / 2) - (buttonWidth / 2);
        int centerY = (height / 2) - (buttonHeight / 2);

        fileSelectButton = new Button(this, centerX, centerY, buttonWidth, buttonHeight, "Choose a File");
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
        fileSelectButton.display();

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

        String displayMessage = "Current File:\n" + selectedFilePath;
        text(displayMessage, sideMargin, textYPosition, textBoxWidth, textboxHeight);
    }

    @Override
    public void mousePressed() {
        if (isStartScreenVisible) {
            isStartScreenVisible = false;
        } else if (fileSelectButton.isMouseHovering()) {
            openFileBrowser();
        }
    }

    private void openFileBrowser() {
        // Launces the file browser for any platform? (DO more research) (It looks really OLD on windows)

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            selectedFilePath = selectedFile.getAbsolutePath();
            System.out.println("File path selected: " + selectedFilePath);
        }
    }
}