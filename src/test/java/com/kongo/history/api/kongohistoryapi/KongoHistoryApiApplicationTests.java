package com.kongo.history.api.kongohistoryapi;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kongo.history.api.kongohistoryapi.validator.AddAuthorFormValidator;

@SpringBootTest
class KongoHistoryApiApplicationTests {

	@Autowired
	AddAuthorFormValidator addAuthorFormValidator;

	@Test
	void contextLoads() {
		
	}

}
