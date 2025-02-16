+start(PosX, PosY) : true <-
    .my_name(Me);
    .print("Started with ", PosX, " and ", PosY);
    +position(PosX, PosY);
    spawn_car(PosX, PosY, Me);
    internal_actions.GetTargetPoint(PosX, PosY);
    !path.

+!path : target(PosX, PosY) <-
    .my_name(Me);
    .print("Target ", PosX, ", ", PosY);
    .wait(3000);
    !terminate.

+!terminate : true <-
    .my_name(Me);
    .print("Terminating.");
    .kill_agent(Me).