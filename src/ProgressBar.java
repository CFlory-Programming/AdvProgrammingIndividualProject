public class ProgressBar {
    public int x, y, width, height;
    public float progress;

    public ProgressBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.progress = 0;
    }

    public void display() {
        // Draw the background of the progress bar
        Main.sketch.fill(200);
        Main.sketch.rect(x, y, width, height);

        // Draw the filled portion of the progress bar
        Main.sketch.fill(100, 150, 255);

        progress += Math.random();
        if (progress > 100) {
            progress = 100;
        }

        Main.sketch.rect(x, y, (int)(width * (progress/100)), height);

        // Draw the border of the progress bar
        Main.sketch.noFill();
        Main.sketch.stroke(0);
        Main.sketch.rect(x, y, width, height);
    }

    public boolean isComplete() {
        return progress >= 100;
    }

    public void reset() {
        progress = 0;
    }
}
