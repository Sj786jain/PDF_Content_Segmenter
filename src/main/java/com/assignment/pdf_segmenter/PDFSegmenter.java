package com.assigment.pdf_segmenter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PDFSegmenter extends PDFTextStripper {

    // Inner class to represent whitespace information
    static class Whitespace {
        int page;
        float yPosition;
        float height;

        Whitespace(int page, float yPosition, float height) {
            this.page = page;
            this.yPosition = yPosition;
            this.height = height;
        }

        public int getPage() {
            return page;
        }

        public float getYPosition() {
            return yPosition;
        }

        public float getHeight() {
            return height;
        }
    }

    private List<Whitespace> whitespaceList = new ArrayList<>();

    public PDFSegmenter() throws IOException {
        super.setSortByPosition(true); // Sort text positions by their position in the document
    }

    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        // Iterate over the text positions to find vertical whitespace
        for (int i = 1; i < textPositions.size(); i++) {
            TextPosition prev = textPositions.get(i - 1);
            TextPosition current = textPositions.get(i);

            float verticalSpace = current.getY() - prev.getY();

            // If the vertical space exceeds a threshold, consider it significant
            if (verticalSpace > 10) {  // Adjust this threshold as needed
                whitespaceList.add(new Whitespace(getCurrentPageNo(), prev.getY(), verticalSpace));
            }
        }
    }

    public List<Whitespace> findSignificantWhitespaces(int cuts) {
        // Sort whitespaces by height in descending order and return the top 'cuts' number
        whitespaceList.sort(Comparator.comparing(Whitespace::getHeight).reversed());
        return whitespaceList.subList(0, Math.min(cuts, whitespaceList.size()));
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java PDFSegmenter <input_pdf> <number_of_cuts>");
            return;
        }

        String sourcePDF = args[0];
        int cuts;
        try {
            cuts = Integer.parseInt(args[1]);
            if (cuts <= 0) {
                throw new NumberFormatException("Number of cuts must be positive.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of cuts: " + e.getMessage());
            return;
        }

        File pdfFile = new File(sourcePDF);
        if (!pdfFile.exists()) {
            System.out.println("The specified PDF file does not exist.");
            return;
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFSegmenter segmenter = new PDFSegmenter();
            segmenter.getText(document);  // Process the PDF to populate whitespaceList

            List<Whitespace> significantWhitespaces = segmenter.findSignificantWhitespaces(cuts);

            significantWhitespaces.sort(Comparator
                .comparingInt(Whitespace::getPage)
                .thenComparing(Whitespace::getYPosition));

            int startPage = 1;
            for (Whitespace ws : significantWhitespaces) {
                PDDocument part = new PDDocument();
                for (int i = startPage; i <= ws.page; i++) {
                    part.addPage(document.getPage(i - 1));
                }
                String outputFileName = "segment_" + startPage + "_to_" + ws.page + ".pdf";
                part.save(new File(outputFileName));
                part.close();
                System.out.println("Saved: " + outputFileName);
                startPage = ws.page + 1;
            }

            // Save remaining pages as the last segment
            if (startPage <= document.getNumberOfPages()) {
                PDDocument part = new PDDocument();
                for (int i = startPage; i <= document.getNumberOfPages(); i++) {
                    part.addPage(document.getPage(i - 1));
                }
                String outputFileName = "segment_" + startPage + "_to_" + document.getNumberOfPages() + ".pdf";
                part.save(new File(outputFileName));
                part.close();
                System.out.println("Saved: " + outputFileName);
            }

            System.out.println("PDF segmentation complete!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
