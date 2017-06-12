package util;

import java.util.HashMap;
import java.util.Map;

public class Enums {
	public enum Users{
		ADMIN("1"),
		PROF("2"),
		DOC("3"),
		Y_DOC("4"),
		ARASTIRMA_GOREVLISI("5"),
		TEZLI_Y_L("6"),
		TEZSIZ_Y_L("7"),
		DOKTORA("8");

		private String value;

		private Users(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static Users parse(String value) {
			for (Users type : Users.values()) {
				if (value == type.getValue()) {
					return type;
				}
			}
			return ADMIN;
		}
	}
	
	public static Map<String,String> getUserTypes(){
		Map<String,String> users = new HashMap<>();
		users.put("1", "Admin");
		users.put("2", "Prof");
		users.put("3", "Doç");
		users.put("4", "Y.Doç");
		users.put("5", "Arastirma Görevlisi");
		users.put("6", "Tezli Y.L");
		users.put("7", "Tezsiz Y.L");
		users.put("8", "Doktora");
		return users;
	}
}
