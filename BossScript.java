package com.ceru.framework.boss;

import com.inuvation.game.adapter.component.InterfaceComponent;
import com.inuvation.game.adapter.scene.Npc;
import com.inuvation.game.adapter.scene.Projectile;
import com.inuvation.game.adapter.scene.SceneObject;
import com.inuvation.game.api.Varps;
import com.inuvation.game.api.component.InterfaceAddress;
import com.inuvation.game.api.component.Interfaces;
import com.inuvation.game.api.component.Item;
import com.inuvation.game.api.component.tab.Backpack;
import com.inuvation.game.api.position.Distance;
import com.inuvation.game.api.scene.*;
import com.inuvation.game.providers.RSAnimableObject;
import com.inuvation.script.Script;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public abstract class BossScript extends Script {

    private static final InterfaceAddress targetAddress = new InterfaceAddress(
            () -> Interfaces.getFirst(1490, a -> a.getHeight() == 17 && a.isVisible())
    );

    private static final InterfaceAddress targetHealthAddress = new InterfaceAddress(
            () -> Interfaces.getFirst(1490, a -> a.getHeight() == 15 && a.isVisible() && !a.getText().isEmpty())
    );

    private boolean stop;

    private boolean canDps = true;
    private boolean canThreshold = true;
    private boolean canFourTick = true;
    private boolean instanceActive = true;
    private boolean canSnipe = true;
    private boolean canShieldDps;
    private boolean canAsphyxiate;
    private boolean canStunDps;
    private boolean canWalkBleed;
    private boolean forceBank;
    private boolean reEnterInstance;

    private int healthThreshold = 500;
    private int criticalHealthThreshold = 6500;

    private String bossName;
    private String status;

    private int loopDelay = 100;

    private List<Item> items;
    private List<Npc> npcs;
    private List<SceneObject> sceneObjects;
    private List<InterfaceComponent> interfaces;
    private List<RSAnimableObject> dynamicNodes;
    private List<Projectile> projectiles;

    public final void clearCaches() {
        items = null;
        npcs = null;
        sceneObjects = null;
        interfaces = null;
        projectiles = null;
        dynamicNodes = null;
        System.out.println("Cleared all caches");
    }

    public Item getItem(Predicate<Item> predicate) {
        for (Item item : getItems()) {
            if (item != null && predicate.test(item)) {
                return item;
            }
        }
        return null;
    }

    public Npc getNpc(Predicate<Npc> predicate) {
        for (Npc npc : getNpcs()) {
            if (npc != null && predicate.test(npc)) {
                return npc;
            }
        }
        return null;
    }

    public SceneObject getSceneObject(Predicate<SceneObject> predicate) {
        for (SceneObject sceneObject : getSceneObjects()) {
            if (sceneObject != null && predicate.test(sceneObject)) {
                return sceneObject;
            }
        }
        return null;
    }

    public Projectile getProjectile(Predicate<Projectile> predicate) {
        for (Projectile projectile : getProjectiles()) {
            if (projectile != null && predicate.test(projectile)) {
                return projectile;
            }
        }
        return null;
    }

    public List<Projectile> getProjectiles() {
        if (projectiles == null) {
            projectiles = Arrays.asList(Projectiles.getLoaded());
        }
        return new ArrayList<>(projectiles);
    }

    public List<Npc> getNpcs() {
        if (npcs == null) {
            npcs = Arrays.asList(Npcs.getLoaded());
        }
        return Distance.sortFrom(Players.getLocal(), npcs);
    }

    public List<SceneObject> getSceneObjects() {
        if (sceneObjects == null) {
            sceneObjects = Arrays.asList(SceneObjects.getLoaded());
        }
        return Distance.sortFrom(Players.getLocal(), sceneObjects);
    }

    public List<Item> getItems() {
        if (items == null) {
            items = Arrays.asList(Backpack.getItems());
        }
        return new ArrayList<>(items);
    }

    public List<InterfaceComponent> getInterfaces() {
        if (interfaces == null) {
            interfaces = Arrays.asList(Interfaces.getComponents(a -> true));
        }
        return new ArrayList<>(interfaces);
    }

    public List<RSAnimableObject> getDynamicNodes() {
        if (dynamicNodes == null) {
            dynamicNodes = Arrays.asList(DynamicObjects.getLoaded(a -> true));
        }
        return new ArrayList<>(dynamicNodes);
    }

    public final void allowDps() {
        canDps = true;
        canThreshold = true;
        canFourTick = true;
    }

    public final void denyDps() {
        canDps = false;
        canThreshold = false;
        canFourTick = false;
    }

    public boolean equip(String item, String action) {
        final Item weapon = getItem(a -> a.getName().equals(item));
        if (weapon != null) {
            weapon.interact(action);
            return true;
        }
        return false;
    }

    public boolean equip(Pattern item) {
        final Item weapon = getItem(a -> a.getName().matches(item.pattern()));
        if (weapon != null) {
            weapon.interact(a -> a.startsWith("W"));
            return true;
        }
        return false;
    }

    public boolean isShieldEquipped() {
        return Varps.getBitValue(22842) > 0;
    }

    public int getBossHealth() {
        return Varps.getBitValue(28663);
    }

    public String getTargetName() {
        final InterfaceComponent component = targetAddress.resolve();
        return component == null ? "" : component.getText();
    }

    public int getTargetHealth() {
        final InterfaceComponent component = targetHealthAddress.resolve();
        return component == null || component.getText().contains("N/A") ? -1 : Integer.valueOf(component.getText());
    }

    public boolean isCanDps() {
        return canDps;
    }

    public void setCanDps(boolean canDps) {
        this.canDps = canDps;
    }

    public boolean isCanThreshold() {
        return canThreshold;
    }

    public void setCanThreshold(boolean canThreshold) {
        this.canThreshold = canThreshold;
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

    public boolean isCanStunDps() {
        return canStunDps;
    }

    public void setCanStunDps(boolean canStunDps) {
        this.canStunDps = canStunDps;
    }

    public boolean isForceBank() {
        return forceBank;
    }

    public void setForceBank(boolean forceBank) {
        this.forceBank = forceBank;
    }

    public boolean isInstanceActive() {
        return instanceActive;
    }

    public void setInstanceActive(boolean instanceActive) {
        this.instanceActive = instanceActive;
    }

    public int getHealthThreshold() {
        return healthThreshold;
    }

    public void setHealthThreshold(int healthThreshold) {
        this.healthThreshold = healthThreshold;
    }

    public int getCriticalHealthThreshold() {
        return criticalHealthThreshold;
    }

    public void setCriticalHealthThreshold(int criticalHealthThreshold) {
        this.criticalHealthThreshold = criticalHealthThreshold;
    }

    public boolean isCanWalkBleed() {
        return canWalkBleed;
    }

    public void setCanWalkBleed(boolean canWalkBleed) {
        this.canWalkBleed = canWalkBleed;
    }

    public boolean isReEnterInstance() {
        return reEnterInstance;
    }

    public void setReEnterInstance(boolean reEnterInstance) {
        this.reEnterInstance = reEnterInstance;
    }

    public String getBossName() {
        return bossName;
    }

    public void setBossName(String bossName) {
        this.bossName = bossName;
    }

    public boolean isCanSnipe() {
        return canSnipe;
    }

    public void setCanSnipe(boolean canSnipe) {
        this.canSnipe = canSnipe;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        System.out.println(status);
        this.status = status;
    }

    public boolean isCanFourTick() {
        return canFourTick;
    }

    public void setCanFourTick(boolean canFourTick) {
        this.canFourTick = canFourTick;
    }

    public boolean isCanShieldDps() {
        return canShieldDps;
    }

    public void setCanShieldDps(boolean canShieldDps) {
        this.canShieldDps = canShieldDps;
    }

    public boolean isCanAsphyxiate() {
        return canAsphyxiate;
    }

    public void setCanAsphyxiate(boolean canAsphyxiate) {
        this.canAsphyxiate = canAsphyxiate;
    }

    public int getLoopDelay() {
        return loopDelay;
    }

    public void setLoopDelay(int loopDelay) {
        this.loopDelay = loopDelay;
    }
}
