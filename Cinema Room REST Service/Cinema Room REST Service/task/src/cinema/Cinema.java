package cinema;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class Seat {
    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.price = Seat.getTicketPrice(row);
    }

    public static int getTicketPrice(int row) {
        if (row <= 4) {
            return 10;
        }
        return 8;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    private int row;
    private int column;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    private int price;
}

public class Cinema {
    private int total_rows;
    private Map<UUID, Seat> tokens = new ConcurrentHashMap<>();

    public Cinema(int total_rows, int total_columns) {
        this.total_rows = total_rows;
        this.total_columns = total_columns;
        List<Seat> seats = new ArrayList<>();
        for (int i = 0; i < total_rows; i++) {
            for (int j = 0; j < total_columns; j++) {
                seats.add(new Seat(i + 1, j + 1));
            }
        }
        this.available_seats = seats;
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public int getTotal_columns() {
        return total_columns;
    }

    public void setTotal_columns(int total_columns) {
        this.total_columns = total_columns;
    }

    public List<Seat> getAvailable_seats() {
        return available_seats;
    }

    public void setAvailable_seats(List<Seat> available_seats) {
        this.available_seats = available_seats;
    }

    private int total_columns;
    private List<Seat> available_seats;
    private int income = 0;

    public boolean seatIsAvailableAndBook(Seat seat) {
        synchronized (available_seats) {
            for (int i = 0; i < available_seats.size(); i++) {
                if (available_seats.get(i).getRow() == seat.getRow()
                        && available_seats.get(i).getColumn() == seat.getColumn()) {
                    available_seats.remove(i);
                    tokens.put(UUID.randomUUID(), seat);
                    income += seat.getPrice();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkSeatRowAndColumn(Seat seat) {
        if (seat.getRow() > 0
                && seat.getRow() <= getTotal_rows()
                && seat.getColumn() > 0
                && seat.getColumn() <= getTotal_columns()) {
            return true;
        }
        return false;
    }

    public UUID getUUIDForSeat(Seat seat) {
        for (Map.Entry<UUID, Seat> ticket : tokens.entrySet()) {
            if (ticket.getValue().getRow() == seat.getRow()
                    && ticket.getValue().getColumn() == seat.getColumn()) {
                return ticket.getKey();
            }
        }
        return null;
    }

    public Seat returnTicket(UUID token) {
        if (!tokens.containsKey(token)) {
            return null;
        }
        Seat seat = null;
        synchronized (available_seats) {
            seat = tokens.get(token);
            available_seats.add(seat);
            income -= seat.getPrice();
        }
        tokens.remove(token);
        return seat;
    }

    public int purchasedTickets() {
        return tokens.size();
    }

    public int income() {
        return income;
    }


}
