package com.example.herokill.model;

public abstract class Card {
    protected String name;
    protected CardType type;
    protected int value;

    public Card(String name, CardType type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public abstract void execute(Hero user, Hero target);

    public String getName() { return name; }
    public CardType getType() { return type; }
    public int getValue() { return value; }
}

enum CardType {
    ATTACK,
    DEFENSE,
    SKILL,
    HEAL
}