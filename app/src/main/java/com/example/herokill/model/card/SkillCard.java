package com.example.herokill.model.card;

import com.example.herokill.model.Card;
import com.example.herokill.model.CardType;
import com.example.herokill.model.Hero;

public class SkillCard extends Card {
    public SkillCard(String name, int value) {
        super(name, CardType.SKILL, value);
    }

    @Override
    public void execute(Hero user, Hero target) {
        user.setAttack(user.getAttack() + value);
        target.takeDamage(value);
    }
}