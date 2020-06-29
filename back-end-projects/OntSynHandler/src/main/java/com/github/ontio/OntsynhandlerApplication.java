package com.github.ontio;

import com.github.ontio.utils.ConfigParam;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@MapperScan(value = "com.github.ontio.dao")
public class OntsynhandlerApplication {

	private static final Logger logger = LoggerFactory.getLogger(OntsynhandlerApplication.class);

	@Autowired
	private ConfigParam configParam;


	@Bean
	public AsyncTaskExecutor taskExecutor() {
		logger.info("########taskExecutor#########");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setMaxPoolSize(configParam.THREADPOOLSIZE_MAX);
		executor.setCorePoolSize(configParam.THREADPOOLSIZE_CORE);
		executor.setQueueCapacity(configParam.THREADPOOLSIZE_QUEUE);
		executor.setThreadNamePrefix("TxnHandlerThread--");
		executor.setKeepAliveSeconds(configParam.THREADPOOLSIZE_KEEPALIVE_SECOND);

		// Rejection policies
		executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				logger.error("###########reject thread....");
				// .....
			}
		});
		// executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.initialize();

		return executor;
	}




	public static void main(String[] args) {
		SpringApplication.run(OntsynhandlerApplication.class, args);
	}
}
