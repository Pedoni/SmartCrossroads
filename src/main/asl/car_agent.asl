+start(X, Y) : true <-
    .my_name(Me);
    .print("Started with ", X, " and ", Y);
    spawn_car(X, Y, Me);
    .wait(3000);
    !terminate.

+!terminate : true <-
    .my_name(Me);
    .print("Terminating.");
    .kill_agent(Me).