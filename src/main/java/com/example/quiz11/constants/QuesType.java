package com.example.quiz11.constants;

public enum QuesType {

	SINGLE("single"), //
	MULTI("multi"), //
	TEXT("text"), //
	;

	private String type;

	private QuesType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	// 把數據檢查的一部份方法抽過來這裡寫，因為 enum 無法 new 出來，只能把這個方法定義成全域 static
	public static boolean checkType(String type) {
//		if (type.equalsIgnoreCase(QuesType.SINGLE.toString()) //
//				|| type.equalsIgnoreCase(QuesType.MULTI.toString()) //
//				|| type.equalsIgnoreCase(QuesType.TEXT.toString())) {
//			return true;
//		}

		// 上面那段邏輯可以用以下的方法來替換:
		// QuesType.values() 可以取得 QuesType 此 enum 中所有的 type
		for(QuesType item: QuesType.values()) {
			if(item.getType().equalsIgnoreCase(type)) {
				return true;
			}
			
		}
		return false;
	}

}
