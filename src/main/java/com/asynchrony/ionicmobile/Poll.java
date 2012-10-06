package com.asynchrony.ionicmobile;

import net.rambaldi.process.Timestamp;

import java.net.URI;

public class Poll {
    public URI uri;
    public String simple_id;
    public String title;
    public int number_of_participants;
    public User owner;
    public boolean locked;
    public Timestamp created_at;
}
