package com.ceru.framework.boss;

import com.inuvation.game.api.Varps;
import com.inuvation.game.api.component.Item;

import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;

public class Potions {

    public enum Type {

        ANTIPOISON(Pattern.compile(".*([Aa])ntipoison.*"), 722),
        OVERLOAD(Pattern.compile("^(Holy|Searing|Supreme)?(O| o)verload( flask| potion| salve)? \\(\\d\\)$"), 26037),
        ADRENALINE(Pattern.compile(".*([Aa]drenaline|[Rr]eplenishment).*"), 21454),
        PRAYER(Pattern.compile(".*([Pp]rayer|[Rr]estore).*"), -1);

        private final Pattern pattern;
        private final int varbit;

        Type(Pattern pattern, int varbit) {
            this.pattern = pattern;
            this.varbit = varbit;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public int getVarbit() {
            return varbit;
        }
    }

    public static class Antipoison implements Potion {

        private Item item;

        @Override
        public boolean shouldDrink(BossScript script) {
            return Varps.getValue(Type.ANTIPOISON.varbit) > -1 && (item = script.getItem(a -> a.getName()
                    .matches(Type.ANTIPOISON.getPattern().pattern()))) != null;
        }

        @Override
        public boolean drink(BossScript script) {
            return (item = script.getItem(a -> a.getName().matches(Type.ANTIPOISON.getPattern().pattern()))) != null && item.interact("Drink");
        }

        @Override
        public boolean exists(BossScript script) {
            return script.getItem(a -> a.getName().matches(Type.ANTIPOISON.getPattern().pattern())) != null;
        }
    }

    public static class Prayer implements Potion {

        private final int threshold;

        private Item item;

        public Prayer(int threshold) {
            this.threshold = threshold;
        }

        @Override
        public boolean shouldDrink(BossScript script) {
            return com.inuvation.game.api.component.tab.Prayers.getPoints() < threshold;
        }

        @Override
        public boolean drink(BossScript script) {
            return (item = script.getItem(a -> a.getName().matches(Type.PRAYER.getPattern().pattern()))) != null && item.interact("Drink");
        }

        @Override
        public boolean exists(BossScript script) {
            return script.getItem(a -> a.getName().matches(Type.PRAYER.getPattern().pattern())) != null;
        }
    }

    public static class Adrenaline implements Potion {

        private Item item;

        private final BooleanSupplier supplier;

        public Adrenaline(BooleanSupplier supplier) {
            this.supplier = supplier == null ? () -> false : supplier;
        }

        @Override
        public boolean shouldDrink(BossScript script) {
            return supplier.getAsBoolean() && Varps.getBitValue(Type.ADRENALINE.getVarbit()) < 1
                    && (item = script.getItem(a -> a.getName().matches(Type.ADRENALINE.getPattern().pattern()))) != null;
        }

        @Override
        public boolean drink(BossScript script) {
            return (item = script.getItem(a -> a.getName().matches(Type.ADRENALINE.getPattern().pattern()))) != null && item.interact("Drink");
        }

        @Override
        public boolean exists(BossScript script) {
            return script.getItem(a -> a.getName().matches(Type.ADRENALINE.getPattern().pattern())) != null;
        }
    }

    public static class Overload implements Potion {

        private Item item;

        @Override
        public boolean shouldDrink(BossScript script) {
            return Varps.getBitValue(Type.OVERLOAD.getVarbit()) < 1 && (item = script.getItem(a ->
                    a.getName().matches(Type.OVERLOAD.getPattern().pattern()))) != null;
        }

        @Override
        public boolean drink(BossScript script) {
            return (item = script.getItem(a -> a.getName().matches(Type.OVERLOAD.getPattern().pattern()))) != null && item.interact("Drink");
        }

        @Override
        public boolean exists(BossScript script) {
            return script.getItem(a -> a.getName().matches(Type.OVERLOAD.getPattern().pattern())) != null;
        }
    }
}