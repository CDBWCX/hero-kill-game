package com.example.herokill.model.card;

import com.example.herokill.model.Card;
import com.example.herokill.model.CardType;
import com.example.herokill.model.Hero;

public class DefenseCard extends Card {
    public DefenseCard(String name, int value) {
        super(name, CardType.DEFENSE, value);
    }

    @Override
    public void execute(Hero user, Hero target) {
        user.setDefense(user.getDefense() + value);
    }
}