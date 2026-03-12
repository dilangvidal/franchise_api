package com.dilangvidal.franchise_api;

import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.controller.FranchiseController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;

@WebFluxTest(FranchiseController.class)
class FranchiseApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
