package com.ceru.framework.boss;

import com.inuvation.script.ScriptController;

public abstract class BossTask {

    protected final BossScript script = (BossScript) ScriptController.getInstance().getActiveScript();

    public abstract boolean validate();

    public abstract void execute();
}
