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

import se.uu.ub.cora.clientdata.converter.jsontojava.JsonToDataRecordConverter;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;

public class ComparerFixture {

	private RecordHandler recordHandler;
	private String type;
	private String storedListAsJson;
	private JsonHandler jsonHandler;
	private JsonToDataRecordConverter jsonToDataRecordConverter;

	public ComparerFixture() {
		recordHandler = new RecordHandlerImp();
	}

	public void testReadRecordListAndStoreRecords() {
		String baseUrl = SystemUrl.getUrl() + "rest/record/";
		storedListAsJson = recordHandler.readRecordList(baseUrl + type);

		JsonObject list = jsonHandler.parseStringAsObject(storedListAsJson);
		// JsonObject jsonObject = (JsonObject) jsonParser.parseString(
		// "{\"dataList\":{\"fromNo\":\"0\",\"data\":[{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"bush3\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://systemone:8080/systemone/rest/record/system/cora\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"bush\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"131313\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://systemone:8080/systemone/rest/record/user/131313\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"createdBy\"},{\"name\":\"tsCreated\",\"value\":\"2020-03-27T08:47:46.179393Z\"},{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"131313\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://systemone:8080/systemone/rest/record/user/131313\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"updatedBy\"},{\"name\":\"tsUpdated\",\"value\":\"2020-03-27T08:47:46.179393Z\"}],\"name\":\"updated\"}],\"name\":\"recordInfo\"},{\"repeatId\":\"1\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"tree\"},{\"name\":\"linkedRecordId\",\"value\":\"tree1\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://systemone:8080/systemone/rest/record/tree/tree1\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"testLink\"}],\"name\":\"bush\"},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://systemone:8080/systemone/rest/record/bush/bush3\",\"accept\":\"application/vnd.uub.record+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"http://systemone:8080/systemone/rest/record/bush/bush3\",\"accept\":\"application/vnd.uub.record+json\"},\"index\":{\"requestMethod\":\"POST\",\"rel\":\"index\",\"body\":{\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"bush\"}],\"name\":\"recordType\"},{\"name\":\"recordId\",\"value\":\"bush3\"},{\"name\":\"type\",\"value\":\"index\"}],\"name\":\"workOrder\"},\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"http://systemone:8080/systemone/rest/record/workOrder/\",\"accept\":\"application/vnd.uub.record+json\"},\"delete\":{\"requestMethod\":\"DELETE\",\"rel\":\"delete\",\"url\":\"http://systemone:8080/systemone/rest/record/bush/bush3\"}}}},{\"record\":{\"data\":{\"children\":[{\"children\":[{\"name\":\"id\",\"value\":\"bush2\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"cora\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://systemone:8080/systemone/rest/record/system/cora\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"dataDivider\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"bush\"}],\"name\":\"type\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"131313\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://systemone:8080/systemone/rest/record/user/131313\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"createdBy\"},{\"name\":\"tsCreated\",\"value\":\"2020-03-27T08:47:46.030604Z\"},{\"repeatId\":\"0\",\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"user\"},{\"name\":\"linkedRecordId\",\"value\":\"131313\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://systemone:8080/systemone/rest/record/user/131313\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"updatedBy\"},{\"name\":\"tsUpdated\",\"value\":\"2020-03-27T08:47:46.030604Z\"}],\"name\":\"updated\"}],\"name\":\"recordInfo\"},{\"repeatId\":\"1\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"tree\"},{\"name\":\"linkedRecordId\",\"value\":\"tree1\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://systemone:8080/systemone/rest/record/tree/tree1\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"testLink\"},{\"repeatId\":\"2\",\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"tree\"},{\"name\":\"linkedRecordId\",\"value\":\"tree1\"}],\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://systemone:8080/systemone/rest/record/tree/tree1\",\"accept\":\"application/vnd.uub.record+json\"}},\"name\":\"testLinkPath\"}],\"name\":\"bush\"},\"actionLinks\":{\"read\":{\"requestMethod\":\"GET\",\"rel\":\"read\",\"url\":\"http://systemone:8080/systemone/rest/record/bush/bush2\",\"accept\":\"application/vnd.uub.record+json\"},\"update\":{\"requestMethod\":\"POST\",\"rel\":\"update\",\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"http://systemone:8080/systemone/rest/record/bush/bush2\",\"accept\":\"application/vnd.uub.record+json\"},\"index\":{\"requestMethod\":\"POST\",\"rel\":\"index\",\"body\":{\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"bush\"}],\"name\":\"recordType\"},{\"name\":\"recordId\",\"value\":\"bush2\"},{\"name\":\"type\",\"value\":\"index\"}],\"name\":\"workOrder\"},\"contentType\":\"application/vnd.uub.record+json\",\"url\":\"http://systemone:8080/systemone/rest/record/workOrder/\",\"accept\":\"application/vnd.uub.record+json\"},\"delete\":{\"requestMethod\":\"DELETE\",\"rel\":\"delete\",\"url\":\"http://systemone:8080/systemone/rest/record/bush/bush2\"}}}}],\"totalNo\":\"6\",\"containDataOfType\":\"bush\",\"toNo\":\"6\"}}");
		JsonObject dataList = (JsonObject) list.getValue("dataList");
		JsonArray data = (JsonArray) dataList.getValue("data");
		while (data.iterator().hasNext()) {
			JsonObject next = (JsonObject) data.iterator().next();
			jsonToDataRecordConverter.toInstance(next);
		}

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

	public void setListIndexToCompareTo(int index) {
		// TODO Auto-generated method stub

	}

	public String testReadFromListCheckContain() {
		// TODO Auto-generated method stub
		return null;
	}

	void setJsonHandler(JsonHandler jsonHandler) {
		// needed for test
		this.jsonHandler = jsonHandler;
	}

	void setJsonToDataRecordConverter(JsonToDataRecordConverter jsonToDataConverter) {
		// needed for test
		this.jsonToDataRecordConverter = jsonToDataConverter;
	}

	// private String testReadCheckContain() {
	// JsonObject jsonObject = jsonHandler.parseStringAsObject(readJson);
	// DataRecord record = (DataRecord) jsonToDataRecordConverter.toInstance(jsonObject);
	//
	// JsonObject childrenObject = jsonHandler.parseStringAsObject(childrenToCompare);
	// return tryToCompareChildren(record, childrenObject);

	// }

	// public void testReadRecordAndStoreJson() {
	// String responseText = testReadRecordList();
	// DataRecord clientDataRecord = convertJsonToClientDataRecord(responseText);
	//
	// RecordHolder.setRecord(clientDataRecord);
	// }

	// public String testReadRecordList() throws UnsupportedEncodingException {
	// String url = baseUrl + type;
	// if (json != null) {
	// url += "?filter=" + URLEncoder.encode(json, StandardCharsets.UTF_8.name());
	// }
	// return getResponseTextOrErrorTextFromUrl(url);
	// }

	// private String getResponseTextOrErrorTextFromUrl(String url) {
	// HttpHandler httpHandler = createHttpHandlerWithAuthTokenAndUrl(url);
	// httpHandler.setRequestMethod("GET");
	//
	// statusType = Response.Status.fromStatusCode(httpHandler.getResponseCode());
	// if (responseIsOk()) {
	// return httpHandler.getResponseText();
	// }
	// return httpHandler.getErrorText();
	// }

}
