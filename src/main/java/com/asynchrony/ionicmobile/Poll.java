package com.asynchrony.ionicmobile;

import net.rambaldi.json.MutableObject;
import net.rambaldi.time.Timestamp;

import java.net.URI;

public class Poll extends MutableObject {
    public URI uri;
    public String simple_id;
    public String title;
    public int number_of_participants;
    public User owner;
    public boolean locked;
    public Timestamp created_at;
}
