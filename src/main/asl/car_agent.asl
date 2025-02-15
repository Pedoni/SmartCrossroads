+start(X, Y) : true <-
    .my_name(Me);
    .print("Started with ", X, " and ", Y);
    +position(X, Y);
    spawn_car(X, Y, Me);
    internal_actions.GetTargetPoint(X, Y);
    !path.

+!path : target(X, Y) <-
    .my_name(Me);
    .print("Target ", X, ", ", Y);
    .wait(3000);
    !terminate.

+!terminate : true <-
    .my_name(Me);
    .print("Terminating.");
    .kill_agent(Me).