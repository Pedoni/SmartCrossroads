+start(PosX, PosY) <-
    .my_name(Me);
    +name(Me);
    .print("Started with ", PosX, " and ", PosY);
    spawn_car(PosX, PosY, Me);
    internal_actions.GetTargetPoint(PosX, PosY).
    //!path(Me).

+target(_, _) : name(Me) <-
    if (target(PosX, PosY) & (PosX >= 0)) {
        move_car(PosX, PosY, Me);
    } else {
        !terminate;
    }.

+!terminate <-
    .my_name(Me);
    remove_car(Me);
    .print("Terminating.");
    .kill_agent(Me).
