green_time(5000).
yellow_time(1000).
red_time(6000).

+!start : is_green(GREEN) & position(PosX, PosY) <-
    .my_name(Me);
    +name(Me);
    if (GREEN) { COLOR = green } else { COLOR = red };
    .print("Started with ", PosX, " and ", PosY, " with color ", COLOR);
    spawn_traffic_light(GREEN, PosX, PosY, Me);
    if (GREEN) { TIME = 5000 } else { TIME = 6000 };
    .wait(TIME);
    if (GREEN) { NEXT = yellow } else { NEXT = green };
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