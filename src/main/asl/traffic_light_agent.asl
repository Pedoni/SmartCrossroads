+!start(PosX, PosY) : type(T) <-
    +tl_position(PosX, PosY);
    .my_name(Me);
    +name(Me);
    +is_green(T = 0);
    spawn_traffic_light(T = 0, PosX, PosY, Me);
    if (T = 0) { 
        .wait(2000);
        !cycle(yellow);
    }.

+share(PosX, PosY)[source(Other)] : (Other \== self) & type(T) & tl_position(X, Y) & number(N) <-
    -position(_, _)[source(Other)];
    if (T = 0 & PosX = X & (PosY = Y | (PosY + 1) = Y | (PosY + 2) = Y)) {
        +position(PosX, PosY)[source(Other)];
    };
    if (T = 1 & PosX = X & (PosY = Y | (PosY - 1) = Y | (PosY - 2) = Y)) {
        +position(PosX, PosY)[source(Other)];
    };
    if ((N = 2 | N = 14) & PosY = Y & (PosX = X | (PosX + 1) = X | (PosX + 2) = X | (PosX + 3) = X | (PosX + 4) = X)) {
        +position(PosX, PosY)[source(Other)];
    };
    if ((N = 7 | N = 11) & PosY = Y & (PosX = X | (PosX - 1) = X | (PosX - 2) = X | (PosX - 3) = X | (PosX - 4) = X)) {
        +position(PosX, PosY)[source(Other)];
    };
    -share(_, _)[source(Other)].

+direction(_)[source(Other)] : name(Me) & (Other \== self) <-
    -direction(_)[source(Other)]. 

+ask_position[source(Other)] : (Other \== self) <-
    -ask_position[source(Other)].

+!cycle(green) <-
    -+is_green(true);
    .my_name(Me);
    update_traffic_light(green, Me);
    .count(position(_, _), N);
    if (N = 0) {
        .wait(1000);
    } else {
        .wait(N * 1000);
    }
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
