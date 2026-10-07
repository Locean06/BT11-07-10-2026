package vn.edu.hcmute.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class UploadConfig_24110282 {

    private static final String PROJECT_PATH =
            "C:\\Users\\LOQ\\Downloads\\LTWEB\\bt11";

    public static Path getUploadPath() {
        return Paths.get(PROJECT_PATH, "uploads");
   }
}