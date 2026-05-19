package com.example.herokill;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.herokill.ai.AIController;
import com.example.herokill.game.GameManager;
import com.example.herokill.model.Card;
import com.example.herokill.model.CardType;
import com.example.herokill.model.Hero;
import com.example.herokill.ui.CardView;
import com.example.herokill.ui.HeroView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GameManager.GameListener {
    private GameManager gameManager;
    private AIController aiController;
    private Handler handler;

    private TextView turnText;
    private TextView turnCount;
    private TextView selectedCardText;
    private LinearLayout playerHandLayout;
    private LinearLayout playerTeamLayout;
    private LinearLayout enemyTeamLayout;
    private Button btnDraw;
    private Button btnEndTurn;
    private Button btnRestart;
    private ScrollView logScrollView;
    private TextView gameLog;

    private Card selectedCard;
    private Hero selectedHero;
    private StringBuilder logBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();
        gameManager = new GameManager(this);
        aiController = new AIController();
        logBuilder = new StringBuilder();

        initViews();
        setupClickListeners();
        updateUI();
        addLog("游戏开始！你是蜀国阵营，对抗魏国阵营！");
        gameManager.startTurn();
    }

    private void initViews() {
        turnText = findViewById(R.id.turn_text);
        turnCount = findViewById(R.id.turn_count);
        selectedCardText = findViewById(R.id.selected_card_text);
        playerHandLayout = findViewById(R.id.player_hand);
        playerTeamLayout = findViewById(R.id.player_team);
        enemyTeamLayout = findViewById(R.id.enemy_team);
        btnDraw = findViewById(R.id.btn_draw);
        btnEndTurn = findViewById(R.id.btn_end_turn);
        btnRestart = findViewById(R.id.btn_restart);
        logScrollView = findViewById(R.id.log_scroll);
        gameLog = findViewById(R.id.game_log);
    }

    private void setupClickListeners() {
        btnDraw.setOnClickListener(v -> {
            if (gameManager.getGameState() == GameManager.GameState.PLAYER_TURN) {
                drawCardForAllPlayers();
                addLog("你摸了2张牌");
                updateUI();
            }
        });

        btnEndTurn.setOnClickListener(v -> {
            if (gameManager.getGameState() == GameManager.GameState.PLAYER_TURN) {
                addLog("你结束了回合");
                endPlayerTurn();
            }
        });

        btnRestart.setOnClickListener(v -> {
            gameManager.initGame();
            selectedCard = null;
            selectedCardText.setText("选择一张牌使用");
            logBuilder = new StringBuilder();
            addLog("游戏重新开始！");
            updateUI();
            gameManager.startTurn();
        });

        setupHeroClickListeners();
    }

    private void setupHeroClickListeners() {
        for (int i = 0; i < playerTeamLayout.getChildCount(); i++) {
            View child = playerTeamLayout.getChildAt(i);
            if (child instanceof HeroView) {
                ((HeroView) child).setOnHeroClickListener(this::onHeroClick);
            }
        }

        for (int i = 0; i < enemyTeamLayout.getChildCount(); i++) {
            View child = enemyTeamLayout.getChildAt(i);
            if (child instanceof HeroView) {
                ((HeroView) child).setOnHeroClickListener(this::onHeroClick);
            }
        }
    }

    private void onHeroClick(Hero hero) {
        if (gameManager.getGameState() != GameManager.GameState.PLAYER_TURN) return;
        if (selectedCard == null) return;

        if (selectedCard.getType() == CardType.ATTACK && !hero.isPlayer()) {
            useCardOnTarget(hero);
        } else if (selectedCard.getType() == CardType.HEAL && hero.isPlayer()) {
            useCardOnTarget(hero);
        } else if (selectedCard.getType() == CardType.DEFENSE && hero.isPlayer()) {
            useCardOnTarget(hero);
        } else if (selectedCard.getType() == CardType.SKILL) {
            useCardOnTarget(hero);
        }
    }

    private void useCardOnTarget(Hero target) {
        Hero user = getFirstAlivePlayerHero();
        if (user != null) {
            String cardName = selectedCard.getName();
            gameManager.playCard(user, selectedCard, target);
            String actionText = user.isPlayer() ? "你使用" : target.isPlayer() ? "对" + target.getName() : target.getName();
            addLog(user.getName() + "使用【" + cardName + "】" + actionText);
            selectedCard = null;
            selectedCardText.setText("选择一张牌使用");
            updateUI();
        }
    }

    private Hero getFirstAlivePlayerHero() {
        for (Hero hero : gameManager.getPlayerTeam()) {
            if (hero.isAlive()) {
                return hero;
            }
        }
        return null;
    }

    private void drawCardForAllPlayers() {
        for (Hero hero : gameManager.getPlayerTeam()) {
            if (hero.isAlive()) {
                gameManager.drawCard(hero);
                gameManager.drawCard(hero);
            }
        }
    }

    private void endPlayerTurn() {
        gameManager.endTurn();
        executeAITurns();
    }

    private void executeAITurns() {
        btnDraw.setEnabled(false);
        btnEndTurn.setEnabled(false);

        handler.postDelayed(() -> {
            addLog("敌方回合开始");
            aiController.executeAllAITurns(
                    gameManager.getEnemyTeam(),
                    gameManager.getPlayerTeam(),
                    gameManager.getDeck()
            );
            addLog("敌方回合结束");
            updateUI();
            gameManager.endTurn();
        }, 1500);
    }

    private void updateUI() {
        updateTurnInfo();
        updatePlayerHand();
        updateHeroViews();
        updateButtons();
        scrollToBottom();
    }

    private void updateTurnInfo() {
        if (gameManager.getCurrentTeam() == GameManager.TEAM_PLAYER) {
            turnText.setText(R.string.your_turn);
            turnText.setTextColor(getResources().getColor(R.color.accent));
        } else {
            turnText.setText(R.string.enemy_turn);
            turnText.setTextColor(getResources().getColor(R.color.secondary));
        }
        turnCount.setText("回合 " + gameManager.getCurrentTurn());
    }

    private void updatePlayerHand() {
        playerHandLayout.removeAllViews();

        Hero player = getFirstAlivePlayerHero();
        if (player != null) {
            for (Card card : player.getHandCards()) {
                CardView cardView = new CardView(this);
                cardView.setCard(card);
                cardView.setOnCardClickListener(this::onCardClick);
                playerHandLayout.addView(cardView);
            }
        }
    }

    private void onCardClick(Card card) {
        if (gameManager.getGameState() != GameManager.GameState.PLAYER_TURN) return;

        selectedCard = card;
        selectedCardText.setText("选择目标: " + card.getName());
    }

    private void updateHeroViews() {
        List<Hero> playerTeam = gameManager.getPlayerTeam();
        List<Hero> enemyTeam = gameManager.getEnemyTeam();

        for (int i = 0; i < playerTeamLayout.getChildCount(); i++) {
            View child = playerTeamLayout.getChildAt(i);
            if (child instanceof HeroView && i < playerTeam.size()) {
                ((HeroView) child).setHero(playerTeam.get(i));
            }
        }

        for (int i = 0; i < enemyTeamLayout.getChildCount(); i++) {
            View child = enemyTeamLayout.getChildAt(i);
            if (child instanceof HeroView && i < enemyTeam.size()) {
                ((HeroView) child).setHero(enemyTeam.get(i));
            }
        }
    }

    private void updateButtons() {
        boolean isPlayerTurn = gameManager.getGameState() == GameManager.GameState.PLAYER_TURN;
        btnDraw.setEnabled(isPlayerTurn);
        btnEndTurn.setEnabled(isPlayerTurn);
    }

    private void addLog(String message) {
        logBuilder.append(message).append("\n");
        gameLog.setText(logBuilder.toString());
        scrollToBottom();
    }

    private void scrollToBottom() {
        logScrollView.post(() -> logScrollView.scrollTo(0, gameLog.getHeight()));
    }

    @Override
    public void onTurnChange(int team, int turn) {
        updateUI();
    }

    @Override
    public void onCardPlayed(Hero user, Card card, Hero target) {
        updateUI();
    }

    @Override
    public void onSkillUsed(Hero user, Hero target) {
        updateUI();
    }

    @Override
    public void onHeroDamaged(Hero hero, int damage) {
        addLog(hero.getName() + "受到" + damage + "点伤害");
        updateUI();
    }

    @Override
    public void onHeroHealed(Hero hero, int amount) {
        addLog(hero.getName() + "恢复了" + amount + "点生命");
        updateUI();
    }

    @Override
    public void onHeroDied(Hero hero) {
        addLog(hero.getName() + "阵亡了！");
        updateUI();
    }

    @Override
    public void onGameEnd(boolean playerWins) {
        String message = playerWins ? getString(R.string.victory) : getString(R.string.defeat);
        new AlertDialog.Builder(this)
                .setTitle("游戏结束")
                .setMessage(message)
                .setPositiveButton("重新开始", (dialog, which) -> {
                    gameManager.initGame();
                    selectedCard = null;
                    selectedCardText.setText("选择一张牌使用");
                    logBuilder = new StringBuilder();
                    addLog("游戏重新开始！");
                    updateUI();
                    gameManager.startTurn();
                })
                .setNegativeButton("退出", (dialog, which) -> finish())
                .show();
    }

    @Override
    public void onDeckEmpty() {
        addLog("牌堆已用尽！");
        new AlertDialog.Builder(this)
                .setTitle("牌堆空了")
                .setMessage("牌堆已用尽")
                .setPositiveButton("确定", null)
                .show();
    }
}