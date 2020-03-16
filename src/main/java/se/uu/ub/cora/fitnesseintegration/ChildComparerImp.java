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

public class ChildComparerImp implements ChildComparer {

	@Override
	public boolean dataGroupContainsChildren(ClientDataGroup dataGroup, JsonValue jsonValue) {
		return checkDataGroupContainsChildren(dataGroup, jsonValue).isEmpty();
	}

	@Override
	public List<String> checkDataGroupContainsChildren(ClientDataGroup dataGroup,
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
		for (JsonValue childValue : extractChildren(jsonValue)) {
			checkDataGroupContainsChild(dataGroup, errorMessages, childValue);
		}
		return errorMessages;
	}

	private void checkDataGroupContainsChild(ClientDataGroup dataGroup, List<String> errorMessages,
			JsonValue childValue) {
		JsonString value = getStringValue(childValue);
		String nameInData = value.getStringValue();
		addErrorMessageIfChildIsMissing(dataGroup, nameInData, errorMessages);
	}

	private JsonArray extractChildren(JsonValue jsonValue) {
		JsonObject jsonObject = (JsonObject) jsonValue;
		return jsonObject.getValueAsJsonArray("children");
	}

	private void addErrorMessageIfChildIsMissing(ClientDataGroup dataGroup, String nameInData,
			List<String> errorMessages) {
		if (!dataGroup.containsChildWithNameInData(nameInData)) {
			errorMessages.add("Child with nameInData " + nameInData + " is missing.");
		}
	}

	private JsonString getStringValue(JsonValue childValue) {
		JsonObject child = (JsonObject) childValue;
		throwErrorIfMissingKey(child);
		return (JsonString) child.getValue("name");
	}

	private void throwErrorIfMissingKey(JsonObject child) {
		if (!child.containsKey("name")) {
			throw new JsonParseException("child must contain key: name");
		}
	}

}
