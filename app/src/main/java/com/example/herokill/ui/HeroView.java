package com.example.herokill.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.herokill.R;
import com.example.herokill.model.GameData;
import com.example.herokill.model.Hero;

public class HeroView extends LinearLayout {
    private TextView nameText;
    private TextView descText;
    private ProgressBar hpBar;
    private TextView hpText;
    private TextView attackText;
    private TextView defenseText;
    private LinearLayout handCardsLayout;
    private ImageView statusIcon;
    private Hero hero;
    private OnHeroClickListener listener;

    public interface OnHeroClickListener {
        void onHeroClick(Hero hero);
    }

    public HeroView(Context context) {
        super(context);
        init(context);
    }

    public HeroView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HeroView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.hero_view, this, true);
        nameText = findViewById(R.id.hero_name);
        descText = findViewById(R.id.hero_desc);
        hpBar = findViewById(R.id.hp_bar);
        hpText = findViewById(R.id.hp_text);
        attackText = findViewById(R.id.attack_text);
        defenseText = findViewById(R.id.defense_text);
        handCardsLayout = findViewById(R.id.hand_cards);
        statusIcon = findViewById(R.id.status_icon);

        setOnClickListener(v -> {
            if (listener != null && hero != null) {
                listener.onHeroClick(hero);
            }
        });
    }

    public void setHero(Hero hero) {
        this.hero = hero;
        updateView();
    }

    public void updateView() {
        if (hero == null) return;

        nameText.setText(hero.getName());
        descText.setText(GameData.getHeroDescription(hero.getName()));
        hpBar.setMax(hero.getMaxHp());
        hpBar.setProgress(hero.getCurrentHp());
        hpText.setText(hero.getCurrentHp() + "/" + hero.getMaxHp());
        attackText.setText("攻击: " + hero.getAttack());
        defenseText.setText("防御: " + hero.getDefense());

        if (hero.isPlayer()) {
            setBackgroundColor(getResources().getColor(R.color.primary));
        } else {
            setBackgroundColor(getResources().getColor(R.color.secondary_variant));
        }

        if (!hero.isAlive()) {
            setAlpha(0.5f);
            statusIcon.setImageResource(R.drawable.ic_dead);
            statusIcon.setVisibility(View.VISIBLE);
        } else {
            setAlpha(1.0f);
            statusIcon.setVisibility(View.GONE);
        }

        updateHandCards();
    }

    private void updateHandCards() {
        handCardsLayout.removeAllViews();
        int cardCount = hero.getHandCards().size();

        for (int i = 0; i < Math.min(cardCount, 5); i++) {
            View cardView = new View(getContext());
            cardView.setBackgroundColor(getResources().getColor(R.color.accent));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 30);
            params.setMargins(2, 0, 2, 0);
            cardView.setLayoutParams(params);
            handCardsLayout.addView(cardView);
        }

        if (cardCount > 5) {
            TextView moreText = new TextView(getContext());
            moreText.setText("+" + (cardCount - 5));
            moreText.setTextColor(Color.WHITE);
            handCardsLayout.addView(moreText);
        }
    }

    public void setOnHeroClickListener(OnHeroClickListener listener) {
        this.listener = listener;
    }

    public Hero getHero() {
        return hero;
    }
}