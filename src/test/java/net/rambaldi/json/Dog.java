package net.rambaldi.json;

public class Dog extends MutableObject {
    Dog() {}
    Dog(String name) {this.name = name;}
    public String name;
    public String owner;
    public int age;
}
