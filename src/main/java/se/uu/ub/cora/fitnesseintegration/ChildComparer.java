/*
 * Copyright 2020 Uppsala University Library
 *
 * This file is part of Cora.
 *
 *     Cora is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Cora is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Cora.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.uu.ub.cora.fitnesseintegration;

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.clientdata.ClientDataElement;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecordLink;
import se.uu.ub.cora.clientdata.ClientDataResourceLink;

public class ChildComparer {

	public List<String> allChildrenExist(ClientDataGroup existingTopDataGroup,
			ClientDataGroup expectedDataGroup) {

		List<String> errorList = new ArrayList<>();
		for (ClientDataElement expectedChild : expectedDataGroup.getChildren()) {
			checkExpectedChild(expectedChild, existingTopDataGroup, errorList);
		}
		return errorList;

	}

	private void checkExpectedChild(ClientDataElement expectedChild,
			ClientDataGroup existingTopDataGroup, List<String> errorList) {
		String expectedNameInData = expectedChild.getNameInData();
		if (noChildWithNameInDataInGroup(expectedNameInData, existingTopDataGroup)) {
			addChildNotDoesExistError(errorList, expectedNameInData);
		} else {
			ClientDataElement existingChild = existingTopDataGroup
					.getFirstChildWithNameInData(expectedNameInData);
			checkExistingChild(expectedChild, existingChild, errorList);
		}
	}

	private boolean noChildWithNameInDataInGroup(String expectedNameInData,
			ClientDataGroup existingTopDataGroup) {
		return !existingTopDataGroup.containsChildWithNameInData(expectedNameInData);
	}

	private void addChildNotDoesExistError(List<String> errorList, String expectedNameInData) {
		errorList.add("child with nameInData " + expectedNameInData + " does not exist");
	}

	private void checkExistingChild(ClientDataElement expectedChild,
			ClientDataElement existingChild, List<String> errorList) {
		if (childrenAreDifferentTypes(expectedChild, existingChild)) {
			addChildNotDoesExistError(errorList, expectedChild.getNameInData());
		} else {
			checkExpectedAndExistingOfSameType(existingChild, errorList, expectedChild);
		}
	}

	private boolean childrenAreDifferentTypes(ClientDataElement expectedChild,
			ClientDataElement existingChild) {
		return !getType(expectedChild).equals(getType(existingChild));
	}

	private void checkExpectedAndExistingOfSameType(ClientDataElement existingChild,
			List<String> errorList, ClientDataElement expectedChild) {
		if (dataElementIsGroup(expectedChild)) {
			List<ClientDataElement> childrenFromExpectedGroup = ((ClientDataGroup) expectedChild)
					.getChildren();
			checkExpectedGroupChildren(childrenFromExpectedGroup, existingChild, errorList);
		}
	}

	private void checkExpectedGroupChildren(List<ClientDataElement> childrenFromExpectedGroup,
			ClientDataElement existingChild, List<String> errorList) {
		for (ClientDataElement expectedDataChildChild : childrenFromExpectedGroup) {
			if (childFromExpectedExistsInExistingChild(expectedDataChildChild, existingChild)) {
				ClientDataElement existingChildChild = ((ClientDataGroup) existingChild)
						.getFirstChildWithNameInData(expectedDataChildChild.getNameInData());
				checkExistingChild(expectedDataChildChild, existingChildChild, errorList);
			} else {
				addChildNotDoesExistError(errorList, expectedDataChildChild.getNameInData());

			}
		}
	}

	private boolean childFromExpectedExistsInExistingChild(ClientDataElement expectedDataChildChild,
			ClientDataElement existingChild) {
		return ((ClientDataGroup) existingChild)
				.containsChildWithNameInData(expectedDataChildChild.getNameInData());
	}

	public String getType(ClientDataElement dataElement) {
		if (dataElement instanceof ClientDataRecordLink) {
			return "dataRecordLink";
		}
		if (dataElement instanceof ClientDataResourceLink) {
			return "resourceLink";
		}
		if (dataElementIsGroup(dataElement)) {
			return "dataGroup";
		}
		return "dataAtomic";
	}

	private boolean dataElementIsGroup(ClientDataElement dataElement) {
		return dataElement instanceof ClientDataGroup;
	}

}
