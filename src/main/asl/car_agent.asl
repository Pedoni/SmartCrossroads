+!start[source(creator)] : direction(D) & position(PosX, PosY) <-
    .my_name(Me);
    +name(Me);
    spawn_car(PosX, PosY, Me);
    internal_actions.GetTargetPoint(PosX, PosY, D).

+target(PosX, PosY) : direction(D) & name(Me) & not(position(PosX, PosY)) <-
        -direction(_)[source(percept)];
        if (PosX = -1 | PosY = -1 | PosX = 17 | PosY = 13) {
            .broadcast(untell, position(_, _));
            !terminate;
        } else {
            -target(_,_);
            -+position(PosX, PosY);
            move_car(PosX, PosY, Me, D);
            .broadcast(tell, share(PosX, PosY));
        }.

+target(PosX, PosY) : direction(D) & name(Me) & position(PosX, PosY) <-
        .wait(50);
        -+target(PosX, PosY).

+share(PosX, PosY)[source(Other)] : name(Me) & (Other \== self) <-
    -share(_, _)[source(Other)]; 
    -+position(PosX, PosY)[source(Other)].

+!terminate : name(Me) <-
    .print("Terminating.");
    remove_car(Me);
    .kill_agent(Me).
