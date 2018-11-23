package com.ceru.framework.boss.common.tasks;

import com.ceru.framework.boss.BossTask;
import com.inuvation.api.commons.Time;
import com.inuvation.game.adapter.scene.Player;
import com.inuvation.game.adapter.scene.SceneObject;
import com.inuvation.game.api.scene.Players;

public class EnterBossPortal extends BossTask {

    private final String charSequence;

    public EnterBossPortal(String charSequence) {
        this.charSequence = charSequence;
    }

    private Player player;

    @Override
    public boolean validate() {
        return (player = Players.getLocal()) != null && AdmireThrone.MAX_GUILD.contains(player);
    }

    @Override
    public void execute() {
        script.setStatus("[EnterBossPortal] Entering boss portal");
        final SceneObject object = script.getSceneObject(a -> a.getName().contains(charSequence));
        if (object != null && object.interact("Enter")) {
            Time.sleepUntil(() -> script.getSceneObject(a -> a.getName().contains(charSequence)) == null, player::isMoving, 600, 1200);
        }
    }
}
