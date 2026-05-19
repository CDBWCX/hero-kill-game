package com.example.herokill.model.skill;

import com.example.herokill.model.Hero;
import com.example.herokill.model.Skill;
import com.example.herokill.model.SkillType;

public class DoubleAttackSkill extends Skill {
    public DoubleAttackSkill() {
        super("连击", SkillType.POWER_STRIKE, 2);
    }

    @Override
    public void execute(Hero user, Hero target) {
        if (isReady()) {
            target.takeDamage(user.getAttack());
            target.takeDamage(user.getAttack());
            resetCooldown();
        }
    }
}