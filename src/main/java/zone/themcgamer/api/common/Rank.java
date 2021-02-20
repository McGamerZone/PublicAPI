package zone.themcgamer.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * @author Braydon
 */
@AllArgsConstructor @RequiredArgsConstructor @Getter
public enum Rank {
    // Staff Ranks
    OWNER("Owner", "§6", "§6§lOwner", RankCategory.STAFF),
    MANAGER("Manager", "§e", "§e§lManager", RankCategory.STAFF),
    DEVELOPER("Dev", "§9", "§9§lDev", RankCategory.STAFF),
    JR_DEVELOPER("Jr.Dev", "§9", "§9§lJr.Dev", RankCategory.STAFF),
    ADMIN("Admin", "§c", "§c§lAdmin", RankCategory.STAFF),
    MODERATOR("Mod", "§2", "§2§lMod", RankCategory.STAFF),
    HELPER("Helper", "§a", "§a§lHelper", RankCategory.STAFF),

    // Other
    SR_BUILDER("Sr.Builder", "§3", "§3§lSr.Builder", RankCategory.OTHER),
    BUILDER("Builder", "§b", "§b§lBuilder", RankCategory.OTHER),
    PARTNER("Partner", "§d", "§d§lPartner", RankCategory.OTHER),
    YOUTUBER("YouTuber", "§c", "§c§lYouTuber", RankCategory.OTHER),

    // Donor Ranks
    ULTIMATE("Ultimate", "§5", "§5§lUltimate", RankCategory.DONATOR),
    EXPERT("Expert", "§b", "§b§lExpert", RankCategory.DONATOR),
    HERO("Hero", "§e", "§e§lHero", RankCategory.DONATOR),
    SKILLED("Skilled", "§6", "§6§lSkilled", RankCategory.DONATOR),
    GAMER("Gamer", "§2", "§2§lGamer", RankCategory.DONATOR),

    DEFAULT("None", "§7", "§7None", RankCategory.OTHER),

    // Sub Ranks
    BETA("Beta", "§5", null, "§5Beta", RankCategory.SUB);

    private final String displayName, color, prefix;
    private String suffix;
    private RankCategory category;

    Rank(String displayName, String color, String prefix, RankCategory category) {
        this(displayName, color, prefix, null, category);
    }

    /**
     * Get whether or not the rank has a prefix
     */
    public boolean hasPrefix() {
        return prefix != null;
    }

    /**
     * Get whether or not the rank has a suffix
     */
    public boolean hasSuffix() {
        return suffix != null;
    }

    /**
     * Get the rank matching the given {@link String}
     *
     * @param s the string
     * @return the rank, otherwise DEFAULT if not found
     */
    public static Rank lookup(String s) {
        return Arrays.stream(Rank.values())
                .filter(rank -> rank.name().equalsIgnoreCase(s) || rank.getDisplayName().equalsIgnoreCase(s))
                .findFirst().orElse(DEFAULT);
    }

    public enum RankCategory {
        STAFF, DONATOR, SUB, OTHER
    }
}