package net.rambaldi.json;

import com.asynchrony.ionicmobile.Poll;
import org.junit.Test;

import java.nio.file.attribute.DosFileAttributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MutableObjectTest {

    public static class Dog extends MutableObject {
        Dog() {}
        Dog(String name) {this.name = name;}
        public String name;
        public String owner;
    }

    public static class Cat extends MutableObject {}

    @Test
    public void can_create() {
        new MutableObject();
    }

    @Test
    public void unchanged_instances_are_equal() {
        assertEquals(new MutableObject(),new MutableObject());
    }

    @Test
    public void unchanged_instances_have_the_same_hash() {
        assertEquals(new MutableObject().hashCode(),new MutableObject().hashCode());
    }

    @Test
    public void dogs_with_different_names_are_different() {
        Dog rover = new Dog("Rover");
        Dog fido = new Dog("Fido");
        assertFalse(rover.equals(fido));
        assertFalse(fido.equals(rover));
    }

    @Test
    public void dogs_are_not_cats() {
        Dog dog = new Dog();
        Cat cat = new Cat();
        assertFalse(dog.equals(cat));
        assertFalse(cat.equals(dog));
    }

    @Test
    public void dogs_are_not_null() {
        assertFalse(new Dog().equals(null));
    }

    @Test
    public void toString_contains_class_name() {
        String dog = new Dog().toString();
        String cat = new Cat().toString();
        assertTrue(dog,dog.contains("Dog"));
        assertTrue(cat,cat.contains("Cat"));
    }

    @Test
    public void toString_contains_property_values() {
        String rover = new Dog("Rover").toString();
        String fido = new Dog("Fido").toString();
        assertTrue(rover,rover.contains("Rover"));
        assertTrue(fido,fido.contains("Fido"));
    }

    @Test
    public void toString_contains_property_names() {
        String rover = new Dog("Rover").toString();
        assertTrue(rover,rover.contains("name"));
        assertTrue(rover,rover.contains("owner"));
    }

}
