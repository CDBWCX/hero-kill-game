package com.example.herokill.model.skill;

import com.example.herokill.model.Hero;
import com.example.herokill.model.Skill;
import com.example.herokill.model.SkillType;

public class BerserkSkill extends Skill {
    public BerserkSkill() {
        super("狂暴", SkillType.BERSERK, 4);
    }

    @Override
    public void execute(Hero user, Hero target) {
        if (isReady()) {
            user.setAttack(user.getAttack() * 2);
            resetCooldown();
        }
    }
}