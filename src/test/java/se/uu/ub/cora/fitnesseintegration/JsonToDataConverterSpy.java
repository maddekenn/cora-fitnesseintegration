package se.uu.ub.cora.fitnesseintegration;

import se.uu.ub.cora.data.DataAtomic;
import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataPart;
import se.uu.ub.cora.data.converter.JsonToDataConverter;
import se.uu.ub.cora.json.parser.JsonValue;

public class JsonToDataConverterSpy implements JsonToDataConverter {

	public boolean toInstanceWasCalled = false;
	public String json;
	public JsonValue jsonValue;

	public JsonToDataConverterSpy(String json) {
		this.json = json;
	}

	public JsonToDataConverterSpy(JsonValue jsonValue) {
		this.jsonValue = jsonValue;
	}

	@Override
	public DataPart toInstance() {
		toInstanceWasCalled = true;
		DataGroup clientDataGroup = DataGroup.withNameInData("someTopLevelDataGroup");
		clientDataGroup.addChild(DataAtomic.withNameInDataAndValue("nameInData", "someNameInData"));
		return clientDataGroup;
	}

}
