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
import se.uu.ub.cora.json.parser.JsonString;
import se.uu.ub.cora.json.parser.JsonValue;

public class ChildComparerImp implements ChildComparer {

	@Override
	public boolean checkDataGroupContainsChildren(ClientDataGroup dataGroup, JsonValue jsonValue) {
		return dataGroupContainsChildren(dataGroup, jsonValue).isEmpty();
	}

	private JsonArray extractChildren(JsonValue jsonValue) {
		JsonObject jsonObject = (JsonObject) jsonValue;
		JsonArray children = jsonObject.getValueAsJsonArray("children");
		return children;
	}

	@Override
	public List<String> dataGroupContainsChildren(ClientDataGroup dataGroup, JsonValue jsonValue) {
		List<String> errorMessages = new ArrayList<>();
		for (JsonValue childValue : extractChildren(jsonValue)) {
			JsonObject child = (JsonObject) childValue;
			JsonString value = (JsonString) child.getValue("name");
			String nameInData = value.getStringValue();
			if (!dataGroup.containsChildWithNameInData(nameInData)) {
				errorMessages.add("child with nameInData " + nameInData + " is missing");
			}
		}
		return errorMessages;
	}

}
