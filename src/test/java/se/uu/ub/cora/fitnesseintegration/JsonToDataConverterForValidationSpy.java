package se.uu.ub.cora.fitnesseintegration;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataPart;
import se.uu.ub.cora.data.converter.JsonToDataConverter;
import se.uu.ub.cora.json.parser.JsonValue;

public class JsonToDataConverterForValidationSpy implements JsonToDataConverter {

	public boolean toInstanceWasCalled = false;
	public String json;
	public JsonValue jsonValue;
	public String isValid = "true";

	public JsonToDataConverterForValidationSpy(String json) {
		this.json = json;
	}

	public JsonToDataConverterForValidationSpy(JsonValue jsonValue) {
		this.jsonValue = jsonValue;
	}

	@Override
	public DataPart toInstance() {
		toInstanceWasCalled = true;
		DataGroup clientDataGroup = DataGroup.withNameInData("someTopLevelDataGroup");
		clientDataGroup.addChild(DataAtomic.withNameInDataAndValue("nameInData", "someNameInData"));
		clientDataGroup.addChild(DataAtomic.withNameInDataAndValue("valid", isValid));
		return clientDataGroup;
	}

}
