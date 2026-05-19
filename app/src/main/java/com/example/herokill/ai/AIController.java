package com.example.herokill.ai;

import com.example.herokill.model.Card;
import com.example.herokill.model.CardType;
import com.example.herokill.model.GameData;
import com.example.herokill.model.Hero;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIController {
    private Random random;

    public AIController() {
        random = new Random();
    }

    public void executeAITurn(Hero aiHero, List<Hero> playerTeam, List<Hero> enemyTeam, List<Card> deck) {
        if (!aiHero.isAlive()) return;

        List<Hero> alivePlayerHeroes = getAliveHeroes(playerTeam);
        List<Hero> aliveEnemyHeroes = getAliveHeroes(enemyTeam);

        if (alivePlayerHeroes.isEmpty()) return;

        for (Card card : new ArrayList<>(aiHero.getHandCards())) {
            if (!aiHero.isAlive()) break;

            if (card.getType() == CardType.ATTACK) {
                Hero target = selectTarget(alivePlayerHeroes);
                if (target != null) {
                    aiHero.useCard(card, target);
                }
            } else if (card.getType() == CardType.HEAL) {
                Hero target = selectHealTarget(aliveEnemyHeroes);
                if (target != null) {
                    aiHero.useCard(card, target);
                }
            } else if (card.getType() == CardType.DEFENSE) {
                aiHero.useCard(card, aiHero);
            }
        }

        GameData.drawCards(aiHero, deck, 2);
    }

    private List<Hero> getAliveHeroes(List<Hero> heroes) {
        List<Hero> alive = new ArrayList<>();
        for (Hero hero : heroes) {
            if (hero.isAlive()) {
                alive.add(hero);
            }
        }
        return alive;
    }

    private Hero selectTarget(List<Hero> targets) {
        if (targets.isEmpty()) return null;

        Hero weakest = targets.get(0);
        for (Hero hero : targets) {
            if (hero.getCurrentHp() < weakest.getCurrentHp()) {
                weakest = hero;
            }
        }

        if (random.nextFloat() < 0.3f) {
            return targets.get(random.nextInt(targets.size()));
        }
        return weakest;
    }

    private Hero selectHealTarget(List<Hero> targets) {
        if (targets.isEmpty()) return null;

        Hero mostHurt = targets.get(0);
        float lowestHealthPercent = (float) mostHurt.getCurrentHp() / mostHurt.getMaxHp();

        for (Hero hero : targets) {
            float healthPercent = (float) hero.getCurrentHp() / hero.getMaxHp();
            if (healthPercent < lowestHealthPercent) {
                lowestHealthPercent = healthPercent;
                mostHurt = hero;
            }
        }

        return mostHurt;
    }

    public void executeAllAITurns(List<Hero> aiTeam, List<Hero> playerTeam, List<Card> deck) {
        for (Hero hero : aiTeam) {
            if (hero.isAlive()) {
                executeAITurn(hero, playerTeam, aiTeam, deck);
            }
        }
    }
}