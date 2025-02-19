+start(PosX, PosY) : true <-
    .my_name(Me);
    .print("Started with ", PosX, " and ", PosY);
    +position(PosX, PosY);
    spawn_car(PosX, PosY, Me);
    internal_actions.GetTargetPoint(PosX, PosY);
    !path(Me).

+!path(Me) : target(PosX, PosY) <-
    if (PosX >= 0) {
        move_car(PosX, PosY, Me);
        internal_actions.GetTargetPoint(PosX, PosY);
    } else {
        !terminate;
    }.

+!terminate : true <-
    .my_name(Me);
    remove_car(Me);
    .print("Terminating.");
    .kill_agent(Me).

