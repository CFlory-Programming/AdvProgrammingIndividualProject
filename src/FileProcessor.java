import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.nio.file.Path;

public class FileProcessor extends FileAnalyzer {
    public List<String> supportedExtensions;
    public Map<String, Object> extractedMetaData;

    public String parseDocument(File doc) {
        return null;
    }

    public void extractExif(File image) {
        
    }
}
