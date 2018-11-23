package com.ceru.framework.boss.common.tasks;

import com.ceru.framework.boss.BossTask;
import com.inuvation.api.commons.Time;
import com.inuvation.game.api.component.Item;
import com.inuvation.game.api.component.tab.Backpack;

import java.util.Arrays;
import java.util.List;

public class RingTask extends BossTask {

    private final boolean luckRing;

    private final List<String> RING_LIST = Arrays.asList("Ring of death", "Asylum surgeon's ring", "Sixth-Age circuit");
    private final List<String> LUCK_RINGS = Arrays.asList("Luck of the dwarves", "Ring of fortune");

    public RingTask(boolean luckRing) {
        this.luckRing = luckRing;
    }

    private Item item;

    private boolean equipLuckRing() {
        return luckRing && script.getBossHealth() <= 12000 && (item = script.getItem(a -> LUCK_RINGS.contains(a.getName()))) != null;
    }

    private boolean equipStandardRing() {
        return script.getBossHealth() > 12000 && (item = script.getItem(a -> RING_LIST.contains(a.getName()))) != null;
    }

    @Override
    public boolean validate() {
        return equipLuckRing() || equipStandardRing();
    }

    @Override
    public void execute() {
        script.setStatus("[RingTask] Equipping " + item.getName());
        if (item.interact("Wear")) {
            Time.sleep(200);
        }
    }
}
