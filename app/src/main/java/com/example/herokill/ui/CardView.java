package com.example.herokill.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.herokill.R;
import com.example.herokill.model.Card;
import com.example.herokill.model.GameData;

public class CardView extends LinearLayout {
    private TextView nameText;
    private TextView valueText;
    private TextView descText;
    private Card card;
    private OnCardClickListener listener;

    public interface OnCardClickListener {
        void onCardClick(Card card);
    }

    public CardView(Context context) {
        super(context);
        init(context);
    }

    public CardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.card_view, this, true);
        nameText = findViewById(R.id.card_name);
        valueText = findViewById(R.id.card_value);
        descText = findViewById(R.id.card_desc);

        setOnClickListener(v -> {
            if (listener != null && card != null) {
                listener.onCardClick(card);
            }
        });
    }

    public void setCard(Card card) {
        this.card = card;
        updateView();
    }

    private void updateView() {
        if (card == null) return;

        nameText.setText(card.getName());
        valueText.setText(String.valueOf(card.getValue()));
        descText.setText(GameData.getCardDescription(card.getName()));

        switch (card.getType()) {
            case ATTACK:
                setBackgroundColor(getResources().getColor(R.color.secondary));
                break;
            case DEFENSE:
                setBackgroundColor(getResources().getColor(R.color.accent));
                break;
            case HEAL:
                setBackgroundColor(getResources().getColor(R.color.green));
                break;
            case SKILL:
                setBackgroundColor(getResources().getColor(R.color.primary_variant));
                break;
            default:
                setBackgroundColor(getResources().getColor(R.color.primary_variant));
        }
    }

    public void setOnCardClickListener(OnCardClickListener listener) {
        this.listener = listener;
    }

    public Card getCard() {
        return card;
    }
}