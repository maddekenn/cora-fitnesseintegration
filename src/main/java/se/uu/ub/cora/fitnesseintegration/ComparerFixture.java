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

public class ComparerFixture {

	private RecordHandler recordHandler;
	private String type;
	private String storedListAsJson;

	public ComparerFixture() {
		recordHandler = new RecordHandlerImp();
	}

	public void testReadRecordListAndStoreRecords() {
		String baseUrl = SystemUrl.getUrl() + "rest/record/";
		storedListAsJson = recordHandler.readRecordList(baseUrl + type);

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
