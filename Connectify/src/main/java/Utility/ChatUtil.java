package Utility;

import java.util.Arrays;

public class ChatUtil {
    /**
     * Compute a stable room ID for two usernames: e.g. "alice" and "bob" → "alice_bob"
     * Always order lexicographically so both sides compute same ID.
     */
    public static String roomId(String user1, String user2) {
        String[] arr = new String[]{user1, user2};
        Arrays.sort(arr);
        return arr[0] + "_" + arr[1];
    }
}