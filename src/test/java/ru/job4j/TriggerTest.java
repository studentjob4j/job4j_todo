package ru.job4j;

import org.junit.Test;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class TriggerTest {

    @Test
    public void whenSumm() {
        Trigger trigger = new Trigger();
        int res = trigger.summ(1, 1);
        assertThat(res, is(2));
    }

}