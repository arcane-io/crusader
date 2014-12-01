package crusader.commons.io;

import crusader.commons.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Convenience class to Zip/UnZip data.
 * 
 * @author Catalin Manolescu <cc.manolescu@gmail.com>
 */
public abstract class Zip {
    
    public static Set<File> unzip(String path, String fileName, String destination) throws IOException {
        return unzip(new FileInputStream(FileUtil.fileInstance(path, fileName)), destination);
    }

    public static Set<File> unzip(File zipFile, String destination) throws IOException {
        return unzip(new FileInputStream(zipFile), destination);
    }

    /**
     * Unzip from input stream.
     * @param in InputStream
     * @param destination Destination for unzipped files
     */
    public static Set<File> unzip(InputStream in, String destination) throws IOException {
        ZipInputStream zis = new ZipInputStream(in);

        Set<File> result = new HashSet<File>();

        ZipEntry zipEntry;
        File zipEntryFile;
        try {
            while ((zipEntry = zis.getNextEntry()) != null) {
                FileOutputStream fos = null;
                try {
                    String outputPath = StringUtil.isEmpty(destination) ? SystemUtils.JAVA_IO_TMPDIR : destination;
                    zipEntryFile = FileUtil.fileInstance(outputPath, zipEntry.getName(), true);
                    fos = new FileOutputStream(zipEntryFile);
                    IOUtils.copyLarge(zis, fos);
                    result.add(zipEntryFile);
                } finally {
                    IOUtils.closeQuietly(fos);
                    zis.closeEntry();
                }
            }
        } finally {
            IOUtils.closeQuietly(zis);
        }

        return result;
    }
}
