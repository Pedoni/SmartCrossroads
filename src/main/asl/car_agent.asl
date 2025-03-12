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

+!start(PosX, PosY, D) <-
    +direction(D);
    +position(PosX, PosY);
    .my_name(Me);
    +name(Me);
    .broadcast(tell, ask_position);
    spawn_car(PosX, PosY, Me);
    +find_target(PosX, PosY, D).

+find_target(PosX, PosY, D) <-
    -find_target(PosX, PosY, D);
    internal_actions.GetTargetPoint(PosX, PosY, D).

+ask_position[source(Other)] : (Other \== self) & position(X, Y)[source(self)] <-
    -ask_position[source(Other)];
    .send(Other, tell, position(X, Y)).

+target(PosX, PosY) : PosX = -1 & PosY = -1 & position(X, Y)[source(self)] <-
    .broadcast(untell, position(X, Y));
    !terminate.

+target(PosX, PosY) : not(position(PosX, PosY)[source(Other)]) & (Other \== self) & position(X, Y)[source(self)] <-
    -target(_, _)[source(percept)];
    -target(_, _)[source(self)];
    if (tl(S, X, Y)) {
        .concat("traffic_light_", S, TL);
        .send(TL, askOne, is_green(GREEN), is_green(GREEN));
        if (not(GREEN)) {
            +target(PosX, PosY)[source(self)];
        } else {
            !go(PosX, PosY);
        }
    } else {
        !go(PosX, PosY);
    }.

+!go(PosX, PosY) : direction(D) & name(Me) <-
    -direction(_)[source(percept)];
    -target(_, _)[source(percept)];
    -target(_, _)[source(self)];
    -position(_, _)[source(creator)];
    -+position(PosX, PosY);
    .broadcast(tell, share(PosX, PosY));
    move_car(PosX, PosY, Me, D).

+target(PosX, PosY) : direction(D) & name(Me) & position(PosX, PosY)[source(Other)] & (Other \== self) <-
        -+target(PosX, PosY).

+share(PosX, PosY)[source(Other)] : (Other \== self) <-
    -share(_, _)[source(Other)]; 
    -+position(PosX, PosY)[source(Other)].

+!terminate : name(Me) <-
    remove_car(Me);
    .kill_agent(Me).
