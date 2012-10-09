package net.rambaldi.json;

import com.asynchrony.ionicmobile.Poll;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTest {

    @Test
    public void can_create_parser() {
        assertNotNull(new Json(Poll.class));
    }

    @Test
    public void parser_can_create_object() {
        assertEquals(new Poll(),new Json(Poll.class));
    }

}
