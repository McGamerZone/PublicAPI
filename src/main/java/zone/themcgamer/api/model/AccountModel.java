package zone.themcgamer.api.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;
import zone.themcgamer.api.common.Rank;
import zone.themcgamer.api.json.JsonResponse;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This class represents an account
 *
 * @author Braydon
 */
@AllArgsConstructor @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class AccountModel {
    @EqualsAndHashCode.Include private final int id;
    private final UUID uuid;
    private final String name;
    private final Rank primaryRank;
    private final Rank[] secondaryRanks;
    private final double gold, gems;
    @Nullable private final String encryptedIpAddress;
    private final long firstLogin, lastLogin;
    private final long timeCached;

    /**
     * Get a {@link AccountModel} from the provided {@link JsonResponse}
     *
     * @param response the json response
     * @return the account model
     */
    public static AccountModel fromResponse(JsonResponse response) {
        JsonElement jsonElement = response.getJsonElement();
        if (!(jsonElement instanceof JsonObject))
            return null;
        JsonObject jsonObject = (JsonObject) jsonElement;
        JsonObject valueObject = jsonObject.getAsJsonObject("value");
        if (valueObject == null)
            return null;
        int accountId = valueObject.get("id").getAsInt();
        UUID uuid = UUID.fromString(valueObject.get("uuid").getAsString());
        String name = valueObject.get("name").getAsString();
        Rank rank = Rank.lookup(valueObject.get("primaryRank").getAsString());

        String secondaryRanksString = valueObject.get("secondaryRanks").getAsString();
        Set<Rank> secondaryRanks = new HashSet<>();
        if (!secondaryRanksString.trim().isEmpty()) {
            if (secondaryRanksString.contains(", ")) {
                String[] split = secondaryRanksString.split(", ");
                for (String secondaryRankString : split)
                    secondaryRanks.add(Rank.lookup(secondaryRankString));
            } else secondaryRanks.add(Rank.lookup(secondaryRanksString));
        }

        double gold = valueObject.get("gold").getAsDouble();
        double gems = valueObject.get("gems").getAsDouble();
        String rawEncryptedIpAddress = valueObject.get("encryptedIpAddress").getAsString();
        String encryptedIpAddress;
        if (rawEncryptedIpAddress.trim().equals("Unauthorized"))
            encryptedIpAddress = null;
        else encryptedIpAddress = rawEncryptedIpAddress;

        long firstLogin = valueObject.get("firstLogin").getAsLong();
        long lastLogin = valueObject.get("lastLogin").getAsLong();
        long timeCached = valueObject.get("timeCached").getAsLong();

        return new AccountModel(
                accountId,
                uuid,
                name,
                rank,
                secondaryRanks.toArray(new Rank[0]),
                gold,
                gems,
                encryptedIpAddress,
                firstLogin,
                lastLogin,
                timeCached
        );
    }
}