package se.uu.ub.cora.fitnesseintegration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import org.testng.annotations.Test;

import se.uu.ub.cora.data.DataGroup;
import se.uu.ub.cora.data.DataRecord;

public class RecordHolderTest {
	@Test
	public void testInit() {
		RecordHolder recordHolder = new RecordHolder();
		assertNotNull(recordHolder);
	}

	@Test
	public void testSetAndGetRecord() throws Exception {
		DataGroup clientDataGroup = DataGroup.withNameInData("someName");
		DataRecord clientDataRecord = DataRecord.withDataGroup(clientDataGroup);
		RecordHolder.setRecord(clientDataRecord);
		assertEquals(RecordHolder.getRecord(), clientDataRecord);
	}
}
