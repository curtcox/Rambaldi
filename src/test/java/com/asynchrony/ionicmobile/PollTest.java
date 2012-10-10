package com.asynchrony.ionicmobile;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PollTest {

    @Test
    public void can_create() {
        new Poll();
    }

    @Test
    public void unchanged_instances_are_equal() {
        assertEquals(new Poll(),new Poll());
    }
}
