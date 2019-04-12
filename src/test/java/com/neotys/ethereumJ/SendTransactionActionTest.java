package com.neotys.ethereumJ;

import static org.junit.Assert.assertEquals;

import com.neotys.ethereumJ.CustomActions.SendTransactionAction;
import org.junit.Test;

public class SendTransactionActionTest {
	@Test
	public void shouldReturnType() {
		final SendTransactionAction action = new SendTransactionAction();
		assertEquals("SendTransaction", action.getType());
	}

}
