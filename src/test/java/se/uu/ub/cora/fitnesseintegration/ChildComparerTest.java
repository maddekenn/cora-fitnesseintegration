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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecordLink;
import se.uu.ub.cora.clientdata.ClientDataResourceLink;

public class ChildComparerTest {

	private ClientDataGroup bookGroup;

	@BeforeMethod
	public void setUp() {
		bookGroup = createBasicDataGroup();
	}

	private ClientDataGroup createBasicDataGroup() {
		ClientDataGroup bookGroup = ClientDataGroup.withNameInData("book");
		bookGroup.addChild(ClientDataGroup.withNameInData("recordInfo"));
		return bookGroup;
	}

	@Test
	public void testDoContainGroupOnTopLevel() {
		ClientDataGroup expectedGroup = createBookDataGroupWithRecordInfo();

		ChildComparer childComparer = new ChildComparer();
		assertTrue(childComparer.allChildrenExist(bookGroup, expectedGroup).isEmpty());
	}

	@Test
	public void testDoContainGroupOnTopLevelButAlsoDataAtomic() {
		bookGroup.addChild(
				ClientDataAtomic.withNameInDataAndValue("recordInfo", "recordInfoAtomic"));

		ClientDataGroup expectedGroup = ClientDataGroup.withNameInData("book");
		expectedGroup.addChild(
				ClientDataAtomic.withNameInDataAndValue("recordInfo", "recordInfoAtomic"));

		ChildComparer childComparer = new ChildComparer();
		assertTrue(childComparer.allChildrenExist(bookGroup, expectedGroup).isEmpty());
	}

	@Test
	public void testDoContainRecordLinkOnTopLevel() {
		ClientDataGroup bookGroup = ClientDataGroup.withNameInData("book");
		bookGroup.addChild(ClientDataRecordLink.withNameInData("someLink"));

		ClientDataGroup expectedGroup = ClientDataGroup.withNameInData("book");
		ClientDataRecordLink childRecordLink = ClientDataRecordLink.withNameInData("someLink");
		expectedGroup.addChild(childRecordLink);

		ChildComparer childComparer = new ChildComparer();
		assertTrue(childComparer.allChildrenExist(bookGroup, expectedGroup).isEmpty());
	}

	@Test
	public void testOneMissingOnTopLevel() {
		ClientDataGroup childrenToCompareTo = ClientDataGroup.withNameInData("book");
		childrenToCompareTo.addChild(ClientDataGroup.withNameInData("NOTrecordInfo"));

		ChildComparer childComparer = new ChildComparer();
		List<String> errorMessage = childComparer.allChildrenExist(bookGroup, childrenToCompareTo);
		assertEquals(errorMessage.size(), 1);
		assertEquals(errorMessage.get(0), "child with nameInData NOTrecordInfo does not exist");
	}

	@Test
	public void testTwoMissingOnTopLevel() {
		ClientDataGroup childrenToCompareTo = ClientDataGroup.withNameInData("book");
		childrenToCompareTo.addChild(ClientDataGroup.withNameInData("NOTrecordInfo"));
		childrenToCompareTo.addChild(ClientDataGroup.withNameInData("someOtherMissingDataGroup"));

		ChildComparer childComparer = new ChildComparer();
		List<String> errorMessage = childComparer.allChildrenExist(bookGroup, childrenToCompareTo);
		assertEquals(errorMessage.size(), 2);
		assertEquals(errorMessage.get(0), "child with nameInData NOTrecordInfo does not exist");
		assertEquals(errorMessage.get(1),
				"child with nameInData someOtherMissingDataGroup does not exist");
	}

	@Test
	public void testDoContainOnTopLevelButWrongElementType() {
		ClientDataGroup childrenToCompareTo = ClientDataGroup.withNameInData("book");
		childrenToCompareTo.addChild(
				ClientDataAtomic.withNameInDataAndValue("recordInfo", "atomicRecordInfo"));

		ChildComparer childComparer = new ChildComparer();
		List<String> errorMessages = childComparer.allChildrenExist(bookGroup, childrenToCompareTo);
		assertEquals(errorMessages.size(), 1);
		assertEquals(errorMessages.get(0), "child with nameInData recordInfo does not exist");
	}

	@Test
	public void testDoContainOnTopLevelButWrongElementTypeRecordLinkVsDataGroup() {
		ClientDataGroup childrenToCompareTo = ClientDataGroup.withNameInData("book");
		childrenToCompareTo.addChild(ClientDataRecordLink.withNameInData("recordInfo"));

		ChildComparer childComparer = new ChildComparer();
		List<String> errorMessages = childComparer.allChildrenExist(bookGroup, childrenToCompareTo);
		assertEquals(errorMessages.size(), 1);
		assertEquals(errorMessages.get(0), "child with nameInData recordInfo does not exist");
	}

	@Test
	public void testDoContainOnTopLevelButWrongElementTypeResourceLinkVsDataGroup() {
		ClientDataGroup childrenToCompareTo = ClientDataGroup.withNameInData("book");
		childrenToCompareTo.addChild(ClientDataResourceLink.withNameInData("recordInfo"));

		ChildComparer childComparer = new ChildComparer();
		List<String> errorMessages = childComparer.allChildrenExist(bookGroup, childrenToCompareTo);
		assertEquals(errorMessages.size(), 1);
		assertEquals(errorMessages.get(0), "child with nameInData recordInfo does not exist");
	}

	private ClientDataGroup createBookDataGroupWithRecordInfo() {
		ClientDataGroup childrenToCompareTo = ClientDataGroup.withNameInData("book");
		childrenToCompareTo.addChild(ClientDataGroup.withNameInData("recordInfo"));
		return childrenToCompareTo;
	}

	@Test
	public void testDoContainOneLevelDown() {
		ClientDataGroup existingRecordInfo = bookGroup.getFirstGroupWithNameInData("recordInfo");
		existingRecordInfo.addChild(ClientDataAtomic.withNameInDataAndValue("id", "someId"));

		ClientDataGroup expectedGroup = createBookDataGroupWithRecordInfo();
		ClientDataGroup expectedRecordInfo = expectedGroup
				.getFirstGroupWithNameInData("recordInfo");
		expectedRecordInfo.addChild(ClientDataAtomic.withNameInDataAndValue("id", "someId"));

		ChildComparer childComparer = new ChildComparer();
		assertTrue(childComparer.allChildrenExist(bookGroup, expectedGroup).isEmpty());
	}

	@Test
	public void testDoContainOneOnTopLevelButNotTheOther() {
		ClientDataGroup expectedGroup = createBookDataGroupWithRecordInfo();

		expectedGroup.addChild(
				ClientDataAtomic.withNameInDataAndValue("someChild", "someValueForChild"));

		ChildComparer childComparer = new ChildComparer();
		List<String> errorMessages = childComparer.allChildrenExist(bookGroup, expectedGroup);

		assertEquals(errorMessages.size(), 1);
		assertEquals(errorMessages.get(0), "child with nameInData someChild does not exist");
	}

	@Test
	public void testDoNotContainOneLevelDown() {
		ClientDataGroup expectedGroup = createBookDataGroupWithRecordInfo();
		ClientDataGroup recordInfo = expectedGroup.getFirstGroupWithNameInData("recordInfo");
		recordInfo.addChild(ClientDataAtomic.withNameInDataAndValue("id", "someId"));
		ChildComparer childComparer = new ChildComparer();
		List<String> errorMessages = childComparer.allChildrenExist(bookGroup, expectedGroup);

		assertEquals(errorMessages.size(), 1);
		assertEquals(errorMessages.get(0), "child with nameInData id does not exist");

	}

}
