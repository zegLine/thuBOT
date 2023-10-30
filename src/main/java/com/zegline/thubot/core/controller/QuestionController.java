package com.zegline.thubot.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.zegline.thubot.core.model.Question;
import com.zegline.thubot.core.model.QuestionResponse;
import com.zegline.thubot.core.model.Response;
import com.zegline.thubot.core.repository.QuestionRepository;
import com.zegline.thubot.core.repository.QuestionResponseRepository;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

	@Autowired
	private QuestionRepository qr;

	@Autowired
	private QuestionResponseRepository qrr;

    @GetMapping("/get/{id}")
	public ResponseEntity<Object> question_get(@PathVariable("id") String id) {
		// Check for question existence
		Optional<Question> oq = qr.findById(id);
		if (!oq.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"question not found");
		}
		Question q = oq.get();

		// Put the question in the map
		Map<String, Object> map = new HashMap<>();
		map.put("question", q);

		// If there is a response defined, put it in the map
		List<QuestionResponse> qr = qrr.findByQuestion(q);
		if (!qr.isEmpty()) {
			Response r = qr.get(0).getResponse();
			map.put("response", r);
		}
		
		// Return map as response
		ResponseEntity<Object> re = new ResponseEntity<Object>(map, null, 200);

		return re;
	}

	@GetMapping("/create")
	public Question question_create(@RequestParam(value = "question") String question_input) {
		Question q = new Question(question_input);
		qr.save(q);
		return q;
	}

}
