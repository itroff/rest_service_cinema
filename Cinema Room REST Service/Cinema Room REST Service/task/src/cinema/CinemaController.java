package cinema;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class CinemaController {
    @Autowired
    private Cinema cinema;

    @GetMapping("/seats")
    public Cinema getInfo() {
        return cinema;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Object> purchase(@RequestBody Seat seat) {
        if (!cinema.checkSeatRowAndColumn(seat)) {
            return new ResponseEntity<>(Map.of("error",
                    "The number of a row or a column is out of bounds!"), HttpStatus.BAD_REQUEST);
        }
        if (cinema.seatIsAvailableAndBook(seat)) {
            return new ResponseEntity<>(new PurchaseResponse(new Seat(seat.getRow(), seat.getColumn()),
                    cinema.getUUIDForSeat(seat)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("error",
                    "The ticket has been already purchased!"), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/return")
    public ResponseEntity<Object> returnTicket (@RequestBody Map<String, String> map) {
        Seat seat = cinema.returnTicket(UUID.fromString(map.get("token")));
        if (seat == null) {
            return new ResponseEntity<>(Map.of("error",
                    "Wrong token!"), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(Map.of("returned_ticket", seat), HttpStatus.OK);
        }
    }

    @PostMapping("/stats")
    public ResponseEntity<Object> stats(@RequestParam(value = "password", required = false) String password) {
        if ("super_secret".equals(password)) {
            Map<String, Integer> map = new HashMap<>();
            map.put("current_income", cinema.income());
            map.put("number_of_available_seats", cinema.getAvailable_seats().size());
            map.put("number_of_purchased_tickets", cinema.purchasedTickets());
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("error", "The password is wrong!"), HttpStatus.UNAUTHORIZED);
    }

}
