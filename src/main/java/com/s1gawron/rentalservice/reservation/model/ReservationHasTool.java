package com.s1gawron.rentalservice.reservation.model;

import com.s1gawron.rentalservice.tool.model.Tool;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "reservation_has_tool")
@DynamicUpdate
@NoArgsConstructor
@Getter
public class ReservationHasTool {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_has_tool")
    private Long reservationHasToolId;

    @ManyToOne
    @JoinColumn(name = "tool_id", nullable = false)
    private Tool tool;

    @ManyToOne
    @JoinColumn(name = "reservation_id") //TODO - rethink about nullable
    private Reservation reservation;

    public ReservationHasTool(final Tool tool, final Reservation reservation) {
        this.tool = tool;
        this.reservation = reservation;
    }
}
