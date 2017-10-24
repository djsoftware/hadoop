package de.uni_leipzig.asv.tools.segmentizer.data;

public class ContentMetaData {

	public ContentMetaData(final String metadataContent) {
		super();
		this.metadataContent = metadataContent;
	}

	private final String metadataContent;

	public String getMetadataContent() {
		return metadataContent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((metadataContent == null) ? 0 : metadataContent.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ContentMetaData other = (ContentMetaData) obj;
		if (metadataContent == null) {
			if (other.metadataContent != null) {
				return false;
			}
		} else if (!metadataContent.equals(other.metadataContent)) {
			return false;
		}
		return true;
	}

}
