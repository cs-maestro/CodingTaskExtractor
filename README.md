# Coding Task Extractor

Coding Task Extractor is a Java application that extracts TODO and FIXME tasks from source code files. It analyzes the specified files, identifies code comments containing TODO or FIXME keywords, and extracts the associated task descriptions. The extracted tasks are then written to a text file.

## Features

- Supports single-line and multi-line comments in Java, Python, and JavaScript files.
- Extracts tasks marked with TODO or FIXME keywords.
- Provides the line number of each task in the source code.
- Supports processing multiple files at once.
- Creates a separate output file for each input file, with tasks listed along with their line numbers.

## Usage

1. Clone the repository or download the source code.
2. Build the project using your preferred Java development environment or use the provided compiled JAR file.
3. Run the application and follow the prompts.

   - Specify the number of files you want to process.
   - Enter the path to each file.
   - The application will analyze the files and extract tasks from the code comments.
   - The extracted tasks will be written to separate text files in the same directory as the input files.

**Note:** Make sure to provide the correct file paths and ensure that the files have supported extensions (.java, .py, .js) for proper task extraction.

## Examples

Suppose you have two Java files, `MyClass.java` and `Utils.java`, containing the following comments:

**MyClass.java**
```java
// TODO: Implement this class
// FIXME: Fix the bug in the getSum method
```

**Utils.java**
```java
/*
 * Utility class for common functions
 * TODO: Add error handling
 * TODO: Refactor this method for better performance
 */
```
After running the Coding Task Extractor, two output files will be generated:

**MyClass.txt**
```
Implement this class - line 1
Fix the bug in the getSum method - line 2
```

**Utils.txt**
```
Add error handling - line 3
Refactor this method for better performance - line 4
```

## Dependencies

Java Development Kit (JDK) 8 or higher

## Contributing

Contributions to the Coding Task Extractor project are welcome. If you encounter any issues or have suggestions for improvements, please feel free to open an issue or submit a pull request.
