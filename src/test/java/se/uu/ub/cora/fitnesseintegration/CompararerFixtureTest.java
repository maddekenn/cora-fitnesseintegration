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
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CompararerFixtureTest {

	private ComparerFixture fixture;
	private RecordHandlerSpy recordHandler;
	private JsonToDataRecordConverterSpy jsonToDataConverter;
	private JsonParserSpy jsonParser;
	private JsonHandlerImp jsonHandler;

	@BeforeMethod
	public void setUp() {
		SystemUrl.setUrl("http://localhost:8080/therest/");
		jsonParser = new JsonParserSpy();
		jsonHandler = JsonHandlerImp.usingJsonParser(jsonParser);
		fixture = new ComparerFixture();
		fixture.setType("someRecordType");
		recordHandler = new RecordHandlerSpy();
		fixture.setRecordHandler(recordHandler);
		fixture.setJsonHandler(jsonHandler);
		jsonToDataConverter = new JsonToDataRecordConverterSpy();
		fixture.setJsonToDataRecordConverter(jsonToDataConverter);
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
	public void testListAsJsonIsConvertedAndStoredInDataHolder() {
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

		String jsonListFromRecordHandler = recordHandler.jsonToReturn;
		String jsonListSentToParser = jsonParser.jsonStringsSentToParser.get(0);
		assertEquals(jsonListSentToParser, jsonListFromRecordHandler);

		JsonObjectSpy listObjectFromSpy = jsonParser.jsonObjectSpies.get(0);
		assertEquals(listObjectFromSpy.getValueKeys.get(0), "dataList");

		JsonObjectSpy dataList = listObjectFromSpy.getValueObjectsReturned.get(0);
		assertEquals(dataList.getValueKeys.get(0), "data");

		JsonArraySpy data = dataList.getValueArraysReturned.get(0);
		IteratorSpy returnedIterator = data.returnedIterator;
		assertTrue(returnedIterator.hasNextWasCalled);

		List<JsonObjectSpy> objectsReturnedFromNext = returnedIterator.objectsReturnedFromNext;
		assertSame(jsonToDataConverter.jsonObjects.get(0), objectsReturnedFromNext.get(0));
		assertSame(jsonToDataConverter.jsonObjects.get(1), objectsReturnedFromNext.get(1));

		// TODO: kolla att resultatet från convertern läggs i listan i dataHolder

	}

}
