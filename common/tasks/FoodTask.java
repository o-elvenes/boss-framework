package com.ceru.framework.boss.common.tasks;

import com.ceru.framework.boss.BossScript;
import com.ceru.framework.boss.Consumable;
import com.inuvation.game.adapter.cache.ItemDefinition;
import com.inuvation.game.api.component.Health;
import com.inuvation.game.api.component.Item;
import com.inuvation.game.api.component.tab.Summoning;

import java.time.Instant;
import java.util.Arrays;

public class FoodTask extends Consumable {

    private long previous;

    private boolean action;

    public FoodTask(BossScript script) {
        super(script);
    }

    private boolean eatBlubber() {
        final Item item = script.getItem(a -> a.getName().contains("blubber"));
        return item != null && item.interact("Eat");
    }

    private boolean drinkBrew() {
        final Item item = script.getItem(a -> a.getName().startsWith("Saradomin"));
        return item != null && item.interact("Drink");
    }

    private boolean eat() {
        final Item item = script.getItem(a -> a.containsAction("Eat") && !a.getName().contains("blubber"));
        return item != null && item.interact("Eat");
    }

    private boolean comboEat() {
        return eat() && (eatBlubber() || drinkBrew());
    }

    @Override
    public boolean shouldConsume() {
        if ((System.currentTimeMillis() - previous) <= 1800) {
            return false;
        }
        final int health = Health.getCurrent();
        if (health <= script.getCriticalHealthThreshold()) {
            action = comboEat();
            return true;
        } else if (health <= script.getHealthThreshold()) {
            action = eat();
            return true;
        }
        return false;
    }

    @Override
    public void consume() {
        if (action) {
            script.setStatus("[FoodTask] Ate food");
            previous = System.currentTimeMillis();
        } else if (Summoning.Familiar.contains(a -> {
            ItemDefinition definition = a.getDefinition();
            return definition != null && Arrays.asList(definition.getActions()).contains("Eat");
        }) && Summoning.Familiar.takeAll()) {
            script.setStatus("[FoodTask] Taking BoB");
        } else {
            script.setStatus("[FoodTask] Failed to eat!");
        }
    }
}
