import java.io.File;

public class FileInfo {
    public File file;
    public long fileSize;
    public long lastModified;
    public String duplicateReason;
    public String actionLabel;

    public FileInfo(File file, long fileSize, long lastModified) {
        this.file = file;
        this.fileSize = fileSize;
        this.lastModified = lastModified;
        this.duplicateReason = "";
        this.actionLabel = "";
    }
}