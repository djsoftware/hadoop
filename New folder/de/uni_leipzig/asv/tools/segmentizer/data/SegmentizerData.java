package de.uni_leipzig.asv.tools.segmentizer.data;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class SegmentizerData {

	private final Queue<ContentAndMetaData> data = new ConcurrentLinkedQueue<ContentAndMetaData>();

	public void addData(final String content, final ContentMetaData contentMetaData) {
		data.offer(new ContentAndMetaData(content, contentMetaData));
	}

	public boolean dataAvailable() {
		return !data.isEmpty();
	}

	public int availableDataLines() {
		return data.size();
	}

	public ContentAndMetaData fetchData() {
		return data.poll();
	}

	private boolean isFinished = false;

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(final boolean isFinished) {
		this.isFinished = isFinished;
	}

	public boolean isSourceSwitch(final ContentMetaData contentMetaData) {
		if (!dataAvailable()) {
			return false;
		}
		return !nullSafeEquals(data.element().getContentMetaData(), contentMetaData);
	}

	private boolean nullSafeEquals(final Object o1, final Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		}
		if (o1 == null) {
			return o2.equals(o1);
		}
		return o1.equals(o2);
	}
}
