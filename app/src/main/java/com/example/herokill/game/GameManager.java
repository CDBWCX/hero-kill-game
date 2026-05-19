package com.example.herokill.game;

import com.example.herokill.model.Card;
import com.example.herokill.model.GameData;
import com.example.herokill.model.Hero;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
    private List<Hero> playerTeam;
    private List<Hero> enemyTeam;
    private List<Card> deck;
    private int currentTurn;
    private int currentTeam;
    private GameState gameState;
    private GameListener listener;

    public static final int TEAM_PLAYER = 0;
    public static final int TEAM_ENEMY = 1;
    public static final int INITIAL_HAND_SIZE = 4;

    public interface GameListener {
        void onTurnChange(int team, int turn);
        void onCardPlayed(Hero user, Card card, Hero target);
        void onSkillUsed(Hero user, Hero target);
        void onHeroDamaged(Hero hero, int damage);
        void onHeroHealed(Hero hero, int amount);
        void onHeroDied(Hero hero);
        void onGameEnd(boolean playerWins);
        void onDeckEmpty();
    }

    public GameManager(GameListener listener) {
        this.listener = listener;
        initGame();
    }

    public void initGame() {
        playerTeam = new ArrayList<>();
        enemyTeam = new ArrayList<>();
        deck = GameData.createDeck();
        GameData.shuffleDeck(deck);
        currentTurn = 0;
        currentTeam = TEAM_PLAYER;
        gameState = GameState.PLAYING;

        List<Hero> allHeroes = GameData.createHeroes();
        for (int i = 0; i < 4; i++) {
            Hero hero = allHeroes.get(i);
            hero.setPlayer(true);
            hero.setPosition(i);
            playerTeam.add(hero);
            GameData.drawCards(hero, deck, INITIAL_HAND_SIZE);
        }

        for (int i = 4; i < 8; i++) {
            Hero hero = allHeroes.get(i);
            hero.setPlayer(false);
            hero.setPosition(i);
            enemyTeam.add(hero);
            GameData.drawCards(hero, deck, INITIAL_HAND_SIZE);
        }
    }

    public void startTurn() {
        currentTurn++;
        if (listener != null) {
            listener.onTurnChange(currentTeam, currentTurn);
        }

        if (currentTeam == TEAM_PLAYER) {
            gameState = GameState.PLAYER_TURN;
        } else {
            gameState = GameState.ENEMY_TURN;
        }
    }

    public void endTurn() {
        resetDefenses();
        currentTeam = currentTeam == TEAM_PLAYER ? TEAM_ENEMY : TEAM_PLAYER;
        startTurn();
    }

    private void resetDefenses() {
        for (Hero hero : playerTeam) {
            if (hero.isAlive()) {
                hero.setDefense(2);
            }
        }
        for (Hero hero : enemyTeam) {
            if (hero.isAlive()) {
                hero.setDefense(2);
            }
        }
    }

    public boolean playCard(Hero user, Card card, Hero target) {
        if (gameState != GameState.PLAYER_TURN) return false;
        if (!user.isPlayer()) return false;
        if (!user.isAlive()) return false;

        boolean success = user.useCard(card, target);
        if (success) {
            if (listener != null) {
                listener.onCardPlayed(user, card, target);
                if (!target.isAlive()) {
                    listener.onHeroDied(target);
                }
            }
            checkGameEnd();
        }
        return success;
    }

    public void useSkill(Hero user, Hero target) {
        if (!user.isAlive()) return;
        // Skill usage logic would be implemented here
        if (listener != null) {
            listener.onSkillUsed(user, target);
            checkGameEnd();
        }
    }

    public void drawCard(Hero hero) {
        if (deck.isEmpty()) {
            if (listener != null) {
                listener.onDeckEmpty();
            }
            return;
        }
        GameData.drawCards(hero, deck, 1);
    }

    private void checkGameEnd() {
        boolean playerAllDead = true;
        for (Hero hero : playerTeam) {
            if (hero.isAlive()) {
                playerAllDead = false;
                break;
            }
        }

        boolean enemyAllDead = true;
        for (Hero hero : enemyTeam) {
            if (hero.isAlive()) {
                enemyAllDead = false;
                break;
            }
        }

        if (playerAllDead) {
            gameState = GameState.ENEMY_WINS;
            if (listener != null) {
                listener.onGameEnd(false);
            }
        } else if (enemyAllDead) {
            gameState = GameState.PLAYER_WINS;
            if (listener != null) {
                listener.onGameEnd(true);
            }
        }
    }

    public List<Hero> getPlayerTeam() { return playerTeam; }
    public List<Hero> getEnemyTeam() { return enemyTeam; }
    public List<Card> getDeck() { return deck; }
    public int getCurrentTurn() { return currentTurn; }
    public int getCurrentTeam() { return currentTeam; }
    public GameState getGameState() { return gameState; }

    public enum GameState {
        PLAYING,
        PLAYER_TURN,
        ENEMY_TURN,
        PLAYER_WINS,
        ENEMY_WINS
    }
}