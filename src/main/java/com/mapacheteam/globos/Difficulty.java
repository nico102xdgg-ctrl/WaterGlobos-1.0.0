package com.mapacheteam.globos;

public enum Difficulty {
    EASY("easy", 20),
    MEDIUM("medium", 15),
    HARD("hard", 10);

    public final String id;
    /** Segundos para responder (se puede cambiar con /globos time). */
    public final int seconds;

    Difficulty(String id, int seconds) {
        this.id = id;
        this.seconds = seconds;
    }
}
