package se.uu.ub.cora.fitnesseintegration;

import java.util.List;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecord;

public class PresentationGroupFixture extends MetadataLinkFixture {

	@Override
	public void setLinkedRecordType(String linkedRecordType) {
		this.linkedRecordType = linkedRecordType;
	}

	@Override
	public void setLinkedRecordId(String linkedRecordId) {
		this.linkedRecordId = linkedRecordId;
	}

	public int numberOfRefs() {
		DataRecord record = RecordHolder.getRecord();
		if (recordHasDataGroup(record)) {
			return possiblyGetNumberOfMatchingChildren(record);
		}
		return 0;
	}

	private int possiblyGetNumberOfMatchingChildren(DataRecord record) {
		DataGroup topLevelDataGroup = record.getDataGroup();
		if (groupHasChildren(topLevelDataGroup)) {
			return getNumberOfMatchingChildren(topLevelDataGroup);
		}
		return 0;
	}

	private int getNumberOfMatchingChildren(DataGroup topLevelDataGroup) {
		List<DataGroup> childReferenceGroups = extractChildReferences(topLevelDataGroup);
		int children = 0;
		for (DataGroup childReference : childReferenceGroups) {

			if (childReferenceMatches(childReference)) {
				children++;
			}
		}
		return children;
	}

	private boolean childReferenceMatches(DataGroup childReference) {
		String childLinkedRecordType = extractValueFromReferenceUsingNameInData(childReference,
				"linkedRecordType");
		String childLinkedRecordId = extractValueFromReferenceUsingNameInData(childReference,
				"linkedRecordId");

		return childReferenceMatchesTypeAndId(childLinkedRecordType, childLinkedRecordId);
	}

	private boolean recordHasDataGroup(DataRecord record) {
		return record != null && record.getDataGroup() != null;
	}

	private boolean groupHasChildren(DataGroup topLevelDataGroup) {
		return topLevelDataGroup.containsChildWithNameInData("childReferences");
	}

	private List<DataGroup> extractChildReferences(DataGroup topLevelDataGroup) {
		DataGroup childReferences = topLevelDataGroup
				.getFirstGroupWithNameInData("childReferences");
		return childReferences.getAllGroupsWithNameInData("childReference");
	}

	@Override
	protected String extractValueFromReferenceUsingNameInData(DataGroup childReference,
			String childNameInData) {
		DataGroup refGroup = childReference.getFirstGroupWithNameInData("refGroup");
		DataGroup ref = refGroup.getFirstGroupWithNameInData("ref");
		return ref.getFirstAtomicValueWithNameInData(childNameInData);
	}
}
