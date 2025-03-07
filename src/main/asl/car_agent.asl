+start(PosX, PosY, D) <-
    -start(PosX, PosY, D)[source(creator)];
    .my_name(Me);
    +name(Me);
    +position(PosX, PosY);
    spawn_car(PosX, PosY, Me);
    internal_actions.GetTargetPoint(PosX, PosY, D).

+target(PosX, PosY) : direction(D) & name(Me) & not(position(PosX, PosY)[source(Other)]
    & (Other \== self)) & PosX >= 0 <-
        -direction(_)[source(percept)];
        -+position(PosX, PosY);  
        move_car(PosX, PosY, Me, D);
        -target(_);
        -target(_)[source(percept)];
        .broadcast(tell, share(PosX, PosY)).

+target(PosX, PosY) : direction(D) & name(Me) & position(PosX, PosY)[source(Other)] & (Other \== self) & (PosX >= 0 & PosY >= 0) <-  
        //.print("Posizione (", PosX, ",", PosY, ") occupata, riprovo tra poco...");
        .wait(500);
        -+target(PosX, PosY).

+share(PosX, PosY)[source(Other)] : name(Me) & (Other \== self) <-
    -share(_, _)[source(Other)]; 
    -+position(PosX, PosY)[source(Other)].

+target(PosX, PosY) : name(Me) & (PosX < 0 | PosY < 0) <-
    .broadcast(untell, position(PosX, PosY));
    !terminate.

+!terminate : name(Me) <-
    .print("Terminating.");
    remove_car(Me);
    .kill_agent(Me).
