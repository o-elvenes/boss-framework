package com.ceru.framework.boss.common.tasks;

import com.ceru.framework.boss.BossScript;
import com.ceru.framework.boss.Consumable;
import com.ceru.framework.boss.Potions;

import java.time.Instant;
import java.util.function.BooleanSupplier;

public class PotionTask extends Consumable {

    private long previous;

    private final Potions.Prayer prayer = new Potions.Prayer(450);
    private final Potions.Overload overload = new Potions.Overload();
    private final Potions.Antipoison antipoison = new Potions.Antipoison();
    private final Potions.Adrenaline adrenaline;

    private boolean bool;

    public PotionTask(BossScript script, BooleanSupplier supplier) {
        super(script);
        this.adrenaline = new Potions.Adrenaline(supplier);
    }

    @Override
    public boolean shouldConsume() {
        if ((System.currentTimeMillis() - previous) <= 1800) {
            return false;
        }
        if (prayer.shouldDrink(script)) {
            bool = prayer.drink(script);
            return true;
        } else if (overload.shouldDrink(script)) {
            bool = overload.drink(script);
            return true;
        } else if (adrenaline.shouldDrink(script)) {
            bool = adrenaline.drink(script);
            return true;
        } else if (antipoison.shouldDrink(script)) {
            bool = antipoison.drink(script);
            return true;
        }
        return false;
    }

    @Override
    public void consume() {
        if (bool) {
            script.setStatus("[PotionTask] Drank potion");
            previous = System.currentTimeMillis();
        } else {
            script.setStatus("[PotionTask] Failed to drink potion");
            script.setForceBank(true); // Only happens for prayer potions atm, check Potions class for details
        }
    }
}
