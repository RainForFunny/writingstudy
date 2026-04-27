package com.ielts.writing.model.enums;

public enum TopicCategory {
    EDUCATION("教育"),
    TECHNOLOGY("科技"),
    ENVIRONMENT("环境"),
    SOCIETY("社会"),
    ECONOMY("经济"),
    HEALTH("健康"),
    CULTURE("文化"),
    MEDIA("媒体"),
    CRIME("犯罪"),
    OTHER("其他");

    private final String displayName;

    TopicCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
