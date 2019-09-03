package se.uu.ub.cora.fitnesseintegration;

import javax.ws.rs.core.Response;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecord;
import se.uu.ub.cora.data.converter.DataToJsonConverter;
import se.uu.ub.cora.data.converter.DataToJsonConverterFactoryImp;
import se.uu.ub.cora.httphandler.HttpHandler;
import se.uu.ub.cora.httphandler.HttpHandlerFactory;
import se.uu.ub.cora.json.builder.JsonBuilderFactory;
import se.uu.ub.cora.json.builder.org.OrgJsonBuilderFactoryAdapter;

public class MetadataValidationFixture extends RecordEndpointFixture {

	private HttpHandlerFactory httpHandlerFactory;
	private String validateLinks;
	private String dataDivider;
	private String validationOrderType;
	private String jsonRecordToValidate;
	private String valid;

	public MetadataValidationFixture() {
		httpHandlerFactory = DependencyProvider.getHttpHandlerFactory();
	}

	public String testGetValidationOrder() {
		DataGroup validationOrder = createValidationOrder();

		JsonBuilderFactory factory = new OrgJsonBuilderFactoryAdapter();
		DataToJsonConverterFactoryImp dataToJsonConverterFactory = new DataToJsonConverterFactoryImp();
		DataToJsonConverter converter = dataToJsonConverterFactory.createForDataElement(factory,
				validationOrder);
		String validationOrderAsJson = converter.toJson();

		return "{\"order\":" + validationOrderAsJson + ",	\"record\":" + jsonRecordToValidate
				+ "}";

	}

	private DataGroup createValidationOrder() {
		DataGroup validationOrder = DataGroup.withNameInData("validationOrder");
		createAndAddRecordInfo(validationOrder);
		createAndAddRecordTypeGroup(validationOrder);
		createAndAddAtomicValues(validationOrder);
		return validationOrder;
	}

	private void createAndAddRecordInfo(DataGroup validationOrder) {
		DataGroup recordInfo = DataGroup.withNameInData("recordInfo");
		DataGroup dataDividerGroup = createLinkUsingNameInDataRecordTypeAndRecordId("dataDivider",
				"system", dataDivider);
		recordInfo.addChild(dataDividerGroup);
		validationOrder.addChild(recordInfo);
	}

	private DataGroup createLinkUsingNameInDataRecordTypeAndRecordId(String nameInData,
			String linkedRecordType, String linkedRecordId) {
		DataGroup linkGroup = DataGroup.withNameInData(nameInData);
		linkGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordType", linkedRecordType));
		linkGroup.addChild(DataAtomic.withNameInDataAndValue("linkedRecordId", linkedRecordId));
		return linkGroup;
	}

	private void createAndAddRecordTypeGroup(DataGroup validationOrder) {
		DataGroup recordTypeGroup = createLinkUsingNameInDataRecordTypeAndRecordId("recordType",
				"recordType", validationOrderType);
		validationOrder.addChild(recordTypeGroup);
	}

	private void createAndAddAtomicValues(DataGroup validationOrder) {
		validationOrder.addChild(DataAtomic.withNameInDataAndValue("validateLinks", validateLinks));
		validationOrder
				.addChild(DataAtomic.withNameInDataAndValue("metadataToValidate", "existing"));
	}

	public String testValidateRecord() {
		HttpHandler httpHandler = createHttpHandlerForPostWithUrlAndContentType(
				baseUrl + "workOrder", "application/vnd.uub.workorder+json");
		statusType = Response.Status.fromStatusCode(httpHandler.getResponseCode());

		return getResponseTextFromHttpHandler(httpHandler);
	}

	private String getResponseTextFromHttpHandler(HttpHandler httpHandler) {
		if (responseIsOk()) {
			return getValidationResponseText(httpHandler);
		}
		return httpHandler.getErrorText();
	}

	private String getValidationResponseText(HttpHandler httpHandler) {
		String responseText = httpHandler.getResponseText();
		extractAndSetValidValue(responseText);
		return responseText;
	}

	private void extractAndSetValidValue(String responseText) {
		DataRecord validationResultRecord = convertJsonToClientDataRecord(responseText);

		DataGroup dataGroup = validationResultRecord.getDataGroup();
		valid = dataGroup.getFirstAtomicValueWithNameInData("valid");
	}

	@Override
	public HttpHandlerFactory getHttpHandlerFactory() {
		return httpHandlerFactory;
	}

	public void setValidateLinks(String validateLinks) {
		this.validateLinks = validateLinks;

	}

	public void setDataDivider(String dataDivider) {
		this.dataDivider = dataDivider;

	}

	public void setValidationOrderRecordType(String validationOrderRecordType) {
		this.validationOrderType = validationOrderRecordType;

	}

	public void setJsonRecordToValidate(String jsonRecordToValidate) {
		this.jsonRecordToValidate = jsonRecordToValidate;
	}

	public String getValid() {
		return valid;
	}
}
