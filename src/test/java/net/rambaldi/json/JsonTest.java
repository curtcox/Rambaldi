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
    public void Fido_is_named_Fido() {
        Dog fido = new Json<>(Dog.class).parse(json("{ 'name' : 'Fido'}"));
        assertEquals("Fido",fido.name);
    }

    @Test
    public void Fred_owns_Fido() {
        Dog fido = new Json<>(Dog.class).parse(json(
            "{ 'name' : 'Fido' 'owner' : 'Fred' }"
        ));
        assertEquals("Fido",fido.name);
        assertEquals("Fred",fido.owner);
    }

    @Test
    public void Thats_105_to_you_and_me() {
        Dog rex = new Json<>(Dog.class).parse(json(
                "{ 'age' : '105'}"
        ));
        assertEquals(105,rex.age);
    }

    @Test
    public void The_cat_is_dead() {
        Cat cat = new Json<>(Cat.class).parse(json(
             "{ 'dead' : 'true'}"
        ));
        assertTrue(cat.dead);
    }

    @Test
    public void Rover_is_named_Rover() {
        Dog rover = new Json<>(Dog.class).parse(json("{ 'name' : 'Rover'}"));
        assertEquals("Rover",rover.name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void You_cannot_teach_a_dog_Pascal() {
        Dog rover = new Json<>(Dog.class).parse(json("{ 'computer_languuages_mastered' : 'Pascal'}"));
        assertEquals("Rover",rover.name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void missing_colon_throws_exception() {
        new Json<>(Dog.class).parse(json("{ 'name' 'Rover'}"));
    }

    private String json(String s) {
        return s.replace("'","\"");
    }
}
