package isb.model;

import java.util.Set;

public class Packet {
    public final int sourceId;
    public final int senderId;
    public final Set<Integer> forwardList; // DP only

    public Packet(int sourceId, int senderId, Set<Integer> forwardList) {
        this.sourceId = sourceId;
        this.senderId = senderId;
        this.forwardList = forwardList;
    }
}
