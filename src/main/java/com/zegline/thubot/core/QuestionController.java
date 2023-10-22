package com.zegline.thubot.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class QuestionController {

	@Autowired
	private QuestionRepository qr;

	@Autowired
	private QuestionResponseRepository qrr;

    @GetMapping("/questions")
	public ResponseEntity<Object> question(@RequestParam(value = "id") long id) {
		Optional<Question> oq = qr.findById(id);
		if (!oq.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"question not found");
		}
		Question q = oq.get();
		QuestionResponse qr = qrr.findByQuestion(q).get(0);
		Response r = qr.getResponse();

		Map<String, Object> map = new HashMap<>();
		map.put("question", q);
		map.put("response", r);

		ResponseEntity<Object> re = new ResponseEntity<Object>(map, null, 200);

		return re;
	}

	@GetMapping("/questions/create")
	public Question question(@RequestParam(value = "question") String question_input) {
		Question q = new Question(question_input);
		qr.save(q);
		return q;
	}

}
