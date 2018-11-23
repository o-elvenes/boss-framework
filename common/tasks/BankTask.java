package com.ceru.framework.boss.common.tasks;

import com.ceru.api.Items;
import com.ceru.framework.boss.BossTask;
import com.inuvation.game.adapter.scene.Player;
import com.inuvation.game.api.component.Bank;
import com.inuvation.game.api.component.Health;
import com.inuvation.game.api.component.Item;
import com.inuvation.game.api.component.tab.Backpack;
import com.inuvation.game.api.component.tab.Prayers;
import com.inuvation.game.api.scene.Players;

public class BankTask extends BossTask {

    @Override
    public boolean validate() {
        return script.isForceBank() || (Bank.isOpen() && Health.getCurrentPercent() < 100);
    }

    @Override
    public void execute() {
        script.setStatus("[BankTask] Banking for items");
        script.setCanDps(false);
        script.setCanThreshold(false);
        final Player player = Players.getLocal();
        if (player == null) {
            return;
        }
        Items.teleMaxGuild(player);
        final Item item = Backpack.getFirst(a -> a.containsAction("Break"));
        if (item != null && item.interact("Break")) {
            return;
        }
        if (Prayers.isQuickPraying()) {
            script.setStatus("[BankTask] Turning off prayers");
            Prayers.toggleQuickPrayers();
        } else if (Bank.isOpen() && Bank.loadPreset(1)) {
            script.setForceBank(false);
        } else {
            script.setStatus("[BankTask] Opening bank");
            Bank.open();
        }
    }
}
