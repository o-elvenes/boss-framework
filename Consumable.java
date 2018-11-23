package com.ceru.framework.boss;

public abstract class Consumable {

    protected final BossScript script;

    public Consumable(BossScript script) {
        this.script = script;
    }

    public abstract boolean shouldConsume();

    public abstract void consume();
}
