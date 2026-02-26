import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Path;

public class FileScanner {
    public String rootPath;
    public HashMap results;
    public boolean isRecursive;
    private DuplicateMap duplicateMap;
    private FileAnalyzer fileAnalyzer;

    public void startScan(Path path) {

    }

    public List<File> filterFiles(File files) {
        return null;
    }

    public static void main(String[] args) {

    }
}
