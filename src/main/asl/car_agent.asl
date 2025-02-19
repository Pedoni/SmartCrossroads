+start(PosX, PosY) : true <-
    .my_name(Me);
    .print("Started with ", PosX, " and ", PosY);
    +position(PosX, PosY);
    spawn_car(PosX, PosY, Me);
    internal_actions.GetTargetPoint(PosX, PosY);
    !path.

+!path : target(PosX, PosY) <-
    .time(HH,MM,SS,MS);
    .print("Agent -> ", HH, ":", MM, ":", SS);
    .my_name(Me);
    .print("Target ", PosX, ", ", PosY);
    if (PosX = -1) {
        !terminate;
    } else {
        move_car(PosX, PosY, Me);
        -target(PosX, PosY);
        internal_actions.GetTargetPoint(PosX, PosY);
    }.
    //
    
    //!terminate.

+!terminate : true <-
    .my_name(Me);
    .print("Terminating.");
    .kill_agent(Me).

