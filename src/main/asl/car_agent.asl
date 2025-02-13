!start.

+!start : true <-
    .print("Started");
    .wait(3000);
    !terminate.

+!terminate : true <-
    .my_name(Me);
    .print("Terminating.");
    .kill_agent(Me).