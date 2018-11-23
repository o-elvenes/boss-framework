package com.ceru.framework.boss.concurrent;

import com.ceru.framework.boss.BossScript;
import com.ceru.framework.boss.Potions;
import com.inuvation.script.Script;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

public class PotionThread {

    private final ScheduledExecutorService service;

    private final Potions.Prayer prayer = new Potions.Prayer(300);
    private final Potions.Overload overload = new Potions.Overload();
    private final Potions.Antipoison antipoison = new Potions.Antipoison();
    private final Potions.Adrenaline adrenaline;

    public PotionThread(BooleanSupplier supplier, BossScript script) {
        this.adrenaline = new Potions.Adrenaline(supplier);
        this.service = Executors.newSingleThreadScheduledExecutor();

        service.scheduleWithFixedDelay(() -> {
            if (script.getState() == Script.State.STOPPED) {
                System.out.println("Shutting down food thread");
                service.shutdown();
                return;
            }
            boolean bool = false;
            if (prayer.shouldDrink(script)) {
                bool = prayer.drink(script);
            } else if (overload.shouldDrink(script)) {
                bool = overload.drink(script);
            } else if (adrenaline.shouldDrink(script)) {
                bool = adrenaline.drink(script);
            } else if (antipoison.shouldDrink(script)) {
                bool = antipoison.drink(script);
            }
            if (bool) {
                script.setStatus("[PotionThread] Drank potion");
            }
        }, 600, 1200, TimeUnit.MILLISECONDS);
    }
}
