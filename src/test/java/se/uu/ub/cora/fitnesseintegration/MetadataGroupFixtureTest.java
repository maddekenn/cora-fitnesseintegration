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

public class MetadataGroupFixtureTest {

	MetadataGroupFixture fixture;

	@BeforeMethod
	public void setUp() {
		fixture = new MetadataGroupFixture();

		DataGroup topLevelDataGroup = createTopLevelDataGroup();

		DataRecord record = DataRecord.withDataGroup(topLevelDataGroup);
		RecordHolder.setRecord(record);

	}

	private DataGroup createTopLevelDataGroup() {
		DataGroup topLevelDataGroup = DataGroup.withNameInData("testStudentThesis");
		topLevelDataGroup.addChild(DataAtomic.withNameInDataAndValue("testTitle", "a title"));
		return topLevelDataGroup;
	}

	@Test
	public void testNumOfChildrenWithoutRecord() throws Exception {
		RecordHolder.setRecord(null);
		fixture.setChildNameInData("testTitle");
		assertEquals(fixture.numberOfChildrenWithNameInData(), 0);
	}

	@Test
	public void testNoTopLevelDatagroup() {
		DataRecord record = DataRecord.withDataGroup(null);
		RecordHolder.setRecord(record);
		fixture.setChildNameInData("testTitle");
		assertEquals(fixture.numberOfChildrenWithNameInData(), 0);
	}

	@Test
	public void testNoChildPresentFirstLevelNoChildDataGroupSet() {
		DataGroup topLevelDataGroup = DataGroup.withNameInData("testStudentThesis");
		DataRecord record = DataRecord.withDataGroup(topLevelDataGroup);
		RecordHolder.setRecord(record);
		fixture.setChildNameInData("testTitle");
		assertEquals(fixture.numberOfChildrenWithNameInData(), 0);
	}

	@Test
	public void testNoChildPresentFirstLevelEmptyNameForChildDataGroup() {
		fixture.setChildDataGroup("");
		fixture.setChildNameInData("testTitle");
		assertEquals(fixture.numberOfChildrenWithNameInData(), 1);
	}

	@Test
	public void testNumOfChildrenNoMatchingChildFirstLevel() {
		fixture.setChildNameInData("NOTtestTitle");
		assertEquals(fixture.numberOfChildrenWithNameInData(), 0);
	}

	@Test
	public void testOneChildWithMatchingNameInDataFirstLevel() {
		fixture.setChildNameInData("testTitle");
		assertEquals(fixture.numberOfChildrenWithNameInData(), 1);
	}

	@Test
	public void testChildDataGroupDoesNotExist() {
		DataGroup topLevelDataGroup = DataGroup.withNameInData("testStudentThesis");
		DataRecord record = DataRecord.withDataGroup(topLevelDataGroup);
		RecordHolder.setRecord(record);

		fixture.setChildDataGroup("recordInfo");
		fixture.setChildNameInData("testTitle");
		assertEquals(fixture.numberOfChildrenWithNameInData(), 0);
	}

	@Test
	public void testNoChildPresentSecondLevel() {
		DataGroup topLevelDataGroup = DataGroup.withNameInData("testStudentThesis");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		topLevelDataGroup.addChild(recordInfo);
		DataRecord record = DataRecord.withDataGroup(topLevelDataGroup);
		RecordHolder.setRecord(record);

		fixture.setChildDataGroup("recordInfo");
		fixture.setChildNameInData("testTitle");
		assertEquals(fixture.numberOfChildrenWithNameInData(), 0);
	}

	@Test
	public void testNoMatchingChildSecondLevel() {
		DataGroup topLevelDataGroup = DataGroup.withNameInData("testStudentThesis");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		topLevelDataGroup.addChild(recordInfo);
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("NOTtestTitle", "Not a title"));
		DataRecord record = DataRecord.withDataGroup(topLevelDataGroup);
		RecordHolder.setRecord(record);

		fixture.setChildDataGroup("recordInfo");
		fixture.setChildNameInData("testTitle");
		assertEquals(fixture.numberOfChildrenWithNameInData(), 0);
	}

	@Test
	public void testOneChildWithMatchingNameInDataSecondLevel() {
		DataGroup topLevelDataGroup = DataGroup.withNameInData("testStudentThesis");
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		topLevelDataGroup.addChild(recordInfo);
		recordInfo.addChild(DataAtomic.withNameInDataAndValue("testTitle", "A title"));
		DataRecord record = DataRecord.withDataGroup(topLevelDataGroup);
		RecordHolder.setRecord(record);

		fixture.setChildDataGroup("recordInfo");
		fixture.setChildNameInData("testTitle");
		assertEquals(fixture.numberOfChildrenWithNameInData(), 1);
	}
}
