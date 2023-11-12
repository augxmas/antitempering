package org.mfds.util;

public class Constants {

	public static enum DrugTypes{
		ETC,		// 전문의약품,		// 0
		OTC, 		// 일반의약품,		// 1
		QUASI,		// 2
		HERB,		// 한약재 // 3
		API,		// 원료의약품,		// 4
		ETC_RARE	// 전문의약품_희귀	// 4	
	}
	
	public static enum METHOD{
		MANUFACTURE,	// 제조 0
		IMPORT,			// 수입 1
		EXPORT			// 수출 2
	}

	public static enum Modifier{
		NEW, 		// 신규등록,		// 0
		INGREDIANT,	// 주성분변경,		// 1
		WARNING,	// 주의사항변경,		// 2
		CHANGE		// 허가사항변경		// 3
	}

	public static enum CANCELLATION{
		APPROVAL,				// 정상
		EXPIRE,					// 유효기간만료,
		WITHDRAW,				// 취하,
		CLOSE,					// 폐업,
		ADMINISTRATIVECANCEL,	// 행정적 취소
		CANCEL					// 취소
	}

	
}
