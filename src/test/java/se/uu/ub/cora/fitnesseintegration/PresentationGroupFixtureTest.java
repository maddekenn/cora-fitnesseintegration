/*
 * Copyright 2018 Uppsala University Library
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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecord;

public class PresentationGroupFixtureTest {

	PresentationGroupFixture fixture;
	private DataGroup topLevelDataGroup;

	@BeforeMethod
	public void setUp() {
		fixture = new PresentationGroupFixture();

		topLevelDataGroup = createTopLevelDataGroup();

		DataRecord record = DataRecord.withDataGroup(topLevelDataGroup);
		RecordHolder.setRecord(record);

	}

	private DataGroup createTopLevelDataGroup() {
		DataGroup dataGroup = DataGroup.withNameInData("presentation");
		DataGroup childReferences = DataGroup.withNameInData("childReferences");
		DataGroup childReference = createChildReferenceWithRepeatIdRecordTypeRecordIdAndType("0",
				"presentationGroup", "somePresentationPGroup", "presentation");
		childReferences.addChild(childReference);
		dataGroup.addChild(childReferences);
		return dataGroup;
	}

	private DataGroup createChildReferenceWithRepeatIdRecordTypeRecordIdAndType(String repeatId,
			String linkedRecordType, String linkedRecordId, String typeAttribute) {
		DataGroup childReference = DataGroup.withNameInData("childReference");
		childReference.setRepeatId(repeatId);

		DataGroup refGroup = DataGroup.withNameInData("refGroup");
		DataGroup ref = DataGroup.withNameInData("ref");
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		ref.addAttributeByIdWithValue("type", typeAttribute);
		refGroup.addChild(ref);
		childReference.addChild(refGroup);

		return childReference;
	}

	@Test
	public void testNumOfChildrenWithoutRecord() throws Exception {
		RecordHolder.setRecord(null);
		fixture.setLinkedRecordId("somePresentationPGroup");
		fixture.setLinkedRecordType("presentationGroup");
		assertEquals(fixture.numberOfRefs(), 0);
	}

	@Test
	public void testNoTopLevelDataGroup() {
		DataRecord record = DataRecord.withDataGroup(null);
		RecordHolder.setRecord(record);
		fixture.setLinkedRecordId("somePresentationPGroup");
		fixture.setLinkedRecordType("presentationGroup");
		assertEquals(fixture.numberOfRefs(), 0);
	}

	@Test
	public void testNumOfChildrenWithNoChildren() {

		DataGroup dataGroup = DataGroup.withNameInData("presentation");
		DataRecord record = DataRecord.withDataGroup(dataGroup);
		RecordHolder.setRecord(record);
		fixture.setLinkedRecordId("somePresentationPGroup");
		fixture.setLinkedRecordType("presentationGroup");
		assertEquals(fixture.numberOfRefs(), 0);
	}

	@Test
	public void testLinkIsNotPresent() {
		fixture.setLinkedRecordType("presentationGroup");
		fixture.setLinkedRecordId("NOTsomePresentationPGroup");
		assertEquals(fixture.numberOfRefs(), 0);
	}

	@Test
	public void testOneLinkIsPresent() {
		fixture.setLinkedRecordType("presentationGroup");
		fixture.setLinkedRecordId("somePresentationPGroup");
		assertEquals(fixture.numberOfRefs(), 1);
	}

	@Test
	public void testOneLinkIsPresentTwice() {
		DataGroup childReferences = topLevelDataGroup
				.getFirstGroupWithNameInDataAndAttributes("childReferences");
		DataGroup childReference = createChildReferenceWithRepeatIdRecordTypeRecordIdAndType("1",
				"presentationGroup", "somePresentationPGroup", "presentation");
		childReferences.addChild(childReference);
		fixture.setLinkedRecordType("presentationGroup");
		fixture.setLinkedRecordId("somePresentationPGroup");
		assertEquals(fixture.numberOfRefs(), 2);
	}
}
