+start(PosX, PosY) <-
    .my_name(Me);
    +name(Me);
    .print("Started with ", PosX, " and ", PosY);
    spawn_car(PosX, PosY, Me);
    internal_actions.GetTargetPoint(PosX, PosY).

+target(PosX, PosY) : name(Me) & PosX >= 0 <-
    move_car(PosX, PosY, Me).

+target(PosX, PosY) : name(Me) & PosX < 0 <-
    !terminate.

+!terminate <-
    .my_name(Me);
    remove_car(Me);
    .print("Terminating.");
    .kill_agent(Me).
