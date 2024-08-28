# PDF Content Segmenter

## Overview
The PDF Content Segmenter is a Java application that segments a PDF file into distinct sections based on significant whitespace between blocks of text. It uses Apache PDFBox to analyze and manipulate PDF content without image processing.


## Technologies Used
- **Java 8 or above**
- **Apache PDFBox**


## Setup Instructions

1. **Install Java 8 or Above:**
   Ensure that you have Java 8 or above installed on your machine. You can download and install it from the [official Oracle website](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) or use OpenJDK.

2. **Download Apache PDFBox:**
   Download the latest PDFBox JAR files from the [Apache PDFBox download page](https://pdfbox.apache.org/download.html). You will need:
   - `pdfbox-app-<version>.jar` (e.g., `pdfbox-app-2.0.29.jar`)

3. **Create a New Java Project in Eclipse:**
4. **Add PDFBox to Your Project:**
5. **Create the `PDFSegmenter` Class:**
6. **Create Unit Tests:**
 

## How to Run the Application in Eclipse

1. **Run the `PDFSegmenter` Class:**
   - Right-click on the `PDFSegmenter.java` file in the Project Explorer.
   - Select **Run As** > **Java Application**.
   

## Examples of Usage

1. **Basic Example:**
   - To segment a PDF file named `sample.pdf` into sections based on the 3 largest vertical whitespaces:
     - Set `Program arguments` to:
       ```
       ./sample.pdf 3
       ```

2. **Handling Different Scenarios:**
   - **Empty PDF:** If the PDF is empty, it will create a single output file with no segmentation.
   - **No Significant Whitespace:** If no significant whitespace is detected, the output will be a single segment of the original PDF.
   - **Invalid File Path:** If the file path is incorrect or the file does not exist, an error message will be shown.

## Troubleshooting

- **Error: `java.io.FileNotFoundException`**
  - Check if the file path to the PDF is correct and the file exists.

- **Error: `java.lang.NumberFormatException`**
  - Ensure that the number of cuts (`X`) is a positive integer.

- **Error: `java.io.IOException`**
  - Verify that the file is accessible and not corrupted.


## Unit Testing

1. **Run Unit Tests in Eclipse:**
   - Right-click on the `PDFSegmenterTest.java` file in the `test` package.
   - Select **Run As** > **JUnit Test**.
   - The test results will be displayed in the JUnit view.
