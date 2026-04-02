import java.io.File;
import java.util.List;
import java.util.Map;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.Files;

public class FileScanner {
    public String rootPath;
    public boolean isRecursive;

    private int totalFiles = 0;
    private int scannedFiles = 0;

    public interface ScanProgressListener {
        void onProgressUpdate(float currentPercent, int scannedFiles, int totalFiles);
    }

    // Scan for duplicates by reading the file's bytes (file content and file size)
    public Map<String, List<FileInfo>> scanForDuplicates(File folder, ScanProgressListener listener) {
        // Compute total number of files to scan for progress tracking
        totalFiles = countSupportedFiles(folder);
        scannedFiles = 0;

        if (totalFiles == 0) {
            if (listener != null) {
                listener.onProgressUpdate(100f, 0, 0);
            }
        }

        // Initialize a map to group files depending on their content
        Map<String, List<FileInfo>> duplicateFiles = new HashMap<>();

        scanFolderRecursively(folder, duplicateFiles, listener);

        // After scanning all files, determine the reason for duplicates and which file to keep/delete
        determineDuplicateReasons(duplicateFiles);

        // Set progress to 100% at the end of scanning
        if (listener != null) {
            listener.onProgressUpdate(100f, scannedFiles, totalFiles);
        }
        
        // Return map containing all files so both the duplicate and non-duplicate files will be shown in the results screen
        return duplicateFiles;
    }

    private void scanFolderRecursively(File folder, Map<String, List<FileInfo>> duplicateFiles, ScanProgressListener listener) {
        // Get all files and subdirectories in the selected folder
        File[] files = folder.listFiles(); // Gets an array of all the files and subdirectories in the selected folder
        if (files != null) {
            // Iterate through each item in the folder
            for (File file : files) {
                if (file.isFile() && isSupportedFileType(file.getName())) {
                    try {
                        byte[] fileBytes = Files.readAllBytes(file.toPath());
                        long fileSize = file.length();
                        long lastModified = file.lastModified();

                        String contentKey = fileSize + "|" + java.util.Arrays.toString(fileBytes);

                        FileInfo fileInfo = new FileInfo(file, fileSize, lastModified);

                        if (duplicateFiles.containsKey(contentKey)) {
                            duplicateFiles.get(contentKey).add(fileInfo);
                        } else {
                            List<FileInfo> fileList = new ArrayList<>();
                            fileList.add(fileInfo);
                            duplicateFiles.put(contentKey, fileList);
                        }

                        if (listener != null && totalFiles > 0) {
                            scannedFiles++;
                            float progressPercent = ((float) scannedFiles / (float) totalFiles) * 100f;
                            listener.onProgressUpdate(PApplet.constrain(progressPercent, 0f, 99.5f), scannedFiles, totalFiles); // Cap progress at 99.5% until the end to allow the progress bar to fill up smoothly in the final step
                        }
                    } catch (Exception e) {
                        System.out.println("Error reading file: " + file.getName());

                        if (listener != null && totalFiles > 0) {
                            scannedFiles++;
                            float progressPercent = ((float) scannedFiles / (float) totalFiles) * 100f;
                            listener.onProgressUpdate(PApplet.constrain(progressPercent, 0f, 99.5f), scannedFiles, totalFiles); // Cap progress at 99.5% until the end to allow the progress bar to fill up smoothly in the final step
                        }
                    }
                } else if (file.isDirectory()) {
                    // Recursively scan subdirectories
                    scanFolderRecursively(file, duplicateFiles, listener);
                }
            }
        }
    }

    private int countSupportedFiles(File folder) {
        int count = 0;
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && isSupportedFileType(file.getName())) {
                    count++;
                } else if (file.isDirectory()) {
                    count += countSupportedFiles(file); // Recursively count files in subdirectories
                }
            }
        }
        return count;
    }

    private boolean isSupportedFileType(String name) {
        String lowercaseName = name.toLowerCase();
        return lowercaseName.endsWith(".txt") || lowercaseName.endsWith(".doc") || lowercaseName.endsWith(".docx") || lowercaseName.endsWith(".gdoc");
    }

    private void determineDuplicateReasons(Map<String, List<FileInfo>> duplicateFiles) {
        for (List<FileInfo> group: duplicateFiles.values()) {
            if (group.size() > 1) {
                // Mark the files that are a byte match
                for (FileInfo fileInfo : group) {
                    fileInfo.duplicateReason = "Byte match";
                }

                // Determine the most recently modified (KEEP THE FILE) and least recently modified (DELETE THE FILE)
                FileInfo mostRecentFileInfo = group.get(0);
                for (FileInfo fileInfo : group) {
                    if (fileInfo.lastModified > mostRecentFileInfo.lastModified) {
                        mostRecentFileInfo = fileInfo;
                    }
                }
                for (FileInfo fileInfo : group) {
                    if (fileInfo == mostRecentFileInfo) {
                        fileInfo.actionLabel = "Keep (most recently modified)";
                    } else {
                        fileInfo.actionLabel = "Delete (least recently modified)";
                    }
                }
            } else {
                // Mark the file that is not a duplicate
                for (FileInfo fileInfo : group) {
                    fileInfo.duplicateReason = "Unique";
                    fileInfo.actionLabel = "Keep (unique)";
                }
            }
        }
    }
}
