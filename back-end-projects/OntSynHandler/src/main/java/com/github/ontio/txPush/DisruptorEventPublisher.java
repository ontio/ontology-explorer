package com.github.ontio.txPush;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;

/**
 * @author zhouq
 */
public interface DisruptorEventPublisher {

	RingBuffer<DisruptorEvent> getRingBuffer();

	default <T> void publish(final T event) {
		getRingBuffer().publishEvent((disruptorEvent, sequence, e) -> disruptorEvent.setEvent(e), event);
	}

	default ThreadFactory threadFactory() {
		return r -> new Thread(r, getClass().getSimpleName() + "-disruptor-thread");
	}

	default WaitStrategy waitStrategy() {
		return new BlockingWaitStrategy();
	}

	default Disruptor<DisruptorEvent> createDisruptor(int bufferSize, ProducerType producerType) {
		return new Disruptor<>(DisruptorEvent.FACTORY, bufferSize, threadFactory(), producerType, waitStrategy());
	}

}
