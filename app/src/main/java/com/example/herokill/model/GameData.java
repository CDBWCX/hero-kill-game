package com.example.herokill.model;

import com.example.herokill.model.card.AttackCard;
import com.example.herokill.model.card.DefenseCard;
import com.example.herokill.model.card.HealCard;
import com.example.herokill.model.card.SkillCard;
import com.example.herokill.model.skill.BerserkSkill;
import com.example.herokill.model.skill.CounterSkill;
import com.example.herokill.model.skill.DoubleAttackSkill;
import com.example.herokill.model.skill.HealingSkill;
import com.example.herokill.model.skill.PowerStrikeSkill;
import com.example.herokill.model.skill.ShieldSkill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameData {
    private static final Random random = new Random();

    public static List<Hero> createHeroes() {
        List<Hero> heroes = new ArrayList<>();

        heroes.add(new Hero("赵云", 10, 3, 2, Arrays.asList(new PowerStrikeSkill())));
        heroes.add(new Hero("关羽", 12, 4, 1, Arrays.asList(new DoubleAttackSkill())));
        heroes.add(new Hero("张飞", 11, 3, 2, Arrays.asList(new BerserkSkill())));
        heroes.add(new Hero("刘备", 9, 2, 2, Arrays.asList(new HealingSkill())));
        heroes.add(new Hero("曹操", 10, 3, 2, Arrays.asList(new ShieldSkill())));
        heroes.add(new Hero("孙权", 9, 3, 3, Arrays.asList(new HealingSkill())));
        heroes.add(new Hero("诸葛亮", 8, 2, 3, Arrays.asList(new ShieldSkill())));
        heroes.add(new Hero("周瑜", 9, 3, 2, Arrays.asList(new CounterSkill())));

        return heroes;
    }

    public static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();

        for (int i = 0; i < 18; i++) {
            deck.add(new AttackCard("杀", 1));
        }
        for (int i = 0; i < 12; i++) {
            deck.add(new AttackCard("闪", -1));
        }
        for (int i = 0; i < 10; i++) {
            deck.add(new DefenseCard("防御", 2));
        }
        for (int i = 0; i < 6; i++) {
            deck.add(new HealCard("桃", 3));
        }
        for (int i = 0; i < 4; i++) {
            deck.add(new SkillCard("决斗", 2));
        }

        return deck;
    }

    public static void shuffleDeck(List<Card> deck) {
        for (int i = deck.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Card temp = deck.get(i);
            deck.set(i, deck.get(j));
            deck.set(j, temp);
        }
    }

    public static void drawCards(Hero hero, List<Card> deck, int count) {
        for (int i = 0; i < count && !deck.isEmpty(); i++) {
            Card card = deck.remove(0);
            hero.addCard(card);
        }
    }

    public static String getHeroDescription(String heroName) {
        switch (heroName) {
            case "赵云": return "龙胆：能攻善守，攻防兼备";
            case "关羽": return "武圣：擅长连续攻击";
            case "张飞": return "咆哮：进入狂暴状态";
            case "刘备": return "仁德：能治疗队友";
            case "曹操": return "奸雄：善于防御";
            case "孙权": return "制衡：攻守兼备";
            case "诸葛亮": return "空城：高防御";
            case "周瑜": return "英姿：能反击敌人";
            default: return "";
        }
    }

    public static String getCardDescription(String cardName) {
        switch (cardName) {
            case "杀": return "对敌人造成伤害";
            case "闪": return "抵挡一次攻击";
            case "防御": return "增加防御力";
            case "桃": return "恢复生命值";
            case "决斗": return "双方互相伤害";
            default: return "";
        }
    }
}