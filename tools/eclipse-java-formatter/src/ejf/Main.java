package ejf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.eclipse.jface.text.BadLocationException;

public class Main {

  private static final String CODE_STYLE_DEFAULT = "eclipse-java-google-style.xml";
  private static final String CODE_STYLE_KEY = "codeStyle";
  private static Formatter formatter;

  public static void main(final String[] args) throws IOException, BadLocationException {
    String codeStyle = CODE_STYLE_DEFAULT;
    if (System.getProperties().containsKey(CODE_STYLE_KEY)) {
      codeStyle = System.getProperty(CODE_STYLE_KEY);
    }

    if (args.length < 1) {
      System.err.println("Missing arguments, ex : [file1.java file2.java ...]");
      return;
    }

    Set<String> filesTobeFormatted = new TreeSet<String>(Arrays.asList(args));

    int fileCount = 1;
    int fileTotal = filesTobeFormatted.size();
    try {
      System.out.println(String.format("Start formatting code using codeStyle=%s", codeStyle));
      System.out.println(String.format("There are %s files to process", fileTotal));
      formatter = new Formatter(codeStyle);
    } catch (Exception e) {
      System.err.println(e.toString());
      System.exit(2);
    }

    int formattedFileNumber = 0;

    for (final String file : filesTobeFormatted) {
      System.out.println(String.format("Formatting [%s/%s] : %s", fileCount++, fileTotal, file));
      if (format(file)) {
        formattedFileNumber++;
      }
    }

    if (formattedFileNumber > 0) {
      System.out.println(String.format("WARNING: %s fomatted files...", formattedFileNumber));
    }
  }

  private static boolean format(String file) throws IOException, BadLocationException {
    Path path = Paths.get(file);
    String fileContent = new String(Files.readAllBytes(path));
    String formattedContent = formatter.format(fileContent);
    Files.write(path, formattedContent.getBytes());
    String fileContent2 = new String(Files.readAllBytes(path));
    return !fileContent.equals(fileContent2);
  }

}
