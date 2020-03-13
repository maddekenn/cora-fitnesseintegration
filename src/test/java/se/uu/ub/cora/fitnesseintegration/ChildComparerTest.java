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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import se.uu.ub.cora.clientdata.ClientDataAtomic;
import se.uu.ub.cora.clientdata.ClientDataGroup;
import se.uu.ub.cora.json.parser.JsonParser;
import se.uu.ub.cora.json.parser.JsonValue;
import se.uu.ub.cora.json.parser.org.OrgJsonParser;

public class ChildComparerTest {

	private ChildComparer childComparer;
	private JsonParser jsonParser;
	private ClientDataGroup dataGroup;

	@BeforeMethod
	public void setUp() {
		childComparer = new ChildComparerImp();
		dataGroup = ClientDataGroup.withNameInData("someDataGroup");
		dataGroup.addChild(ClientDataAtomic.withNameInDataAndValue("workoutName", "cirkelfys"));
		jsonParser = new OrgJsonParser();

	}

	@Test
	public void testCheckContainOKWhenOneChild() {
		JsonValue jsonValue = jsonParser.parseString("{\"children\":[{\"name\":\"workoutName\"}]}");
		List<String> errorMessages = childComparer.dataGroupContainsChildren(dataGroup, jsonValue);
		assertTrue(errorMessages.isEmpty());
		boolean containsChildren = childComparer.checkDataGroupContainsChildren(dataGroup,
				jsonValue);
		assertTrue(containsChildren);

	}

	@Test
	public void testContainOKWhenOneChild() {
		JsonValue jsonValue = jsonParser.parseString("{\"children\":[{\"name\":\"workoutName\"}]}");
		List<String> errorMessages = childComparer.dataGroupContainsChildren(dataGroup, jsonValue);
		assertTrue(errorMessages.isEmpty());

	}

	@Test
	public void testCheckContainOKWhenMoreThanOneChild() {
		dataGroup.addChild(ClientDataAtomic.withNameInDataAndValue("instructorId", "45"));

		JsonValue jsonValue = jsonParser.parseString(
				"{\"children\":[{\"name\":\"workoutName\"},{\"name\":\"instructorId\"}]}");
		boolean containsChildren = childComparer.checkDataGroupContainsChildren(dataGroup,
				jsonValue);
		assertTrue(containsChildren);

	}

	@Test
	public void testContainOKWhenMoreThanOneChild() {
		dataGroup.addChild(ClientDataAtomic.withNameInDataAndValue("instructorId", "45"));
		JsonValue jsonValue = jsonParser.parseString(
				"{\"children\":[{\"name\":\"workoutName\"},{\"name\":\"instructorId\"}]}");
		List<String> errorMessages = childComparer.dataGroupContainsChildren(dataGroup, jsonValue);
		assertTrue(errorMessages.isEmpty());
	}

	@Test
	public void testCheckContainNotOKNoChildExistInDataGroup() {
		dataGroup = ClientDataGroup.withNameInData("someDataGroup");
		JsonValue jsonValue = jsonParser.parseString("{\"children\":[{\"name\":\"workoutName\"}]}");
		List<String> errorMessages = childComparer.dataGroupContainsChildren(dataGroup, jsonValue);
		assertEquals(errorMessages.size(), 1);
		assertEquals(errorMessages.get(0), "child with nameInData workoutName is missing");

		boolean containsChildren = childComparer.checkDataGroupContainsChildren(dataGroup,
				jsonValue);
		assertFalse(containsChildren);
	}

	@Test
	public void testContainNotOKNoChildExistInDataGroup() {
		dataGroup = ClientDataGroup.withNameInData("someDataGroup");
		JsonValue jsonValue = jsonParser.parseString("{\"children\":[{\"name\":\"workoutName\"}]}");
		List<String> errorMessages = childComparer.dataGroupContainsChildren(dataGroup, jsonValue);
		assertEquals(errorMessages.size(), 1);
		assertEquals(errorMessages.get(0), "child with nameInData workoutName is missing");

	}

	@Test
	public void testCheckContainOneButNotTheOther() {
		JsonValue jsonValue = jsonParser.parseString(
				"{\"children\":[{\"name\":\"workoutName\"},{\"name\":\"instructorId\"}]}");
		boolean containsChildren = childComparer.checkDataGroupContainsChildren(dataGroup,
				jsonValue);
		assertFalse(containsChildren);

	}

	@Test
	public void testContainOneButNotTheOther() {
		JsonValue jsonValue = jsonParser.parseString(
				"{\"children\":[{\"name\":\"workoutName\"},{\"name\":\"instructorId\"}]}");
		List<String> errorMessages = childComparer.dataGroupContainsChildren(dataGroup, jsonValue);
		assertEquals(errorMessages.size(), 1);
		assertEquals(errorMessages.get(0), "child with nameInData instructorId is missing");

	}

}
