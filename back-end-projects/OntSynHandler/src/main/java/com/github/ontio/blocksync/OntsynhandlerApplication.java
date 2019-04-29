package com.github.ontio.blocksync;

import com.github.ontio.blocksync.utils.ConfigParam;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@EnableAsync
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(value = "com.github.ontio.blocksync.mapper")
public class OntsynhandlerApplication {

    @Autowired
    private ConfigParam configParam;


    @Bean
    public AsyncTaskExecutor taskExecutor() {
        log.info("########taskExecutor#########");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(configParam.THREADPOOLSIZE_MAX);
        executor.setCorePoolSize(configParam.THREADPOOLSIZE_CORE);
        executor.setQueueCapacity(configParam.THREADPOOLSIZE_QUEUE);
        executor.setThreadNamePrefix("TxnHandlerThread--");
        executor.setKeepAliveSeconds(configParam.THREADPOOLSIZE_KEEPALIVE_SECOND);

        // Rejection policies
/*		executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				logger.error("###########reject thread....");
				// .....
			}
		});*/
        //调用者的线程会执行该任务,如果执行器已关闭,则丢弃
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();

        return executor;
    }


    public static void main(String[] args) {
        SpringApplication.run(OntsynhandlerApplication.class, args);
    }
}
