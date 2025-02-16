+start(GREEN, PosX, PosY) : true <-
    if (GREEN = true) { COLOR = green } else { COLOR = red };
    .my_name(Me);
    .print("Started with ", PosX, " and ", PosY, " with color ", COLOR);
    spawn_traffic_light(GREEN, PosX, PosY, Me);
    if (GREEN = true) { TIME = 5000 } else { TIME = 6000 };
    .wait(TIME);
    if (GREEN = true) { NEXT = yellow } else { NEXT = green };
    !cycle(NEXT).

+!cycle(green) : true <-
    .my_name(Me);
    update_traffic_light(green, Me);
    .wait(5000);
    !cycle(yellow).

+!cycle(yellow) : true <-
    .my_name(Me);
    update_traffic_light(yellow, Me);
    .wait(1000);
    !cycle(red).

+!cycle(red) : true <-
    .my_name(Me);
    update_traffic_light(red, Me);
    .wait(6000);
    !cycle(green).