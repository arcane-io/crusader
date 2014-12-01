package crusader.commons.io;

import crusader.commons.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Utility class for File manipulation.
 * Extends functionality from {@link org.apache.commons.io.FileUtils}
 * 
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public final class FileUtil extends org.apache.commons.io.FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * Convenience method to create File instance.
     *
     * @param path     File path
     * @param fileName File name
     * @return File instance
     */
    public static File fileInstance(String path, String fileName) throws IOException {
        return fileInstance(path, fileName, false);
    }

    /**
     * Convenience method to create File instance.
     *
     * @param path          File path
     * @param fileName      File name
     * @param createMissing Create missing directories
     * @return File instance
     */
    public static File fileInstance(String path, String fileName, boolean createMissing) throws IOException {
        String fullPath = StringUtil.concat(File.separator, path, fileName);

        File file = new File(fullPath);
        File parent = file.getParentFile();

        if (createMissing && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Failed to create complete path '" + path + "'");
        }

        return file;
    }

    /**
     * Private constructor to prevent instantiation.
     */
    private FileUtil() {
    }
}
