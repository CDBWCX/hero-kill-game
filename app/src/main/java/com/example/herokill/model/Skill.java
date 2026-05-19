package com.example.herokill.model;

public abstract class Skill {
    protected String name;
    protected SkillType type;
    protected int cooldown;
    protected int currentCooldown;

    public Skill(String name, SkillType type, int cooldown) {
        this.name = name;
        this.type = type;
        this.cooldown = cooldown;
        this.currentCooldown = 0;
    }

    public abstract void execute(Hero user, Hero target);

    public boolean isReady() {
        return currentCooldown == 0;
    }

    public void tickCooldown() {
        if (currentCooldown > 0) {
            currentCooldown--;
        }
    }

    public void resetCooldown() {
        currentCooldown = cooldown;
    }

    public String getName() { return name; }
    public SkillType getType() { return type; }
    public int getCooldown() { return cooldown; }
    public int getCurrentCooldown() { return currentCooldown; }
}

enum SkillType {
    POWER_STRIKE,
    HEALING,
    SHIELD,
    COUNTER,
    BERSERK
}