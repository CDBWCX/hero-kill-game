package com.example.herokill.model;

import java.util.ArrayList;
import java.util.List;

public class Hero {
    private String name;
    private int maxHp;
    private int currentHp;
    private int attack;
    private int defense;
    private List<Skill> skills;
    private List<Card> handCards;
    private boolean isAlive;
    private boolean isPlayer;
    private int position;

    public Hero(String name, int maxHp, int attack, int defense, List<Skill> skills) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.skills = skills != null ? skills : new ArrayList<>();
        this.handCards = new ArrayList<>();
        this.isAlive = true;
        this.isPlayer = false;
        this.position = 0;
    }

    public void takeDamage(int damage) {
        int actualDamage = Math.max(0, damage - defense);
        currentHp -= actualDamage;
        if (currentHp <= 0) {
            currentHp = 0;
            isAlive = false;
        }
    }

    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public void addCard(Card card) {
        handCards.add(card);
    }

    public void removeCard(Card card) {
        handCards.remove(card);
    }

    public boolean useCard(Card card, Hero target) {
        if (handCards.contains(card)) {
            card.execute(this, target);
            removeCard(card);
            return true;
        }
        return false;
    }

    public boolean hasSkill(SkillType type) {
        for (Skill skill : skills) {
            if (skill.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public String getName() { return name; }
    public int getMaxHp() { return maxHp; }
    public int getCurrentHp() { return currentHp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public List<Skill> getSkills() { return skills; }
    public List<Card> getHandCards() { return handCards; }
    public boolean isAlive() { return isAlive; }
    public boolean isPlayer() { return isPlayer; }
    public int getPosition() { return position; }

    public void setCurrentHp(int currentHp) { this.currentHp = currentHp; }
    public void setAttack(int attack) { this.attack = attack; }
    public void setDefense(int defense) { this.defense = defense; }
    public void setAlive(boolean alive) { isAlive = alive; }
    public void setPlayer(boolean player) { isPlayer = player; }
    public void setPosition(int position) { this.position = position; }
}