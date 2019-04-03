package se.uu.ub.cora.fitnesseintegration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MetadataValidationFixtureTest {

	private HttpHandlerFactorySpy httpHandlerFactorySpy;
	private JsonToDataConverterFactorySpy jsonToDataConverterFactory;
	private MetadataValidationFixture fixture;

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
		fixture = new MetadataValidationFixture();
	}

	@Test
	public void init() {
		assertTrue(fixture.getHttpHandlerFactory() instanceof HttpHandlerFactorySpy);
	}

	@Test
	public void testGetValidationOrderForRecord() {
		fixture.setJsonRecordToValidate("{\"name\":\"value\"}");
		fixture.setValidateLinks("true");
		fixture.setDataDivider("someDataDivider");
		fixture.setValidationOrderRecordType("someRecordType");
		String validationOrderJson = fixture.testGetValidationOrder();
		String expectedJson = "{\"order\":{\"children\":[{\"children\":[{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"system\"},{\"name\":\"linkedRecordId\",\"value\":\"someDataDivider\"}],\"name\":\"dataDivider\"}],\"name\":\"recordInfo\"},{\"children\":[{\"name\":\"linkedRecordType\",\"value\":\"recordType\"},{\"name\":\"linkedRecordId\",\"value\":\"someRecordType\"}],\"name\":\"recordType\"},{\"name\":\"validateLinks\",\"value\":\"true\"},{\"name\":\"metadataToValidate\",\"value\":\"existing\"}],\"name\":\"validationOrder\"},	\"record\":{\"name\":\"value\"}}";
		assertEquals(validationOrderJson, expectedJson);

	}

	@Test
	public void testValidateRecordDataForFactoryIsOk() {
		jsonToDataConverterFactory.typeToFactor = "validatorSpy";
		fixture.setType("someType");
		fixture.setId("someId");
		fixture.setAuthToken("someToken");
		fixture.setJson("{\"name\":\"value\"}");
		fixture.testValidateRecord();
		assertCorrectHttpHandlerForValidate();

		JsonToDataConverterForValidationSpy converterSpy = (JsonToDataConverterForValidationSpy) jsonToDataConverterFactory.factored;

		assertTrue(converterSpy.toInstanceWasCalled);

	}

	private void assertCorrectHttpHandlerForValidate() {
		HttpHandlerSpy httpHandlerSpy = httpHandlerFactorySpy.httpHandlerSpy;
		assertEquals(httpHandlerSpy.requestMetod, "POST");
		assertEquals(httpHandlerSpy.outputString, "{\"name\":\"value\"}");
		assertEquals(httpHandlerSpy.requestProperties.get("Accept"),
				"application/vnd.uub.record+json");
		assertEquals(httpHandlerSpy.requestProperties.get("Content-Type"),
				"application/vnd.uub.workorder+json");
		assertEquals(httpHandlerSpy.requestProperties.get("authToken"), "someToken");
		assertEquals(httpHandlerSpy.requestProperties.size(), 3);
		assertEquals(httpHandlerFactorySpy.urlString,
				"http://localhost:8080/therest/rest/record/workOrder");
	}

	@Test
	public void testValidateRecordOk() {
		jsonToDataConverterFactory.typeToFactor = "validatorSpy";
		assertEquals(fixture.testValidateRecord(),
				httpHandlerFactorySpy.httpHandlerSpy.responseText);
		assertEquals(fixture.getValid(), "true");
	}

	@Test
	public void testValidateRecordNotOk() {
		jsonToDataConverterFactory.isValid = "false";
		jsonToDataConverterFactory.typeToFactor = "validatorSpy";
		assertEquals(fixture.testValidateRecord(),
				httpHandlerFactorySpy.httpHandlerSpy.responseText);
		assertEquals(fixture.getValid(), "false");
	}

	@Test
	public void testValidateRecordIncorrectValidationOrder() {
		httpHandlerFactorySpy.changeFactoryToFactorInvalidHttpHandlers();
		jsonToDataConverterFactory.isValid = "false";
		jsonToDataConverterFactory.typeToFactor = "validatorSpy";
		assertEquals(fixture.testValidateRecord(), "bad things happend");
	}

}
