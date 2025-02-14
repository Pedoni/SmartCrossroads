+start(GREEN, X, Y) : true <-
    .my_name(Me);
    .print("Started with ", X, " and ", Y);
    spawn_traffic_light(GREEN, X, Y, Me).