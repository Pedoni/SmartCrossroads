+start(PosX, PosY, D) <-
    .my_name(Me);
    +name(Me);
    +direction(D);
    .print("Started with ", PosX, " and ", PosY);
    spawn_car(PosX, PosY, Me);
    .print("Direction: ", D);
    internal_actions.GetTargetPoint(PosX, PosY, D).

+target(PosX, PosY) : direction(D) & name(Me) & PosX >= 0 <-
    -target(_, _);
    .print("Target: ", PosX, " and ", PosY);
    move_car(PosX, PosY, Me, D).

+target(PosX, PosY) : name(Me) & PosX < 0 <-
    !terminate.

+!terminate : name(Me) <-
    remove_car(Me);
    .print("Terminating.");
    .kill_agent(Me).
