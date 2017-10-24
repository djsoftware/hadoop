package de.uni_leipzig.asv.html2text.textextractor.main;

import de.uni_leipzig.asv.html2text.textextractor.helper.FileHelper;
import de.uni_leipzig.asv.html2text.textextractor.helper.Function;
import de.uni_leipzig.asv.html2text.textextractor.helper.Pair;
import de.uni_leipzig.asv.html2text.textextractor.text.TextData;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TextExtractor {


    private class SimpleRatingFunction implements Function<String, Double> {

        private final static int MIN_LINE_LENGTH = 20;

        public String[] badPrefixes = new String[]{"*", "o ", "+ ", "<a ","<meta "};
        public String[] badStrs = new String[]{"{", "}", "if(", "src=\"", "|","·",
                "【","】","---","<",">", "<a", "href=\"", "http://","\t","┊","/"};

        private int getBadStrsCount(final String str) {
            int badCount = 0;
            for (final String badStr : badStrs) {
                int pos = 0;
                while ((pos = str.indexOf(badStr, pos)) != -1) {
                    pos++;
                    badCount++;
                }
            }
            return badCount;
        }

        public Double evaluate(final String input) {
            double result = 0;

            if (input.length() > MIN_LINE_LENGTH) {
           
                final String trimmedInput = input.trim();

                double badContent = 1;
                for (final String badPrefix : badPrefixes) {
                    if (trimmedInput.startsWith(badPrefix)) {
                        badContent = -1;
                    }
                }

                if (getBadStrsCount(trimmedInput) > 1) {
                    badContent = -1;
                }

                final String[] splittedInput = trimmedInput.split(" ");
                final double tokenCountRating = splittedInput.length;
                result = badContent * tokenCountRating;

            }

            return result;
        }
    }

    public String doExtraction(final List<String> lines) {
        return doExtraction(new TextData(lines));
    }

    public String doExtraction(final String str) {
        return doExtraction(new TextData(Arrays.asList(str.split("\n"))));
    }

    private String doExtraction(final TextData textData) {
        textData.calculateLineRatings(new SimpleRatingFunction());
//        System.out.println(textData.toStringWithRatings());
        final TextData filteredBlock = filterBlock(textData);
        return filteredBlock.toString();
    }

    private TextData filterBlock(final TextData textData) {
        final List<String> goodLines = new LinkedList<String>();
        for (final Pair<String, Double> ratedLine : textData.getLinesWithRatings()) {
            if (ratedLine.second() > 0) {
                goodLines.add(ratedLine.first());
            }
        }
        return new TextData(goodLines);
    }


       

    // private TextData findBestBlock(final TextData textData) {
    // final int includeMaxNegLines = 1;
    //
    // double bestRating = -99999;
    // final List<String> bestLines = new LinkedList<String>();
    //
    // int curNegLines = 0;
    // double curRating = 0;
    // final List<String> curLines = new LinkedList<String>();
    //
    // for (final Pair<String, Double> ratedLine :
    // textData.getLinesWithRatings()) {
    // if (ratedLine.second() > 0) {
    // curLines.add(ratedLine.first());
    // curRating = curRating + ratedLine.second();
    // } else {
    // curNegLines++;
    // if (curNegLines > includeMaxNegLines) {
    // if (curLines.size() > bestLines.size() && bestRating < curRating) {
    // bestLines.clear();
    // bestLines.addAll(curLines);
    // bestRating = curRating;
    // }
    // curLines.clear();
    // curRating = 0;
    // curNegLines = 0;
    // } else {
    // curLines.add(ratedLine.first());
    // curRating = curRating + ratedLine.second();
    // }
    // }
    // }
    //
    // if (curLines.size() > bestLines.size() && bestRating < curRating) {
    // return new TextData(curLines);
    // }
    // return new TextData(bestLines);
    // }
    public static void main(final String[] args) {
        final TextExtractor textExctractor = new TextExtractor();
        // final String[] fileNames = new String[] { "a.txt", "b.txt", "d.txt",
        // "e.txt", "f.txt", "g.txt", "h.txt" };
        final String[] fileNames = new String[]{"a.txt"};
        for (final String fileName : fileNames) {
            try {
                final List<String> loadText = FileHelper.loadText(new File("txt/" + fileName));
                System.out.println("--------------" + fileName + "--------------");
                System.out.println(textExctractor.doExtraction(loadText));
                System.out.println("\n");
            } catch (final IOException e) {
                System.err.println(e);
            }
        }
    }
}
