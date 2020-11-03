package com.aeta.competition;

import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CompetitionApplication.class)
class CompetitionApplicationTests implements ApplicationContextAware {

	private ApplicationContextAware applicationContextAware;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContextAware = applicationContextAware;
	}



	@Test
	void contextLoads() {
	}
}
