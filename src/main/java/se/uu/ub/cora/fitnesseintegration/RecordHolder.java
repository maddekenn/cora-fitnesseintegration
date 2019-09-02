package se.uu.ub.cora.fitnesseintegration;

import se.uu.ub.cora.data.DataRecord;

public class RecordHolder {

	public RecordHolder() {
		// needed by fitnesse
		super();
	}

	private static DataRecord clientDataRecord;

	public static void setRecord(DataRecord clientDataRecord) {
		RecordHolder.clientDataRecord = clientDataRecord;
	}

	public static DataRecord getRecord() {
		return clientDataRecord;
	}

}
