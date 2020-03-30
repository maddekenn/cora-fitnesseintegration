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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.DataRecord;
import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataRecordConverter;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParseException;

public class ComparerFixture {

	private RecordHandler recordHandler;
	private String type;
	private String storedListAsJson;
	private JsonHandler jsonHandler;
	private JsonToDataRecordConverter jsonToDataRecordConverter;
	private ChildComparer childComparer;
	private String childrenToCompare;
	private int indexToCompareTo;
	private HttpHandlerFactory httpHandlerFactory;
	private String authToken;
	private String listFilter;

	public ComparerFixture() {
		httpHandlerFactory = DependencyProvider.getHttpHandlerFactory();
		recordHandler = new RecordHandlerImp(httpHandlerFactory);
		childComparer = DependencyProvider.getChildComparer();
	}

	public void testReadRecordListAndStoreRecords() throws UnsupportedEncodingException {
		String baseUrl = SystemUrl.getUrl() + "rest/record/";
		storedListAsJson = recordHandler.readRecordList(baseUrl + type, listFilter,
				authToken).responseText;

		List<DataRecord> convertedRecords = convertToRecords();
		DataHolder.setRecordList(convertedRecords);

	}

	private List<DataRecord> convertToRecords() {
		JsonArray data = extractListOfRecords();
		List<DataRecord> convertedRecords = new ArrayList<>();
		while (data.iterator().hasNext()) {
			convertAndAddRecord(data, convertedRecords);
		}
		return convertedRecords;
	}

	private JsonArray extractListOfRecords() {
		JsonObject list = jsonHandler.parseStringAsObject(storedListAsJson);
		JsonObject dataList = (JsonObject) list.getValue("dataList");
		return (JsonArray) dataList.getValue("data");
	}

	private void convertAndAddRecord(JsonArray data, List<DataRecord> convertedRecords) {
		JsonObject next = (JsonObject) data.iterator().next();
		DataRecord record = jsonToDataRecordConverter.toInstance(next);
		convertedRecords.add(record);
	}

	public void setType(String type) {
		this.type = type;

	}

	public RecordHandler getRecordHandler() {
		return recordHandler;
	}

	void setRecordHandler(RecordHandler recordHandler) {
		this.recordHandler = recordHandler;

	}

	public String getStoredListAsJson() {
		return storedListAsJson;
	}

	public String testReadFromListCheckContain() {
		try {
			ClientDataGroup clientDataGroup = getDataGroupFromRecordHolderUsingIndex();
			return compareChildrenUsingDataGroup(clientDataGroup);
		} catch (JsonParseException exception) {
			return exception.getMessage();
		}

	}

	private String compareChildrenUsingDataGroup(ClientDataGroup clientDataGroup) {
		JsonObject childrenObject = jsonHandler.parseStringAsObject(childrenToCompare);
		List<String> errorMessages = childComparer
				.checkDataGroupContainsChildrenWithCorrectValues(clientDataGroup, childrenObject);
		return errorMessages.isEmpty() ? "OK" : joinErrorMessages(errorMessages);
	}

	private ClientDataGroup getDataGroupFromRecordHolderUsingIndex() {
		int index = getListIndexToCompareTo();
		return DataHolder.getRecordList().get(index).getClientDataGroup();
	}

	private int getListIndexToCompareTo() {
		return indexToCompareTo;
	}

	private String joinErrorMessages(List<String> errorMessages) {
		StringJoiner compareError = new StringJoiner(" ");
		for (String errorMessage : errorMessages) {
			compareError.add(errorMessage);
		}
		return compareError.toString();
	}

	public void setListIndexToCompareTo(int index) {
		this.indexToCompareTo = index;

	}

	void setJsonHandler(JsonHandler jsonHandler) {
		// needed for test
		this.jsonHandler = jsonHandler;
	}

	void setJsonToDataRecordConverter(JsonToDataRecordConverter jsonToDataConverter) {
		// needed for test
		this.jsonToDataRecordConverter = jsonToDataConverter;
	}

	public void setChildren(String children) {
		childrenToCompare = children;

	}

	public ChildComparer getChildComparer() {
		// needed for test
		return childComparer;
	}

	public HttpHandlerFactory getHttpHandlerFactory() {
		// needed for test
		return httpHandlerFactory;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public void setListFilter(String listFilter) {
		this.listFilter = listFilter;
	}
}
