green_time(5000).
yellow_time(1000).
red_time(6000).

+!start : is_green(GREEN) & position(PosX, PosY) <-
    .my_name(Me);
    +name(Me);
    if (GREEN) { COLOR = green } else { COLOR = red };
    spawn_traffic_light(GREEN, PosX, PosY, Me);
    if (GREEN) { 
        .wait(2000);
        !cycle(yellow);
    }.

+!cycle(green) <-
    .print("I go green");
    .my_name(Me);
    update_traffic_light(green, Me);
    .wait(2000);
    !cycle(yellow).

+!cycle(yellow) <-
    .my_name(Me);
    update_traffic_light(yellow, Me);
    .wait(500);
    !cycle(red).

+!cycle(red) : number(N) <-
    .my_name(Me);
    update_traffic_light(red, Me);
    .wait(1000);
    if (not(N = 0) & N mod 4 = 3) {
        M = N - 3;
    } else {
        M = N + 1;
    };
    .concat("traffic_light_", M, Res);
    .print("Sending achieve to ", Res);
    .send(Res, achieve, cycle(green)).