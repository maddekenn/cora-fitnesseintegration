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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecord;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonString;

public class MetadataLinkFixtureTest {

	MetadataLinkFixture fixture;
	private HttpHandlerFactorySpy httpHandlerFactorySpy;
	private JsonToDataConverterFactorySpy jsonToDataConverterFactory;

	@BeforeMethod
	public void setUp() {
		SystemUrl.setUrl("http://localhost:8080/therest/");
		AuthTokenHolder.setAdminAuthToken("someAdminToken");
		DependencyProvider.setHttpHandlerFactoryClassName(
				"se.uu.ub.cora.fitnesseintegration.HttpHandlerFactorySpy");
		DependencyProvider.setJsonToDataFactoryClassName(
				"se.uu.ub.cora.fitnesseintegration.JsonToDataConverterFactorySpy");
		httpHandlerFactorySpy = (HttpHandlerFactorySpy) DependencyProvider.getHttpHandlerFactory();
		jsonToDataConverterFactory = (JsonToDataConverterFactorySpy) DependencyProvider
				.getJsonToDataConverterFactory();
		fixture = new MetadataLinkFixture();

		DataGroup topLevelDataGroup = createTopLevelDataGroup();

		DataRecord record = DataRecord.withDataGroup(topLevelDataGroup);
		RecordHolder.setRecord(record);

	}

	private DataGroup createTopLevelDataGroup() {
		DataGroup topLevelDataGroup = DataGroup.withNameInData("metadata");
		DataGroup childReferences = DataGroup.withNameInData("childReferences");
		DataGroup childReference = createChildReferenceWithRepeatIdRecordTypeAndRecordId("0",
				"metadataGroup", "someRecordId", "0", "X");
		childReferences.addChild(childReference);
		topLevelDataGroup.addChild(childReferences);
		return topLevelDataGroup;
	}

	private DataGroup createChildReferenceWithRepeatIdRecordTypeAndRecordId(String repeatId,
			String linkedRecordType, String linkedRecordId, String repeatMin, String repeatMax) {
		DataGroup childReference = DataGroup.withNameInData("childReference");
		childReference.setRepeatId(repeatId);
		DataGroup ref = DataGroup.withNameInData("ref");
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		ref.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		childReference.addChild(ref);
		childReference.addChild(DataAtomic.withNameInDataAndValue("repeatMin", repeatMin));
		childReference.addChild(DataAtomic.withNameInDataAndValue("repeatMax", repeatMax));
		return childReference;
	}

	@Test
	public void testNameInData() {
		fixture.setAuthToken("someToken");
		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("someRecordId");

		assertCorrectHttpHandler();

		String nameInData = fixture.getNameInData();
		assertEquals(nameInData, "someNameInData");

		assertTrue(fixture.getJsonToRecordDataConverter() instanceof JsonToDataRecordConverter);
		JsonToDataConverterSpy converterSpy = (JsonToDataConverterSpy) jsonToDataConverterFactory.factored;

		assertTrue(converterSpy.toInstanceWasCalled);

		JsonObject jsonObject = (JsonObject) converterSpy.jsonValue;
		assertJsonObjectFromReadRecordIsSentToConverter(jsonObject);

	}

	private void assertCorrectHttpHandler() {
		assertEquals(httpHandlerFactorySpy.httpHandlerSpy.requestMetod, "GET");
		assertEquals(httpHandlerFactorySpy.urlString,
				"http://localhost:8080/therest/rest/record/metadataGroup/someRecordId");
		assertEquals(httpHandlerFactorySpy.httpHandlerSpy.requestProperties.get("authToken"),
				"someToken");
	}

	private void assertJsonObjectFromReadRecordIsSentToConverter(JsonObject jsonObject) {
		JsonString name = jsonObject.getValueAsJsonString("name");
		assertEquals(name.getStringValue(), "metadata");

		assertNotNull(jsonObject.getValue("children"));
		assertNotNull(jsonObject.getValue("attributes"));
		assertEquals(jsonObject.entrySet().size(), 3);
	}

	@Test
	public void testNoMatchingChild() {
		DataGroup topLevelDataGroup = DataGroup.withNameInData("metadata");
		DataRecord record = DataRecord.withDataGroup(topLevelDataGroup);
		RecordHolder.setRecord(record);
		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("someRecordId");
		assertEquals(fixture.getRepeatMin(), "not found");
		assertEquals(fixture.getNameInData(), "not found");
	}

	@Test
	public void testNoTopLevelDatagroupInRecord() {
		DataRecord record = DataRecord.withDataGroup(null);
		RecordHolder.setRecord(record);
		fixture.setLinkedRecordId("someRecordId");
		fixture.setLinkedRecordType("metadataGroup");
		assertEquals(fixture.getRepeatMin(), "not found");
	}

	private void createAndAddSecondChild() {
		DataRecord record = RecordHolder.getRecord();
		DataGroup clientDataGroup = record.getDataGroup();
		DataGroup childReferences = clientDataGroup.getFirstGroupWithNameInData("childReferences");
		DataGroup childReference = createChildReferenceWithRepeatIdRecordTypeAndRecordId("1",
				"metadataGroup", "someOtherRecordId", "1", "3");
		childReferences.addChild(childReference);
	}

	@Test
	public void testRepeatMinWithoutRecord() throws Exception {
		RecordHolder.setRecord(null);
		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("someRecordId");
		assertEquals(fixture.getRepeatMin(), "not found");
	}

	@Test
	public void testNoMatchingChildForRepeatMinRecordType() {
		fixture.setLinkedRecordType("NOTmetadataGroup");
		fixture.setLinkedRecordId("someRecordId");
		assertEquals(fixture.getRepeatMin(), "not found");
	}

	@Test
	public void testNoMatchingChildForRepeatMin() {
		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("NOTsomeRecordId");
		assertEquals(fixture.getRepeatMin(), "not found");
	}

	@Test
	public void testRepeatMinIsCorrect() {
		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("someRecordId");
		assertEquals(fixture.getRepeatMin(), "0");
	}

	@Test
	public void testRepeatMinIsCorrectSecondChild() {
		createAndAddSecondChild();
		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("someOtherRecordId");
		assertEquals(fixture.getRepeatMin(), "1");
	}

	@Test
	public void testRepeatMaxWithoutRecord() throws Exception {
		RecordHolder.setRecord(null);
		assertEquals(fixture.getRepeatMax(), "not found");
	}

	@Test
	public void testNoMatchingChildForRepeatMax() {
		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("NOTsomeRecordId");
		assertEquals(fixture.getRepeatMax(), "not found");
	}

	@Test
	public void testRepeatMaxIsCorrect() {
		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("someRecordId");
		assertEquals(fixture.getRepeatMax(), "X");
	}

	@Test
	public void testRepeatMaxIsCorrectSecondChild() {
		createAndAddSecondChild();
		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("someOtherRecordId");
		assertEquals(fixture.getRepeatMax(), "3");
	}

	@Test
	public void testMoreThanOneTestOnSameRecord() {
		createAndAddSecondChild();
		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("someRecordId");
		assertEquals(fixture.getRepeatMin(), "0");
		assertEquals(fixture.getRepeatMax(), "X");

		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("someOtherRecordId");
		assertEquals(fixture.getRepeatMin(), "1");
		assertEquals(fixture.getRepeatMax(), "3");
	}

	@Test
	public void testMoreThanOneTestOnSameRecordNoMatchSecondLink() {
		createAndAddSecondChild();
		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("someRecordId");
		assertEquals(fixture.getRepeatMin(), "0");
		assertEquals(fixture.getRepeatMax(), "X");

		fixture.setLinkedRecordType("metadataGroup");
		fixture.setLinkedRecordId("NOTsomeOtherRecordId");
		assertEquals(fixture.getRepeatMin(), "not found");
		assertEquals(fixture.getRepeatMax(), "not found");
	}
}
