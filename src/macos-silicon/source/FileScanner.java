import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.nio.file.Files;

public class FileScanner {
    public String rootPath;
    public boolean isRecursive;

    // Scan for duplicates by reading the file's bytes (file content and file size)
    public Map<String, List<FileInfo>> scanForDuplicates(File folder) {
        // Initialize a map to group files depending on their content
        Map<String, List<FileInfo>> duplicateFiles = new HashMap<>();

        scanFolderRecursively(folder, duplicateFiles);

        determineDuplicateReasons(duplicateFiles);
        
        // Return map containing all files so both the duplicate and non-duplicate files will be shown in the results screen
        return duplicateFiles;
    }

    private void scanFolderRecursively(File folder, Map<String, List<FileInfo>> duplicateFiles) {
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
                    } catch (Exception e) {
                        System.out.println("Error reading file: " + file.getName());
                    }
                } else if (file.isDirectory()) {
                    // Recursively scan subdirectories
                    scanFolderRecursively(file, duplicateFiles);
                }
            }
        }
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
