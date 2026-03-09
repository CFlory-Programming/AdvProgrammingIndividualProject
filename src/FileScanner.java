import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Files;

public class FileScanner {
    public String rootPath;
    public HashMap results;
    public boolean isRecursive;

    // Scan for duplicates by reading the file's bytes FOR NOW
    public Map<String, List<File>> scanForDuplicates(File folder) {
        // Initialize a map to group files depending on their content FOR NOW
        Map<String, List<File>> duplicateFiles = new HashMap<>();

        // Get all files and subdirectories in the selected folder
        File[] files = folder.listFiles(); // Gets an array of all the files and subdirectories in the selected folder
        if (files != null) {
            // Iterate through each item in the folder
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) { // Check if It's a file, and It's name ends with "txt"
                    try {
                        String content = new String(Files.readAllBytes(file.toPath())); // Read file content (bytes) as a string

                        if (duplicateFiles.containsKey(content)) { // Check if this content has been seen before
                            duplicateFiles.get(content).add(file); // Add the file to the existing list of duplicate files (Line 18)
                        } else {
                            List<File> duplicateFileList = new ArrayList<>(); // Create a new list if this content hasn't been seen before
                            duplicateFileList.add(file);
                            duplicateFiles.put(content, duplicateFileList); // Put the file byte content in the duplicateFileList
                        }
                    } catch (Exception e) {
                        System.out.println("Error reading file: " + file.getName());
                    }
                }
            }
        }

        // Remove entries that do not have duplicates (only one file with that content)
        duplicateFiles.entrySet().removeIf(entry -> entry.getValue().size() <= 1); // Lambda expression operator (Do more research)

        // Return the map containing only actual duplicates
        return duplicateFiles;
    }

    public void startScan(Path path) {

    }

    public List<File> filterFiles(File files) {
        return null;
    }

    public static void main(String[] args) {

    }
}
