package com.github.ontio.txPush;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zhouq
 */
public class DisruptorEvent implements Serializable {

	public static final EventFactory<DisruptorEvent> FACTORY = DisruptorEvent::new;

	public static final EventHandler<DisruptorEvent> CLEANER = (event, sequence, endOfBatch) -> event.setEvent(null);

	@Getter
	@Setter
	private Object event;
}
