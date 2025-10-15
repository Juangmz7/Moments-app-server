package com.juangomez.campusconnect.model.enums;

//@Enumerated(EnumType.STRING)
public enum InterestTag {

    // Lifestyle & Wellbeing
    SPORTS,
    ART,
    MUSIC,
    FOOD,
    EDUCATION,
    TECHNOLOGY,
    TRAVEL,
    NATURE,
    ADVENTURE,
    PHOTOGRAPHY;

    // Gives a more specific value to the client
    public String getDisplayName() {
        return switch (this) {
            // Lifestyle & Wellbeing
            case SPORTS -> "Sports & Activities";

            // Culture & Entertainment
            case ART -> "Art & Exhibitions";
            case MUSIC -> "Music & Performance";

            // Food & Drink
            case FOOD -> "Food & Dining";

            // Education & Technology
            case EDUCATION -> "Education & Learning";
            case TECHNOLOGY -> "Technology & Innovation";

            // Nature & Outdoors
            case NATURE -> "Nature & Outdoors";
            case TRAVEL -> "Travel & Exploration";
            case ADVENTURE -> "Adventure";

            // Creativity & Hobbies
            case PHOTOGRAPHY -> "Photography";
        };
    }

}
