+start_creation(X, Y) <- !create_next_car(X, Y).

+!create_next_car(X, Y) : true <- 
    .random(R);
    RInt = (R * 8) - ((R * 8) mod 1);
    !get_spawn_position(RInt, X, Y, Xs, Ys);
    .print("Creating car at: (", Xs, ", ", Ys, ")");
    spawn_car(1);
    .create_agent(car, "car_agent.asl", [Xs, Ys]);
    .wait(1000);
    !create_next_car(X, Y).

+!get_spawn_position(0, X, Y, 0, Y / 3 + 10 / 4).
+!get_spawn_position(1, X, Y, 0, Y * 2 / 3 + 10 / 4).
+!get_spawn_position(2, X, Y, X / 3 - 10 / 4, 0).
+!get_spawn_position(3, X, Y, X * 2 / 3 - 10 / 4, 0).
+!get_spawn_position(4, X, Y, X, Y / 3 - 10 / 4).
+!get_spawn_position(5, X, Y, X, Y * 2 / 3 - 10 / 4).
+!get_spawn_position(6, X, Y, X * 2 / 3 + 10 / 4, Y).
+!get_spawn_position(7, X, Y, X / 3 + 10 / 4, Y).

+!terminate_car(X, Y) : true <-
    .print("Terminating car agent car");
    kill_agent("car").

