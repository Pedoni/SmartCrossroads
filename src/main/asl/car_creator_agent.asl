cars(1).
lights(0).

+start_creation(X, Y) <- !create_traffic_lights(X, Y).

+!create_traffic_lights(X, Y) <-
    ROAD = 100;
    TILE = 50;
    List1 = [
        TILE * 4.5,
        TILE * 7.5,
        TILE * 4.5,
        TILE * 7.5,

        TILE * 9.5,
        TILE * 12.5,
        TILE * 9.5,
        TILE * 12.5,

        TILE * 4.5,
        TILE * 7.5,
        TILE * 4.5,
        TILE * 7.5,

        TILE * 9.5,
        TILE * 12.5,
        TILE * 9.5,
        TILE * 12.5
    ];
    List2 = [
        TILE * 2.5,
        TILE * 5.5,
        TILE * 5.5,
        TILE * 2.5,

        TILE * 2.5,
        TILE * 5.5,
        TILE * 5.5,
        TILE * 2.5,

        TILE * 7.5,
        TILE * 10.5,
        TILE * 10.5,
        TILE * 7.5,

        TILE * 7.5,
        TILE * 10.5,
        TILE * 10.5,
        TILE * 7.5
    ];
    while(lights(L) & L <= 15) {
        .nth(L, List1, Xs);
        .nth(L, List2, Ys);
        .concat("traffic_light_", L, N);
        .create_agent(N, "traffic_light_agent.asl");
        if (L mod 2 = 0) { GREEN = true } else { GREEN = false };
        .send(N, tell, start(GREEN, Xs, Ys));
        -+lights(L + 1);
    };
    !create_next_car(X, Y).

+!create_next_car(X, Y) : cars(C) <-
    .random(R);
    RInt = (R * 8) - ((R * 8) mod 1);
    !get_spawn_position(RInt, X, Y, Xs, Ys);
    .concat("car_", C, N);
    .create_agent(N, "car_agent.asl");
    .send(N, tell, start(Xs, Ys));
    -+cars(C + 1);
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
