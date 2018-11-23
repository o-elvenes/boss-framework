package com.ceru.framework.boss.concurrent;

import com.ceru.api.Items;
import com.ceru.framework.boss.BossScript;
import com.inuvation.game.api.component.Health;
import com.inuvation.script.Script;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FoodThread {

    private final ScheduledExecutorService service;

    public FoodThread(BossScript script) {
        this.service = Executors.newSingleThreadScheduledExecutor();

        service.scheduleWithFixedDelay(() -> {
            if (script.getState() == Script.State.STOPPED) {
                System.out.println("Shutting down food thread");
                service.shutdown();
                return;
            }
            final int current = Health.getCurrent();
            if (current > script.getHealthThreshold() && current > script.getCriticalHealthThreshold()) {
                return;
            }
            if (current <= script.getCriticalHealthThreshold() && Items.comboEat()) {
                script.setStatus("[FoodThread] Combo eating");
            } else if (current <= script.getHealthThreshold() && Items.eat()) {
                script.setStatus("[FoodThread] Eating");
            } else {
                script.setForceBank(true);
            }
        }, 600, 1200, TimeUnit.MILLISECONDS);
    }
}
