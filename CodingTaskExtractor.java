import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CodingTaskExtractor {

    // List of patterns for different comment types
    private static final List<Pattern> COMMENT_PATTERNS = Arrays.asList(
            Pattern.compile("//.*"),  // Single-line comments starting with //
            Pattern.compile("#.*"),   // Single-line comments starting with #
            Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL)  // Multi-line comments between /* and */
    );

    // Pattern to match TODO and FIXME comments
    private static final Pattern TODO_FIXME_PATTERN = Pattern.compile("(?i)(todo|fixme)\\s*:(.*)");

    // Inner class representing a coding task
    private static class Task {
        private final String description;
        private final int lineNumber;

        Task(String description, int lineNumber) {
            this.description = description;
            this.lineNumber = lineNumber;
        }

        String getDescription() {
            return description;
        }

        int getLineNumber() {
            return lineNumber;
        }
    }

    private List<Task> tasks = new ArrayList<>();

    // Extracts coding tasks from the given file
    public void extractTasks(String filePath) {
        String fileExtension = getFileExtension(filePath);
        if (!isSupportedFileType(fileExtension)) {
            System.out.println("Skipping file: " + filePath + " (Unsupported file type)");
            return;
        }

        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            StringBuilder commentBuilder = new StringBuilder();
            boolean insideComment = false;
            int lineNumber = 1;

            for (String line : (Iterable<String>) lines::iterator) {
                if (insideComment) {
                    commentBuilder.append(line).append("\n");
                    if (line.trim().endsWith("*/")) {
                        String comment = commentBuilder.toString();
                        extractTasksFromComment(comment, lineNumber - 1);
                        insideComment = false;
                        commentBuilder.setLength(0);
                    }
                } else {
                    for (Pattern commentPattern : COMMENT_PATTERNS) {
                        Matcher matcher = commentPattern.matcher(line);
                        if (matcher.find()) {
                            String comment = matcher.group();
                            extractTasksFromComment(comment, lineNumber);
                            if (comment.trim().startsWith("/*") && !comment.trim().endsWith("*/")) {
                                insideComment = true;
                                commentBuilder.append(comment).append("\n");
                            }
                            break;
                        }
                    }
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.err.println("Error occurred while reading the file " + filePath + ": " + e.getMessage());
        }
    }

    // Extracts coding tasks from a comment
    private void extractTasksFromComment(String comment, int lineNumber) {
        Matcher matcher = TODO_FIXME_PATTERN.matcher(comment);
        while (matcher.find()) {
            tasks.add(new Task(matcher.group(2).trim(), lineNumber));
        }
    }

    // Retrieves the file extension from the file path
    private String getFileExtension(String filePath) {
        String extension = "";
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filePath.length() - 1) {
            extension = filePath.substring(dotIndex + 1).toLowerCase();
        }
        return extension;
    }

    // Checks if the file extension is supported
    private boolean isSupportedFileType(String extension) {
        List<String> supportedExtensions = Arrays.asList("java", "py", "js");  // Add more extensions as needed
        return supportedExtensions.contains(extension);
    }

    // Writes the extracted tasks to a text file
    public void writeTasksToFile(String filePath) {
        String outputFilename = filePath.substring(0, filePath.lastIndexOf('.')) + ".txt";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilename))) {
            for (Task task : tasks) {
                writer.write(task.getDescription() + " - line " + task.getLineNumber());
                writer.newLine();
            }
            System.out.println("Output written to: " + outputFilename);
        } catch (IOException e) {
            System.err.println("Error occurred while writing to the file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How many files do you want to process?");
        int fileCount;
        try {
            fileCount = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input. Please enter an integer number.");
            scanner.close();
            return;
        }
        CodingTaskExtractor extractor = new CodingTaskExtractor();
        for (int i = 0; i < fileCount; i++) {
            System.out.println("Enter the path to file " + (i + 1) + ":");
            String filePath = scanner.nextLine();
            while (!Files.exists(Paths.get(filePath)) || !extractor.isSupportedFileType(extractor.getFileExtension(filePath))) {
                System.out.println("Invalid file path or unsupported file type. Please try again:");
                filePath = scanner.nextLine();
            }
            extractor.extractTasks(filePath);
            extractor.writeTasksToFile(filePath);
            extractor.tasks.clear();  // Clear tasks for the next file
        }
        scanner.close();
    }
}
