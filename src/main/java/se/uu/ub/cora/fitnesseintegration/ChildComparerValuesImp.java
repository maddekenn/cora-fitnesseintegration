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

import java.util.ArrayList;
import java.util.List;

import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.json.parser.JsonArray;
import se.uu.ub.cora.json.parser.JsonObject;
import se.uu.ub.cora.json.parser.JsonParseException;
import se.uu.ub.cora.json.parser.JsonString;
import se.uu.ub.cora.json.parser.JsonValue;

public class ChildComparerValuesImp implements ChildComparerValues {

	@Override
	public List<String> checkDataGroupContainsChildrenWithValues(ClientDataGroup dataGroup,
			JsonValue jsonValue) {
		try {
			return tryToCheckDataGroupContainsChildren(dataGroup, jsonValue);
		} catch (Exception e) {
			throw new JsonParseException(e.getMessage());
		}
	}

	private List<String> tryToCheckDataGroupContainsChildren(ClientDataGroup dataGroup,
			JsonValue jsonValue) {
		List<String> errorMessages = new ArrayList<>();
		for (JsonValue childValue : extractChildren((JsonObject) jsonValue)) {
			checkDataGroupContainsChild(dataGroup, errorMessages, (JsonObject) childValue);
		}
		return errorMessages;
	}

	private void checkDataGroupContainsChild(ClientDataGroup dataGroup, List<String> errorMessages,
			JsonObject childObject) {
		String nameInData = getNameInData(childObject);
		if (!dataGroup.containsChildWithNameInData(nameInData)) {
			errorMessages.add("Child with nameInData " + nameInData + " is missing.");
		} else {
			checkChildValues(dataGroup, errorMessages, childObject, nameInData);
		}
	}

	private void checkChildValues(ClientDataGroup dataGroup, List<String> errorMessages,
			JsonObject childObject, String nameInData) {
		if (isAtomicType(childObject)) {
			checkAtomicChild(dataGroup, errorMessages, childObject, nameInData);
		} else {
			checkDataGroup(errorMessages, dataGroup, childObject, nameInData);
		}
	}

	private String getNameInData(JsonObject childObject) {
		JsonString name = getName(childObject);
		return name.getStringValue();
	}

	private void checkDataGroup(List<String> errorMessages, ClientDataGroup dataGroup,
			JsonObject groupObject, String nameInData) {
		ClientDataGroup childGroup = dataGroup.getFirstGroupWithNameInData(nameInData);

		JsonArray children = groupObject.getValueAsJsonArray("children");
		for (JsonValue childValue : children) {
			JsonObject childObject = (JsonObject) childValue;
			checkDataGroupContainsChild(childGroup, errorMessages, childObject);
		}

	}

	private boolean isAtomicType(JsonObject childObject) {
		JsonString type = (JsonString) childObject.getValue("type");
		return "atomic".equals(type.getStringValue());
	}

	private void checkAtomicChild(ClientDataGroup dataGroup, List<String> errorMessages,
			JsonObject childObject, String nameInData) {
		JsonString value = (JsonString) childObject.getValue("value");
		String atomicValue = dataGroup.getFirstAtomicValueWithNameInData(nameInData);
		if (!atomicValue.equals(value.getStringValue())) {
			errorMessages.add(
					"Child with nameInData " + nameInData + " does not have the correct value.");
		}
	}

	private JsonArray extractChildren(JsonObject jsonObject) {
		return jsonObject.getValueAsJsonArray("children");
	}

	private JsonString getName(JsonObject child) {
		throwErrorIfMissingKey(child);
		return (JsonString) child.getValue("name");
	}

	private void throwErrorIfMissingKey(JsonObject child) {
		if (!child.containsKey("name")) {
			throw new JsonParseException("child must contain key: name");
		}
	}

}
