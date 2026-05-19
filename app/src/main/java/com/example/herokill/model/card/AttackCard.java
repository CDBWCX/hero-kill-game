package com.example.herokill.model.card;

import com.example.herokill.model.Card;
import com.example.herokill.model.CardType;
import com.example.herokill.model.Hero;

public class AttackCard extends Card {
    public AttackCard(String name, int value) {
        super(name, CardType.ATTACK, value);
    }

    @Override
    public void execute(Hero user, Hero target) {
        int damage = user.getAttack() + value;
        target.takeDamage(damage);
    }
}