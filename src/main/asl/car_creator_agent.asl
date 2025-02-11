!start.

+!start : true <- !create_next_car(x).

+!create_next_car(x) : true <-
    .print("Creating car agent car");
    spawn_car(1);
    .create_agent(car, "car_agent.asl");
    .wait(1000);
    !create_next_car(x).

+!terminate_car(x) : true <-
    .print("Terminating car agent car");
    kill_agent("car").
