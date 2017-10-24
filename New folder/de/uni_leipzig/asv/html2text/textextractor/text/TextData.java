package de.uni_leipzig.asv.html2text.textextractor.text;

import de.uni_leipzig.asv.html2text.textextractor.helper.Function;
import de.uni_leipzig.asv.html2text.textextractor.helper.Pair;
import java.util.LinkedList;
import java.util.List;

public class TextData {

    private final List<Pair<String, Double>> lines = new LinkedList<Pair<String, Double>>();

    public TextData(final List<String> lines) {
        for (final String line : lines) {
            this.lines.add(Pair.create(line, 0d));
        }
    }

    public void calculateLineRatings(final Function<String, Double> ratingFunction) {
        for (final Pair<String, Double> line : lines) {
            line.setSecond(ratingFunction.evaluate(line.first()));
        }
    }

    public List<Pair<String, Double>> getLinesWithRatings() {
        return lines;
    }

    public String toStringWithRatings() {
        final String CR = "\n";
        final StringBuffer strBuff = new StringBuffer();
        for (final Pair<String, Double> line : lines) {
            strBuff.append(line.second() + ":" + line.first());
            strBuff.append(CR);
        }
        return strBuff.toString();
    }

    @Override
    public String toString() {
        final String CR = "\n";
        final StringBuffer strBuff = new StringBuffer();
        for (final Pair<String, Double> line : lines) {
            strBuff.append(line.first());
            strBuff.append(CR);
        }
        return strBuff.toString();
    }
}
