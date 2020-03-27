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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CompararerFixtureTest {

	private ComparerFixture fixture;
	private RecordHandlerSpy recordHandler;
	private JsonToDataRecordConverterSpy jsonToDataConverter;

	@BeforeMethod
	public void setUp() {
		SystemUrl.setUrl("http://localhost:8080/therest/");
		fixture = new ComparerFixture();
		fixture.setType("someRecordType");
		recordHandler = new RecordHandlerSpy();
		fixture.setRecordHandler(recordHandler);
	}

	@Test
	public void testInit() {
		fixture = new ComparerFixture();
		assertTrue(fixture.getRecordHandler() instanceof RecordHandlerImp);
	}

	@Test
	public void testReadRecordListAndStoreRecords() {
		fixture.testReadRecordListAndStoreRecords();
		assertTrue(recordHandler.readRecordListWasCalled);

		String expectedUrl = SystemUrl.getUrl() + "rest/record/someRecordType";
		assertEquals(recordHandler.url, expectedUrl);
		assertEquals(fixture.getStoredListAsJson(), recordHandler.jsonToReturn);
	}

	@Test
	public void testListAsJsonIsConverterdAndStoredInDataHolder() {
		fixture.testReadRecordListAndStoreRecords();

	}

	// @Test
	// public void testReadCheckContainResultOK() {
	// jsonToDataConverter = new JsonToDataRecordConverterSpy();
	// String childrenToLookFor = "{\"doesContain\":[{\"textVariable\":\"workoutName\"}]}";
	// setUpFixtureForReadCheckContain(childrenToLookFor);
	//
	// assertEquals(fixture.testReadCheckContain(), "OK");
	// }

	// private void setUpFixtureForReadCheckContain(String childrenToLookFor) {
	// fixture.setJsonHandler(jsonHandler);
	//
	// fixture.setJsonToDataRecordConverter(jsonToDataConverter);
	//
	// fixture.setType("someCheckChildrenOkType");
	// fixture.setId("someId");
	// fixture.setChildren(childrenToLookFor);
	// }

	@Test
	public void testReadCheckContainRecordInList() {
		fixture.testReadRecordListAndStoreRecords();
		fixture.setListIndexToCompareTo(1);
		String result = fixture.testReadFromListCheckContain();
	}

}
