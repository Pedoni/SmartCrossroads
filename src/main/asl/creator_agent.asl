cars(1).
lights(0).

!start_creation.

+!start_creation <-
    List1 = [5, 6, 4, 7, 10, 11, 9, 12, 5, 6, 4, 7, 10, 11, 9, 12];
    List2 = [2, 5, 4, 3, 2, 5, 4, 3, 7, 10, 9, 8, 7, 10, 9, 8];
    // In creator_agent.asl modification
    while(lights(L) & L <= 15) {
        GroupID = L / 4;  // 0-3 groups for 16 lights
        IndexInGroup = L mod 4;
        .nth(L, List1, PosX);
        .nth(L, List2, PosY);
        .concat("traffic_light_", L, N);
        .create_agent(N, "traffic_light_agent.asl");
        
        if (IndexInGroup == 0) {
            .send(N, tell, start(true, PosX, PosY, GroupID, IndexInGroup));
        } else {
            .send(N, tell, start(false, PosX, PosY, GroupID, IndexInGroup));
        }
        -+lights(L + 1);
    };
    !create_next_car.

+!create_next_car : cars(C) <-
    .random(R);
    RInt = (R * 8) - ((R * 8) mod 1);
    !get_spawn_position(RInt, PosX, PosY, D);
    .concat("car_", C, N);
    .create_agent(N, "car_agent.asl");
    .send(N, tell, start(PosX, PosY, D));
    -+cars(C + 1);
    .wait(1500);
    !create_next_car.

+!get_spawn_position(0, 0, 4, 3).
+!get_spawn_position(1, 0, 9, 3).
+!get_spawn_position(2, 5, 0, 0).
+!get_spawn_position(3, 10, 0, 0).
+!get_spawn_position(4, 16, 3, 2).
+!get_spawn_position(5, 16, 8, 2).
+!get_spawn_position(6, 11, 12, 1).
+!get_spawn_position(7, 6, 12, 1).
