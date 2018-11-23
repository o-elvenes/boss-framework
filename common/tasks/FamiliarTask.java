package com.ceru.framework.boss.common.tasks;

import com.ceru.framework.boss.BossTask;
import com.inuvation.api.commons.Time;
import com.inuvation.game.api.component.Item;
import com.inuvation.game.api.component.tab.Summoning;

public class FamiliarTask extends BossTask {

    private Item item;

    @Override
    public boolean validate() {
        return Summoning.getMinutesRemaining() < 3 && (item = script.getItem(a -> a.containsAction("Summon"))) != null;
    }

    @Override
    public void execute() {
        if (!Summoning.Familiar.isPresent() && item.interact("Summon")) {
            script.setStatus("[FamiliarTask] Familiar not active, summoned first pouch available");
            Time.sleep(600);
        } else if (Summoning.Familiar.renew()) {
            script.setStatus("[FamiliarTask] Successfully renewed familiar");
            Time.sleep(600);
        } else {
            script.setStatus("[FamiliarTask] Failed to renew familiar");
        }
    }
}