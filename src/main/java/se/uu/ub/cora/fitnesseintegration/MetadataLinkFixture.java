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

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecord;
import se.uu.ub.cora.data.converter.JsonToDataConverterFactory;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class MetadataLinkFixture {

	protected String linkedRecordType;
	protected String linkedRecordId;
	private List<DataGroup> childReferenceList = new ArrayList<>();
	private DataGroup matchingChildReference;
	private HttpHandlerFactory httpHandlerFactory;
	private String baseUrl = SystemUrl.getUrl() + "rest/record/";
	private String authToken;
	private JsonToDataConverterFactory jsonToDataConverterFactory;
	private JsonToDataRecordConverter recordConverter;

	public MetadataLinkFixture() {
		httpHandlerFactory = DependencyProvider.getHttpHandlerFactory();
		jsonToDataConverterFactory = DependencyProvider.getJsonToDataConverterFactory();
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void setLinkedRecordType(String linkedRecordType) {
		this.linkedRecordType = linkedRecordType;
		tryToSetMatchingChildReference();
	}

	public void setLinkedRecordId(String linkedRecordId) {
		this.linkedRecordId = linkedRecordId;
		tryToSetMatchingChildReference();
	}

	private void tryToSetMatchingChildReference() {
		if (linkedRecordTypeAndRecordIdExist()) {
			resetData();
			possiblySetChildReferenceList();
			setMatchingChildReference();
		}
	}

	private boolean linkedRecordTypeAndRecordIdExist() {
		return linkedRecordType != null && linkedRecordId != null;
	}

	private void resetData() {
		matchingChildReference = null;
	}

	private void possiblySetChildReferenceList() {
		DataRecord record = RecordHolder.getRecord();
		if (recordContainsDataGroup(record)) {
			DataGroup topLevelDataGroup = record.getDataGroup();
			setChildReferenceList(topLevelDataGroup);
		}
	}

	private boolean recordContainsDataGroup(DataRecord record) {
		return null != record && record.getDataGroup() != null;
	}

	private void setChildReferenceList(DataGroup topLevelDataGroup) {
		if (childReferencesExists(topLevelDataGroup)) {
			DataGroup childReferences = topLevelDataGroup
					.getFirstGroupWithNameInData("childReferences");
			childReferenceList = childReferences.getAllGroupsWithNameInData("childReference");
		}
	}

	private boolean childReferencesExists(DataGroup topLevelDataGroup) {
		return topLevelDataGroup.containsChildWithNameInData("childReferences");
	}

	private void setMatchingChildReference() {
		for (DataGroup childReference : childReferenceList) {
			setChildReferenceIfMatchingTypeAndId(childReference);
		}
	}

	private void setChildReferenceIfMatchingTypeAndId(DataGroup childReference) {
		String childLinkedRecordType = extractValueFromReferenceUsingNameInData(childReference,
				"linkedRecordType");
		String childLinkedRecordId = extractValueFromReferenceUsingNameInData(childReference,
				"linkedRecordId");

		if (childReferenceMatchesTypeAndId(childLinkedRecordType, childLinkedRecordId)) {
			matchingChildReference = childReference;
			setUpHttpHandlerForReadingChildReference(childLinkedRecordType, childLinkedRecordId);
		}
	}

	private HttpHandler setUpHttpHandlerForReadingChildReference(String childLinkedRecordType,
			String childLinkedRecordId) {
		String url = baseUrl + childLinkedRecordType + "/" + childLinkedRecordId;
		HttpHandler httpHandler = httpHandlerFactory.factor(url);
		httpHandler.setRequestMethod("GET");
		httpHandler.setRequestProperty("authToken", authToken);
		return httpHandler;
	}

	protected boolean childReferenceMatchesTypeAndId(String childLinkedRecordType,
			String childLinkedRecordId) {
		return childLinkedRecordId.equals(linkedRecordId)
				&& childLinkedRecordType.equals(linkedRecordType);
	}

	protected String extractValueFromReferenceUsingNameInData(DataGroup childReference,
			String childNameInData) {
		DataGroup ref = childReference.getFirstGroupWithNameInData("ref");
		return ref.getFirstAtomicValueWithNameInData(childNameInData);
	}

	public String getRepeatMin() {
		return getAtomicValueByNameInDataFromMatchingChild("repeatMin");
	}

	private String getAtomicValueByNameInDataFromMatchingChild(String childNameInData) {
		if (null == matchingChildReference) {
			return "not found";
		}
		return matchingChildReference.getFirstAtomicValueWithNameInData(childNameInData);
	}

	public String getRepeatMax() {
		return getAtomicValueByNameInDataFromMatchingChild("repeatMax");
	}

	public String getNameInData() {
		if (null == matchingChildReference) {
			return "not found";
		}
		return getNameInDataFromMatchingChildReference();

	}

	private String getNameInDataFromMatchingChildReference() {
		RecordIdentifier identfier = getChildReferenceAsRecordIdentifier();
		String responseText = readRecordAsJson(identfier);
		return getNameInDataFromConvertedJson(responseText);
	}

	private RecordIdentifier getChildReferenceAsRecordIdentifier() {
		String childLinkedRecordType = extractValueFromReferenceUsingNameInData(
				matchingChildReference, "linkedRecordType");
		String childLinkedRecordId = extractValueFromReferenceUsingNameInData(
				matchingChildReference, "linkedRecordId");

		return RecordIdentifier.usingTypeAndId(childLinkedRecordType, childLinkedRecordId);
	}

	private String readRecordAsJson(RecordIdentifier identfier) {
		HttpHandler httpHandler = setUpHttpHandlerForReadingChildReference(identfier.type,
				identfier.id);
		return httpHandler.getResponseText();
	}

	private String getNameInDataFromConvertedJson(String responseText) {
		JsonObject recordJsonObject = createJsonObjectFromResponseText(responseText);
		recordConverter = JsonToDataRecordConverter
				.forJsonObjectUsingConverterFactory(recordJsonObject, jsonToDataConverterFactory);
		DataRecord clientDataRecord = recordConverter.toInstance();
		return getNameInDataFromDataGroupInRecord(clientDataRecord);
	}

	private String getNameInDataFromDataGroupInRecord(DataRecord clientDataRecord) {
		DataGroup dataElement = clientDataRecord.getDataGroup();
		return dataElement.getFirstAtomicValueWithNameInData("nameInData");
	}

	private JsonObject createJsonObjectFromResponseText(String responseText) {
		JsonParser jsonParser = new OrgJsonParser();
		JsonValue jsonValue = jsonParser.parseString(responseText);
		return (JsonObject) jsonValue;
	}

	public JsonToDataRecordConverter getJsonToRecordDataConverter() {
		return recordConverter;
	}

}
