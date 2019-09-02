package se.uu.ub.cora.fitnesseintegration;

import java.util.List;

import se.uu.ub.cora.data.DataElement;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecord;

public class MetadataGroupFixture {

	private String nameInData;
	private String childDataGroupName;

	public void setChildNameInData(String nameInData) {
		this.nameInData = nameInData;
	}

	public int numberOfChildrenWithNameInData() {
		int numOfMatchingChildren = 0;
		DataRecord record = RecordHolder.getRecord();
		if (topLevelGroupExists(record)) {
			numOfMatchingChildren = findNumOfMatchingChildren(record);
		}
		return numOfMatchingChildren;
	}

	private int findNumOfMatchingChildren(DataRecord record) {
		DataGroup topLevelDataGroup = record.getDataGroup();
		if (shouldFindChildrenInTopLevelDataGroup()) {
			return getNumberOfMatchingChildren(topLevelDataGroup);
		}
		return possiblyGetNumOfMatchingChildrenFromChildDataGroup(topLevelDataGroup);
	}

	private boolean shouldFindChildrenInTopLevelDataGroup() {
		return childDataGroupName == null || "".equals(childDataGroupName);
	}

	private int possiblyGetNumOfMatchingChildrenFromChildDataGroup(DataGroup topLevelDataGroup) {
		if (childDataGroupExist(topLevelDataGroup)) {
			return getNumOfMatchingChildrenFromChildDataGroup(topLevelDataGroup);
		}
		return 0;
	}

	private boolean childDataGroupExist(DataGroup topLevelDataGroup) {
		return topLevelDataGroup.containsChildWithNameInData(childDataGroupName);
	}

	private int getNumOfMatchingChildrenFromChildDataGroup(DataGroup topLevelDataGroup) {
		DataGroup childDataGroup = topLevelDataGroup
				.getFirstGroupWithNameInData(childDataGroupName);
		return getNumberOfMatchingChildren(childDataGroup);
	}

	private boolean topLevelGroupExists(DataRecord record) {
		return record != null && record.getDataGroup() != null;
	}

	private int getNumberOfMatchingChildren(DataGroup topLevelDataGroup) {
		List<DataElement> matchingChildren = topLevelDataGroup
				.getAllChildrenWithNameInData(nameInData);
		return matchingChildren.size();
	}

	public void setChildDataGroup(String childDataGroup) {
		this.childDataGroupName = childDataGroup;
	}

}
