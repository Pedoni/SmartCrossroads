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

+share(PosX, PosY)[source(Other)] : (Other \== self) <-
    -share(_, _)[source(Other)]; 
    -+position(PosX, PosY)[source(Other)].

+position(_, _)[source(Other)] : name(Me) & (Other \== self) <-
    -position(_, _)[source(Other)].

+direction(_)[source(Other)] : name(Me) & (Other \== self) <-
    -direction(_)[source(Other)]. 

+ask_position[source(Other)] : (Other \== self) <-
    -ask_position[source(Other)].

+!cycle(green) <-
    -+is_green(true);
    .my_name(Me);
    update_traffic_light(green, Me);
    .wait(2000);
    !cycle(yellow).

+!cycle(yellow) <-
    -+is_green(false);
    .my_name(Me);
    update_traffic_light(yellow, Me);
    .wait(500);
    !cycle(red).

+!cycle(red) : number(N) <-
    -+is_green(false);
    .my_name(Me);
    update_traffic_light(red, Me);
    .wait(1000);
    if (not(N = 0) & N mod 4 = 3) {
        M = N - 3;
    } else {
        M = N + 1;
    };
    .concat("traffic_light_", M, Res);
    .send(Res, achieve, cycle(green)).