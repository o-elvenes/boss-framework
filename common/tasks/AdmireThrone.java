package com.ceru.framework.boss.common.tasks;

import com.ceru.framework.boss.BossTask;
import com.inuvation.api.commons.Time;
import com.inuvation.game.adapter.scene.Player;
import com.inuvation.game.adapter.scene.SceneObject;
import com.inuvation.game.api.component.tab.Prayers;
import com.inuvation.game.api.position.Area;
import com.inuvation.game.api.position.Position;
import com.inuvation.game.api.scene.Players;
import com.inuvation.game.api.scene.SceneObjects;

public class AdmireThrone extends BossTask {

    public static final Area MAX_GUILD = Area.rectangular(
            Position.global(2266, 3300, 1), Position.global(2286, 3317, 1)
    );

    private Player player;

    private boolean isPrayerFull() {
        return Prayers.getPoints() >= Prayers.getMaximumPoints();
    }

    @Override
    public boolean validate() {
        return (player = Players.getLocal()) != null && !isPrayerFull() && MAX_GUILD.contains(player);
    }

    @Override
    public void execute() {
        if (Prayers.isQuickPraying()) {
            script.setStatus("[AdmireThrone] Turning off prayers");
            Prayers.toggleQuickPrayers();
        } else {
            script.setStatus("[AdmireThrone] Regenerating prayer and summoning points");
            final SceneObject throne = SceneObjects.getNearest("Throne of Fame");
            if (throne != null && throne.interact("Admire")) {
                Time.sleepUntil(this::isPrayerFull, player::isMoving, 1200, 1800);
            }
        }
    }
}
