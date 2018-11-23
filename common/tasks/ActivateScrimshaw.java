package com.ceru.framework.boss.common.tasks;

import com.ceru.framework.boss.BossTask;
import com.inuvation.api.commons.Time;
import com.inuvation.game.api.Varps;
import com.inuvation.game.api.component.Item;
import com.inuvation.game.api.component.tab.Equipment;

public class ActivateScrimshaw extends BossTask {

    private Item item;

    @Override
    public boolean validate() {
        return Varps.getBitValue(22899) <= 0 && (item = Equipment.getFirst(a -> a.containsAction("Activate/Deactivate"))) != null;
    }

    @Override
    public void execute() {
        script.setStatus("[ActivateScrimshaw] Toggling scrimshaw");
        if (item.interact("Activate/Deactivate")) {
            Time.sleep(600);
        }
    }
}
