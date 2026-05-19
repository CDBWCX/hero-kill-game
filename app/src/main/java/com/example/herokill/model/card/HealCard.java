package com.example.herokill.model.card;

import com.example.herokill.model.Card;
import com.example.herokill.model.CardType;
import com.example.herokill.model.Hero;

public class HealCard extends Card {
    public HealCard(String name, int value) {
        super(name, CardType.HEAL, value);
    }

    @Override
    public void execute(Hero user, Hero target) {
        target.heal(value);
    }
}