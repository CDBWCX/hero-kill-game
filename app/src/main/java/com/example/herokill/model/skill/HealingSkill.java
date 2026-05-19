package com.example.herokill.model.skill;

import com.example.herokill.model.Hero;
import com.example.herokill.model.Skill;
import com.example.herokill.model.SkillType;

public class HealingSkill extends Skill {
    private int healAmount;

    public HealingSkill() {
        super("治疗", SkillType.HEALING, 2);
        this.healAmount = 3;
    }

    @Override
    public void execute(Hero user, Hero target) {
        if (isReady()) {
            target.heal(healAmount);
            resetCooldown();
        }
    }
}