package se.uu.ub.cora.fitnesseintegration;

import javax.ws.rs.core.Response;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.clientdata.ClientDataRecord;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverter;
import se.uu.ub.cora.clientdata.converter.javatojson.DataToJsonConverterFactoryImp;
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
	// private String type;
	// private String authToken;
	// private String id;
	// private String json;
	String valid;
	// private String baseUrl = SystemUrl.getUrl() + "rest/record/";

	public MetadataValidationFixture() {
		httpHandlerFactory = DependencyProvider.getHttpHandlerFactory();
	}

	public String testGetValidationOrder() {
		ClientDataGroup validationOrder = createValidationOrder();

		JsonBuilderFactory factory = new OrgJsonBuilderFactoryAdapter();
		DataToJsonConverterFactoryImp dataToJsonConverterFactory = new DataToJsonConverterFactoryImp();
		DataToJsonConverter converter = dataToJsonConverterFactory
				.createForClientDataElement(factory, validationOrder);
		String validationOrderAsJson = converter.toJson();

		return "{\"order\":" + validationOrderAsJson + ",	\"record\":" + jsonRecordToValidate
				+ "}";

	}

	private ClientDataGroup createValidationOrder() {
		ClientDataGroup validationOrder = ClientDataGroup.withNameInData("validationOrder");
		createAndAddRecordInfo(validationOrder);

		createAndAddRecordTypeGroup(validationOrder);

		validationOrder
				.addChild(ClientDataAtomic.withNameInDataAndValue("validateLinks", validateLinks));
		validationOrder.addChild(
				ClientDataAtomic.withNameInDataAndValue("metadataToValidate", "existing"));
		return validationOrder;
	}

	private void createAndAddRecordInfo(ClientDataGroup validationOrder) {
		ClientDataGroup recordInfo = ClientDataGroup.withNameInData("recordInfo");
		ClientDataGroup dataDividerGroup = ClientDataGroup.withNameInData("dataDivider");
		dataDividerGroup
				.addChild(ClientDataAtomic.withNameInDataAndValue("linkedRecordType", "system"));
		dataDividerGroup
				.addChild(ClientDataAtomic.withNameInDataAndValue("linkedRecordId", dataDivider));
		recordInfo.addChild(dataDividerGroup);
		validationOrder.addChild(recordInfo);
	}

	private void createAndAddRecordTypeGroup(ClientDataGroup validationOrder) {
		ClientDataGroup recordTypeGroup = ClientDataGroup.withNameInData("recordType");
		recordTypeGroup.addChild(
				ClientDataAtomic.withNameInDataAndValue("linkedRecordType", "recordType"));
		recordTypeGroup.addChild(
				ClientDataAtomic.withNameInDataAndValue("linkedRecordId", validationOrderType));
		validationOrder.addChild(recordTypeGroup);
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

	public String testValidateRecord() {
		HttpHandler httpHandler = createHttpHandlerForPostWithUrlAndContentType(
				baseUrl + "workOrder", "application/vnd.uub.workorder+json");
		statusType = Response.Status.fromStatusCode(httpHandler.getResponseCode());

		if (statusType.equals(Response.Status.OK)) {
			String responseText = httpHandler.getResponseText();
			ClientDataRecord validationResultRecord = convertJsonToClientDataRecord(responseText);

			ClientDataGroup dataGroup = validationResultRecord.getClientDataGroup();
			valid = dataGroup.getFirstAtomicValueWithNameInData("valid");
			return responseText;
		}
		return httpHandler.getErrorText();
	}

	public void setJsonRecordToValidate(String jsonRecordToValidate) {
		this.jsonRecordToValidate = jsonRecordToValidate;
	}

	public String getValid() {
		return valid;
	}
}
