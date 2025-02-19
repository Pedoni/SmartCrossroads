cars(1).
lights(0).

!start_creation.

+!start_creation <-
    List1 = [5, 6, 4, 7, 10, 11, 9, 12, 5, 6, 4, 7, 10, 11, 9, 12];
    List2 = [2, 5, 4, 3, 2, 5, 4, 3, 7, 10, 9, 8, 7, 10, 9, 8];
    while(lights(L) & L <= 15) {
        .nth(L, List1, PosX);
        .nth(L, List2, PosY);
        .concat("traffic_light_", L, N);
        .create_agent(N, "traffic_light_agent.asl");
        if (L mod 2 = 0) { GREEN = true } else { GREEN = false };
        .send(N, tell, start(GREEN, PosX, PosY));
        -+lights(L + 1);
    };
    !create_next_car.

+!create_next_car : cars(C) <-
    .random(R);
    RInt = (R * 8) - ((R * 8) mod 1);
    !get_spawn_position(RInt, PosX, PosY);
    .concat("car_", C, N);
    .create_agent(N, "car_agent.asl");
    .send(N, tell, start(PosX, PosY));
    -+cars(C + 1);
    .wait(30000);
    !create_next_car.

+!get_spawn_position(0, 0, 4).
+!get_spawn_position(1, 0, 9).
+!get_spawn_position(2, 5, 0).
+!get_spawn_position(3, 10, 0).
+!get_spawn_position(4, 16, 3).
+!get_spawn_position(5, 16, 8).
+!get_spawn_position(6, 11, 12).
+!get_spawn_position(7, 6, 12).
