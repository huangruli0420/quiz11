package com.example.quiz11.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.quiz11.constants.QuesType;
import com.example.quiz11.constants.ResMessage;
import com.example.quiz11.entity.Ques;
import com.example.quiz11.entity.Quiz;
import com.example.quiz11.repository.QuesDao;
import com.example.quiz11.repository.QuizDao;
import com.example.quiz11.service.ifs.QuizService;
import com.example.quiz11.vo.BasicRes;
import com.example.quiz11.vo.CreateUpdateReq;
import com.example.quiz11.vo.DeleteReq;
import com.example.quiz11.vo.SearchReq;
import com.example.quiz11.vo.SearchRes;

@Service
public class QuizServiceImpl implements QuizService {

	@Autowired
	private QuizDao quizDao;

	@Autowired
	private QuesDao quesDao;

	// 新增問卷
	@Transactional
	@Override
	public BasicRes create(CreateUpdateReq req) {
		// 檢查新增問卷時，id 要為 0，新增問卷時才需要
		if (req.getId() != 0) {
			return new BasicRes(ResMessage.QUIZ_PARAM_ERROR.getCode(), //
					ResMessage.QUIZ_PARAM_ERROR.getMessage());
		}
		// 參數檢查，獨立成一個方法
		BasicRes checkResult = checkParams(req);
		if (checkResult != null) {
			return checkResult;
		}
		// Spring Data JPA 的 save() 方法會回傳帶有主鍵（id）的 Quiz 實體，這個方法PK存在會update，不存在會insert
		// 因為 quiz 的 PK 是流水號，不會重複寫入，所以不用檢查資料庫是否已存在相同的 PK
		// 新增問卷
		// 因為 Quiz 中的 id 是 AI (自動生成的流水號)，要讓 quizDao 執行 save 後可以把該 id 的值回傳，
		// 必須要在 Quiz 此 Entity 中將資料型態為 int 的屬性 id
		// 加上 @GeneratedValue(strategy = GenerationType.IDENTITY)
		Quiz quizRes = quizDao.save(new Quiz(req.getName(), req.getDescription(), req.getStartDate(), //
				req.getEndDate(), req.isPublished()));
		// 將上面回傳的 quiz (問卷) 中的 id 設定成每一個 ques (問題)的 quiz_id，代表這些問題屬於這個問卷
		int quizId = quizRes.getId();
		for (Ques item : req.getQuesList()) {
			item.setQuizId(quizId);
		}
		// 新增所有已加入 quiz_id 的問題到 名稱為 ques 的 Table
		quesDao.saveAll(req.getQuesList());
		// 成功
		return new BasicRes(ResMessage.SUCCESS.getCode(), //
				ResMessage.SUCCESS.getMessage());
	}

	// 參數檢查，獨立成一個方法
	private BasicRes checkParams(CreateUpdateReq req) {

		// 檢查問卷名稱跟問卷敘述是否有填寫
		if (!StringUtils.hasText(req.getName()) || !StringUtils.hasText(req.getDescription())) {
			return new BasicRes(ResMessage.QUIZ_PARAM_ERROR.getCode(), //
					ResMessage.QUIZ_PARAM_ERROR.getMessage());
		}
		// 檢查有無問卷時間，以及開始時間不能比結束時間晚
		if (req.getStartDate() == null || req.getEndDate() == null //
				|| req.getStartDate().isAfter(req.getEndDate())) {
			return new BasicRes(ResMessage.DATE_ERROR.getCode(), ResMessage.DATE_ERROR.getMessage());
		}
		// 檢查開始時間不能比今天早 (問卷的開始時間最晚只能是今天)
		if (req.getStartDate().isBefore(LocalDate.now())) {
			return new BasicRes(ResMessage.DATE_ERROR.getCode(), ResMessage.DATE_ERROR.getMessage());
		}
		// 檢查 Ques，問題的陣列裡面是否有問題，問題名稱，問題類型，問題選項
		for (Ques item : req.getQuesList()) {
			if (item.getQuesId() <= 0 || !StringUtils.hasText(item.getQuesName()) //
					|| !StringUtils.hasText(item.getType())) { //
				return new BasicRes(ResMessage.QUES_PARAM_ERROR.getCode(), //
						ResMessage.QUES_PARAM_ERROR.getMessage());
			}
			// 檢查題目類型 : 單選(single)、多選(multi)、文字(text)
			if (!QuesType.checkType(item.getType())) {
				return new BasicRes(ResMessage.QUES_TYPE_ERROR.getCode(), //
						ResMessage.QUES_TYPE_ERROR.getMessage());
			}
			// 檢查非文字類型時，選項沒有值，QuesType.TEXT.toString()返回的是枚舉的定義名稱，全大寫TEXT
			if (!item.getType().equalsIgnoreCase(QuesType.TEXT.toString()) && //
					!StringUtils.hasText(item.getOptions())) {
				return new BasicRes(ResMessage.QUES_TYPE_ERROR.getCode(), //
						ResMessage.QUES_TYPE_ERROR.getMessage());
			}
		}
		// 成功
		return null;
	}

	// 練習，修改問卷，因為有操作到兩次 Dao 要加上 Transactional
	@Transactional
	@Override
	public BasicRes update(CreateUpdateReq req) {
		// 檢查更新問卷時，因為問卷已存在在資料庫中，所以 id 不能為 0
		if (req.getId() == 0) {
			return new BasicRes(ResMessage.QUIZ_PARAM_ERROR.getCode(), //
					ResMessage.QUIZ_PARAM_ERROR.getMessage());
		}
		// 參數檢查，獨立成一個方法
		BasicRes checkResult = checkParams(req);
		if (checkResult != null) {
			return checkResult;
		}
		// 檢查 Ques 的 quiz_id 是否與 Quiz 的 id 相符
		int quizId = req.getId();
		for (Ques item : req.getQuesList()) {
			if (item.getQuizId() != quizId) {
				return new BasicRes(ResMessage.QUIZID_MISMATCH.getCode(), //
						ResMessage.QUIZID_MISMATCH.getMessage());
			}
		}
		// 問卷可以更新的狀態: 1.未發布; 2.已發布但尚未開始
		Optional<Quiz> op = quizDao.findById(quizId);
		// 確認問卷是否存在
		if (op.isEmpty()) { // op.isEmpty() == true 時，表示資料不存在
			return new BasicRes(ResMessage.QUIZ_NOT_FOUND.getCode(), //
					ResMessage.QUIZ_NOT_FOUND.getMessage());
		}
		// 取得問卷 (資料庫中的資料)
		Quiz quiz = op.get();
		// 確認問卷是否可以進行更新
		// 尚未發布: !quiz.isPublished();
		// 已發布但尚未開始: quiz.isPublished() && req.getStartDate().isAfter(LocalDate.now())
		// || 右邊的小括號是分組用，雖然 && 的 運算順序會比 || 高但為了方便分別 
		// 排除法: 所以整個邏輯式前面加上! 表示反向
		if (!(!quiz.isPublished() || (quiz.isPublished() && req.getStartDate().isAfter(LocalDate.now())))) {
			return new BasicRes(ResMessage.QUIZ_UPDATE_FAILED.getCode(), //
					ResMessage.QUIZ_UPDATE_FAILED.getMessage());
		}
		
		// 將 req 中的值 set 回從資料庫取出的 quiz 裡面 : id 不需要 set，因為一樣
		quiz.setName(req.getName());
		quiz.setDescription(req.getDescription());
		quiz.setStartDate(req.getStartDate());
		quiz.setEndDate(req.getEndDate());
		quiz.setPublished(req.isPublished());
		// 更新問卷
		quizDao.save(quiz);
		// 先刪除相同 quiz_id 的問卷所有問題，再新增
		quesDao.deleteByQuizId(quizId);
		quesDao.saveAll(req.getQuesList());
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 刪除問卷
	@Override
	public BasicRes delete (DeleteReq req) {
		// 刪除問卷
		quizDao.deleteByIdIn(req.getQuizIdList());
		// 刪除相同 quiz_id 問卷的所有問題
		quesDao.deleteByQuizIdIn(req.getQuizIdList());
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	@Override
	public SearchRes search(SearchReq req) {
		// 檢視條件
		String name = req.getName(); 
		// 若 name = null 或 空字串 或 全空白字串，一律都轉換成空字串
		if (!StringUtils.hasText(name) ) {
			name = "";
		}
		// 若沒有開始日期條件，將日期轉換成很早的時間
		LocalDate startDate = req.getStartDate();
		if (startDate == null) {
			startDate = LocalDate.of(1970, 1, 1);
		}
		// 若沒有結束日期條件，將日期轉換成很長遠的未來的時間
		LocalDate endDate = req.getEndDate();
		if (endDate == null) {
			endDate = LocalDate.of(9999, 12, 31);
		}
		List<Quiz> quizList = quizDao.getByConditions(name, startDate, endDate);
		return new SearchRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(),//
				quizList);
	}

}
