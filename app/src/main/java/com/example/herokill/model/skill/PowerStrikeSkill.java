package com.example.herokill.model.skill;

import com.example.herokill.model.Hero;
import com.example.herokill.model.Skill;
import com.example.herokill.model.SkillType;

public class PowerStrikeSkill extends Skill {
    private int multiplier;

    public PowerStrikeSkill() {
        super("重击", SkillType.POWER_STRIKE, 3);
        this.multiplier = 2;
    }

    @Override
    public void execute(Hero user, Hero target) {
        if (isReady()) {
            int damage = user.getAttack() * multiplier;
            target.takeDamage(damage);
            resetCooldown();
        }
    }
}