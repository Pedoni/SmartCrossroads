count(1).

+start_creation(X, Y) <- !create_next_car(X, Y).

+!create_next_car(X, Y) : count(C) <-
    .random(R);
    RInt = (R * 8) - ((R * 8) mod 1);
    !get_spawn_position(RInt, X, Y, Xs, Ys);
    .concat("car_", C, N);
    .create_agent(N, "car_agent.asl");
    .send(N, tell, start(Xs, Ys));
    -+count(C + 1);
    .wait(1000);
    !create_next_car(X, Y).

+!get_spawn_position(0, X, Y, 0, Y / 3 + 100 / 4).
+!get_spawn_position(1, X, Y, 0, Y * 2 / 3 + 100 / 4).
+!get_spawn_position(2, X, Y, X / 3 - 100 / 4, 0).
+!get_spawn_position(3, X, Y, X * 2 / 3 - 100 / 4, 0).
+!get_spawn_position(4, X, Y, X, Y / 3 - 100 / 4).
+!get_spawn_position(5, X, Y, X, Y * 2 / 3 - 100 / 4).
+!get_spawn_position(6, X, Y, X * 2 / 3 + 100 / 4, Y).
+!get_spawn_position(7, X, Y, X / 3 + 100 / 4, Y).
