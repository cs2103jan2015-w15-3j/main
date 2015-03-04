package com.equinox;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SignalHandlerTest {

    @Test
    public void testSuccess() {
        String expected_empty = " operation is successful for !";
        Signal successSignal_empty = new Signal(Signal.SIGNAL_SUCCESS);

        String expected_one_param = "add operation is successful for !";
        Signal successSignal_one_param = new Signal(Signal.SIGNAL_SUCCESS,
                new String[] { "add" });

        String expected_two_params = "add operation is successful for my text!";
        Signal successSignal_two_params = new Signal(Signal.SIGNAL_SUCCESS,
                new String[] { "add", "my text" });
        
        assertEquals(expected_empty,
                SignalHandler.processSignal(successSignal_empty));

        assertEquals(expected_one_param,
                SignalHandler.processSignal(successSignal_one_param));

        assertEquals(expected_two_params,
                SignalHandler.processSignal(successSignal_two_params));
    }
}
