/*
 * Copyright (C) 2017 Universidade da Coruña
 * 
 * This file is part of MarDRe.
 * 
 * MarDRe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * MarDRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with MarDRe. If not, see <http://www.gnu.org/licenses/>.
 */
package es.udc.gac.mardre;

import java.io.IOException;

import org.apache.hadoop.io.Text;

import es.udc.gac.mardre.util.LineReader;

public class FastQRecordReader extends SequenceRecordReader {

	private static final int NUMBER_OF_LINES_PER_READ = 4;
	private static final Text FASTQ_COMMENT_LINE = new Text("+" + LineReader.LF);

	private Text newLine;
	private Text temp;
	private long numReads;

	public FastQRecordReader() {
		super();
		newLine = new Text();
		temp = new Text();
		numReads = start;
	}

	@Override
	public boolean nextKeyValue() throws IOException {
		int i = 0;
		long firstRead = 0, secondRead = 0;
		boolean found = false;
		value.clear();

		//System.out.println("nextKeyValue: start "+start+", end "+end+", splitPos "+getSplitPosition());

		if (isSplitFinished())
			return false;

		while (true) {
			firstRead = readLine(newLine);
			i++;

			if (firstRead == 0) //EOF
				return false;

			if (found && i == NUMBER_OF_LINES_PER_READ) {
				//System.out.println("nextKeyValue: last line and starting '@' has been previously found");
				numReads++;
				key.set(start+numReads);
				value.append(newLine.getBytes(), 0, newLine.getLength());
				break;
			}

			if (newLine.charAt(0) == '@') {
				//System.out.println("nextKeyValue: starting '@' has been found at line "+i);
				found = true;

				secondRead = readLine(temp);

				if (secondRead == 0) //EOF
					return false;

				if (temp.charAt(0) != '@') {
					i = 2;
					//Trim spaces in sequence name
					LineReader.trim(newLine, ' ', 1);
					newLine.append(temp.getBytes(), 0, temp.getLength());
				} else {
					i = 1;
					//Trim spaces in sequence name
					LineReader.trim(temp, ' ', 1);
					value.append(temp.getBytes(), 0, temp.getLength());
					continue;
				}
			}

			if (found) {
				if (i != 3)
					value.append(newLine.getBytes(), 0, newLine.getLength());
				else
					value.append(FASTQ_COMMENT_LINE.getBytes(), 0, FASTQ_COMMENT_LINE.getLength());
				continue;
			}

			if (i == NUMBER_OF_LINES_PER_READ) {
				//System.out.println("nextKeyValue: last line and no starting '@' has been found (discard previous data)");
				i = 0;
			}
		}

		//System.out.println("nextKeyValue: start "+start+", end "+end+", splitPos "+getSplitPosition());

		return true;
	}
}
