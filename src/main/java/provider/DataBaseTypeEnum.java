package provider;

public enum DataBaseTypeEnum {
	ORACLE("oracle"),
	MICROSOFT_SQL_SERVER("sqlserver"),
	MYSQL("mysql");

	private String value;
	DataBaseTypeEnum(String value) {
		this.value = value;
	}

	public String stringValue() {
		return this.value;
	}
}
