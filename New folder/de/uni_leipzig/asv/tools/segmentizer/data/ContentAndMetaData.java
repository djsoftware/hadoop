package de.uni_leipzig.asv.tools.segmentizer.data;

public class ContentAndMetaData {

	public ContentAndMetaData(String content, ContentMetaData contentMetaData) {
		this.content = content;
		this.contentMetaData = contentMetaData;
	}

	private final String content;

	public String getContent() {
		return content;
	}

	private final ContentMetaData contentMetaData;

	public ContentMetaData getContentMetaData() {
		return contentMetaData;
	}

}
