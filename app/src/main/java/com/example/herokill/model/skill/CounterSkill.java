package com.example.herokill.model.skill;

import com.example.herokill.model.Hero;
import com.example.herokill.model.Skill;
import com.example.herokill.model.SkillType;

public class CounterSkill extends Skill {
    public CounterSkill() {
        super("反击", SkillType.COUNTER, 4);
    }

    @Override
    public void execute(Hero user, Hero target) {
        if (isReady()) {
            int damage = target.getAttack();
            target.takeDamage(damage);
            resetCooldown();
        }
    }
}