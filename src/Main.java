import processing.core.PApplet;

public class Main extends PApplet {

    // Boolean to indicate if the start screen is activated or not
    public static boolean startScreen = true;

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
    }

    @Override
    public void draw() {
        // Window should begin with a blank white screen
        background(255);

        // If the user clicks their mouse, the start screen should disappear
        if (mousePressed) {
            startScreen = false;
        }

        if (startScreen) {
            // Display the start screen
            fill(255, 255, 0); // Yellow text
            textAlign(CENTER);
            textSize(30);
            text("WARNING: USE THIS PROGRAM WITH CAUTION AS IT HAS THE ABILITY TO DELETE ANY FILE ON YOUR COMPUTER!", 0, height/2, width, height); // Warning Text

        } else {
            // Begin the program
            fill(255, 0, 0); // Red Text
            textAlign(CENTER);
            textSize(48);
            // Draw 2 texts with +1 pixel offset on x-axis to create fake bold
            text("Duplicate File Finder", width/2, 50); // Title
            text("Duplicate File Finder", width/2 + 1, 50); // Title

            boolean hover = mouseX > width/2 - 100 && mouseX < width/2 + 100 && mouseY > height/2 - 25 && mouseY < height/2 + 25;
            if (hover) {
                fill(200, 255, 255); // Lighter blue color when hovering
            } else {
                fill(255, 255, 255); // Normal color
            }

            rect(width/2 - 100, height/2 - 25, 200, 50); // Button

            fill(0); // Black text
            textSize(12);
            text("Choose a File", width/2, height/2 + 7); // Button Text

            text("Choose a file below to check if they are duplicates", width/2, height/2 + 80); // Instructions


        }
    }


}
