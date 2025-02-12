!start.

+!start : true <-
    .print("Car started");
    .wait(3000);
    !terminate.

+!terminate : true <-
    .print("Car terminating.");
    kill_car(x).