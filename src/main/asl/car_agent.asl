+start(PosX, PosY, D) <-
    .my_name(Me);
    +name(Me);
    +position(PosX, PosY, D);
    spawn_car(PosX, PosY, Me);
    internal_actions.GetTargetPoint(PosX, PosY, D).

+target(PosX, PosY) : direction(D) & name(Me) & PosX >= 0 <-
    -target(_, _);
    -+position(PosX, PosY, D);
    move_car(PosX, PosY, Me, D).

+target(PosX, PosY) : name(Me) & PosX < 0 <-
    !terminate.

+!terminate : name(Me) <-
    .print("Terminating.");
    remove_car(Me);
    .kill_agent(Me).
