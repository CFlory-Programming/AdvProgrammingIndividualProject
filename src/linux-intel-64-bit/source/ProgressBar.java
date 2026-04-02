import processing.core.PApplet;

public class ProgressBar {
    public int x;
    public int y;
    public int width;
    public int height;
    private float progress; // Current progress
    private float targetProgress; // Target set by ScanningScreen
    private float animationSpeed = 1.2f; // How fast the progress bar fills up

    public ProgressBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.progress = 0;
        this.targetProgress = 0;
    }

    public void setTargetProgress(float value) {
        targetProgress = PApplet.constrain(value, 0f, 100f); // Don't go out of the box silly!
    }

    public float getTargetProgress() {
        return targetProgress;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float value) {
        progress = PApplet.constrain(value, 0f, 100f);
        targetProgress = progress; // Sync targetProgress with current progress when manually set in ScanningScreen
    }

    private void update() {
        if (progress >= targetProgress) {
            return; // Stop when target reached
        }

        // Simulate slower/faster progress and stuttering

        float remaining = targetProgress - progress;

        // Speed of current progress is based on remaining to scan (slower near completion)
        float baseSpeed = PApplet.map(remaining, 0f, 100f, 0.3f, animationSpeed);

        // Stutter: Random variablility every frame as in the update method
        float variability = 0.4f + (float) Math.random() * 1.0f; // Random factor between 0.4 and 1.4
        float frameProgress = baseSpeed * variability;

        // Occasionally jump forward a bit when current progress is behind
        if (remaining > 20f && Math.random() < 0.06) { // 6% chance to jump when far from target
            frameProgress *= 1.4f + (float) Math.random() * 1.2f; // Jump boost between 1.4x and 2.6x
        }

        progress += frameProgress;
        progress = PApplet.constrain(progress, 0f, targetProgress); // Don't go past the target silly!
    }

    public void display() {
        update();

        // Draw the background of the progress bar
        Main.sketch.fill(200);
        Main.sketch.rect(x, y, width, height);

        // Draw the filled portion of the progress bar
        Main.sketch.fill(100, 150, 255);

        Main.sketch.rect(x, y, (int)(width * (progress / 100f)), height);

        // Draw the border of the progress bar
        Main.sketch.noFill();
        Main.sketch.stroke(0);
        Main.sketch.rect(x, y, width, height);

        // Readout the percentage text
        Main.sketch.fill(0);
        Main.sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        Main.sketch.textSize(12);
        Main.sketch.text(String.format("%.0f%%", progress), x + width / 2f, y + height / 2f);
    }

    public boolean isComplete() {
        return progress >= 100f && targetProgress >= 100f;
    }

    public void reset() {
        progress = 0;
        targetProgress = 0;
    }
}
