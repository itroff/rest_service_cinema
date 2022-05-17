package cinema;

import java.util.UUID;

public class PurchaseResponse {

    public UUID getToken() {
        return token;
    }

    public PurchaseResponse(Seat ticket, UUID token) {
        this.token = token;
        this.ticket = ticket;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Seat getTicket() {
        return ticket;
    }

    public void setTicket(Seat ticket) {
        this.ticket = ticket;
    }

    private UUID token;
    private Seat ticket;
}
