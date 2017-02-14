package ejf;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

  private static final String CODE_STYLE_HUB = "eclipse-java-google-style.xml";
    private static final String CODE_STYLE_KEY = "codeStyle";
    private static Formatter formatter;

    public static void main(final String[] args) {
        String codeStyle = CODE_STYLE_HUB;
        if (System.getProperties().contains(codeStyle)) {
            codeStyle = System.getProperty(CODE_STYLE_KEY);
        }

        if (args.length < 1) {
            System.err.println("Missing arguments, ex : [file1.java file2.java ...]");
            return;
        }

        try {
            formatter = new Formatter(codeStyle);
        } catch (Exception e) {
            System.err.println(e.toString());
            return;
        }

        for (final String file : Arrays.copyOfRange(args, 1, args.length)) {
            boolean success = format(file);
            if (!success) {
                break;
            }
        }
    }

    private static boolean format(String file) {
        boolean success = false;
        try {
            Path path = Paths.get(file);
            String fileContent = new String(Files.readAllBytes(path));
            String formattedContent = formatter.format(fileContent);
            Files.write(path, formattedContent.getBytes());
            success = true;
        } catch (Exception e) {
            System.err.println(e.toString() + ": " + file);
        }
        return success;
    }

}
