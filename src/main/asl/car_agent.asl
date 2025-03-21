+target(PosX, PosY) <-
    !check_target(PosX, PosY).

+!start(PosX, PosY, D) <-
    .my_name(Me);
    .broadcast(achieve, ask_position);
    spawn_car(PosX, PosY, Me, D).

+!ask_position[source(Other)] : (Other \== percept) & position(X, Y)[source(percept)] <-
    .send(Other, tell, position(X, Y)).

+!check_target(PosX, PosY) : PosX = -1 & PosY = -1 & position(X, Y)[source(percept)] <-
    .broadcast(untell, position(X, Y));
    !terminate.

+!check_target(PosX, PosY) : not(position(PosX, PosY)[source(Other)]) & (Other \== percept) & position(X, Y)[source(percept)] <-
    if (tl(S, X, Y)) {
        .concat("traffic_light_", S, TL);
        .send(TL, askOne, is_green(GREEN), is_green(GREEN));
        if (not(GREEN)) {
            !check_target(PosX, PosY);
        } else {
            !go(PosX, PosY);
        }
    } else {
        !go(PosX, PosY);
    }.

+!check_target(PosX, PosY) : position(PosX, PosY)[source(Other)] & (Other \== percept) <-
    !check_target(PosX, PosY).

+!go(PosX, PosY) : direction(D) & name(Me) <- 
    .broadcast(achieve, share(PosX, PosY));
    move_car(PosX, PosY, Me, D).

+!share(PosX, PosY)[source(Other)] : (Other \== percept) <-
    -+position(PosX, PosY)[source(Other)].

+!terminate : name(Me) <-
    remove_car(Me);
    .kill_agent(Me).
