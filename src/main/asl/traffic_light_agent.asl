+!start(PosX, PosY) : type(T) <-
    .my_name(Me);
    spawn_traffic_light(T = 0, PosX, PosY, Me);
    if (T = 0) { 
        .wait(2000);
        !cycle(yellow);
    }.

+!share(PosX, PosY)[source(Other)] : (Other \== self) & type(T) & tl_position(X, Y) & number(N) <-
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
    if ((N = 3 | N = 15) & PosY = Y & (PosX = X | (PosX - 1) = X | (PosX - 2) = X)) {
        +position(PosX, PosY)[source(Other)];
    };
    if ((N = 6 | N = 10) & PosY = Y & (PosX = X | (PosX + 1) = X | (PosX + 2) = X)) {
        +position(PosX, PosY)[source(Other)];
    }.

+!ask_position.

+!cycle(green) <-
    .my_name(Me);
    update_traffic_light(green, Me);
    .count(position(_, _), N);
    if (N = 0) {
        !cycle(red);
    } else {
        TIME = N * 1000;
        .wait(TIME);
        !cycle(yellow);
    }.

+!cycle(yellow) <-
    .my_name(Me);
    update_traffic_light(yellow, Me);
    .wait(500);
    !cycle(red).

+!cycle(red) : number(N) <-
    .my_name(Me);
    update_traffic_light(red, Me);
    .wait(1000);
    if (not(N = 0) & N mod 4 = 3) {M = N - 3;} else { M = N + 1;};
    .concat("traffic_light_", M, Res);
    .send(Res, achieve, cycle(green)).
