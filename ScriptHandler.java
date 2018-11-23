package com.ceru.framework.boss;

import java.util.List;

public interface ScriptHandler {

    void execute();

    default BossTask getTask(List<BossTask> tasks) {
        for (BossTask task : tasks) {
            if (task.validate()) {
                return task;
            }
        }
        return null;
    }
}
