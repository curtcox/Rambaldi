package net.rambaldi.json;

import com.asynchrony.ionicmobile.Poll;
import org.junit.Test;

import java.util.AbstractCollection;

import static org.junit.Assert.*;

public class JsonTest {

    @Test
    public void can_create_parser() {
        assertNotNull(new Json(Dog.class));
    }

    @Test(expected = NullPointerException.class)
    public void class_must_be_supplied() {
        assertNotNull(new Json(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void class_must_be_instantiable() {
        assertNotNull(new Json(AbstractCollection.class));
    }

    @Test
    public void parser_can_create_object() {
        assertEquals(new Dog(),new Json(Dog.class).parse("{}"));
        assertEquals(new Cat(),new Json(Cat.class).parse("{}"));
    }

    @Test
    public void string_properties_will_be_assigned() {
        Dog dog = new Json<>(Dog.class).parse(json("{ 'name' : 'Fido'}"));
        assertEquals("Fido",dog.name);
    }

    private String json(String s) {
        return s.replace("'","\"");
    }
}
