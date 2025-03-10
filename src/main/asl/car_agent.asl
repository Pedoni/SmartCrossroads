tl(0, 5, 2).
tl(1, 6, 5).
tl(2, 4, 4).
tl(3, 7, 3).
tl(4, 10, 2).
tl(5, 11, 5).
tl(6, 9, 4).
tl(7, 12, 3).
tl(8, 5, 7).
tl(9, 6, 10).
tl(10, 4, 9).
tl(11, 7, 8).
tl(12, 10, 7).
tl(13, 11, 10).
tl(14, 9, 9).
tl(15, 12, 8).

+!start[source(creator)] : direction(D) & position(PosX, PosY) <-
    -position(_, _)[source(creator)];
    +position(PosX, PosY)[source(self)];
    .my_name(Me);
    +name(Me);
    spawn_car(PosX, PosY, Me);
    internal_actions.GetTargetPoint(PosX, PosY, D).

+target(PosX, PosY) : direction(D) & name(Me) & not(position(PosX, PosY)) & position(X, Y)[source(self)] <-
        if (PosX = -1 | PosY = -1 | PosX = 17 | PosY = 13) {
            .broadcast(untell, position(_, _));
            !terminate;
        } else {
            if (tl(S, X, Y)) {
                .print("Traffic light found at ", X, ", ", Y);
                .concat("traffic_light_", S, TL);
                .send(TL, askOne, is_green(GREEN), is_green(GREEN));
                .print("Traffic light ", TL, " is green: ", GREEN);
                if (not(GREEN)) {
                    .wait(500);
                    -target(_,_);
                    +target(PosX, PosY);
                } else {
                    !go(PosX, PosY, Me, D);
                }
            } else {
                !go(PosX, PosY, Me, D);
            };
        }.

+!go(PosX, PosY, Me, D) <-
    -direction(_)[source(percept)];
    -target(_,_);
    -position(_, _)[source(creator)];
    -+position(PosX, PosY);
    move_car(PosX, PosY, Me, D);
    .broadcast(tell, share(PosX, PosY)).

+target(PosX, PosY) : direction(D) & name(Me) & position(PosX, PosY) <-
        .wait(500);
        -+target(PosX, PosY).

+share(PosX, PosY)[source(Other)] : (Other \== self) <-
    -share(_, _)[source(Other)]; 
    -+position(PosX, PosY)[source(Other)].

+!terminate : name(Me) <-
    .print("Terminating.");
    remove_car(Me);
    .kill_agent(Me).
