package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class AddressBook {

    @Id
    @GeneratedValue
    private Long id;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BuddyInfo> buddies;

    public AddressBook() {
        this.buddies = new ArrayList<>();
    }

    public List<BuddyInfo> getBuddies() {
        return buddies;
    }

    public void addBuddy(BuddyInfo buddy) {
        if(buddy != null) {
            buddies.add(buddy);
        }
    }

    public BuddyInfo getBuddy(int index) {
        if (index >= 0 && index < buddies.size()) {
            return buddies.get(index);
        }
        return null;
    }

    public BuddyInfo removeBuddy(int index){
        if (index >= 0 && index < buddies.size()) {
            return buddies.remove(index);
        }
        return null;
    }

    public void emptyAddressBook() {
        while(!buddies.isEmpty()){
            this.removeBuddy(0);
        }
    }

    public void printAddressBook() {
        for(BuddyInfo buddy: buddies){
            System.out.println(buddy);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
