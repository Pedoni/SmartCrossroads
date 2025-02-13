+start(X, Y) : true <-
    .print("Started with ", X, " and ", Y);
    .wait(3000);
    !terminate.

+!terminate : true <-
    .my_name(Me);
    .print("Terminating.");
    .kill_agent(Me).