package com.ceru.framework.boss;

public interface Potion {

    boolean shouldDrink(BossScript script);

    boolean drink(BossScript script);

    boolean exists(BossScript script);
}
