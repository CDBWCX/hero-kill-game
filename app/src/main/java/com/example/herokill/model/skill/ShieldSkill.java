package com.example.herokill.model.skill;

import com.example.herokill.model.Hero;
import com.example.herokill.model.Skill;
import com.example.herokill.model.SkillType;

public class ShieldSkill extends Skill {
    private int shieldAmount;

    public ShieldSkill() {
        super("护盾", SkillType.SHIELD, 2);
        this.shieldAmount = 4;
    }

    @Override
    public void execute(Hero user, Hero target) {
        if (isReady()) {
            target.setDefense(target.getDefense() + shieldAmount);
            resetCooldown();
        }
    }
}