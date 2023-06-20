package fr.fyustorm.minetiface.commons.config;

public class BlockScoreConfig {

    private String translationKey;
    private String name;
    private float score;

    public BlockScoreConfig() {
        // Used by jackson
    }

    public BlockScoreConfig(String translationKey, String name, float score) {
        this.translationKey = translationKey;
        this.name = name;
        this.score = score;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public String getName() {
        return name;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }
}