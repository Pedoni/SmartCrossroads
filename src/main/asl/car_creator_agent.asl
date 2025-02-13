+start_creation(X, Y) <- !create_next_car(X, Y).

+!create_next_car(X, Y) : true <-
    .print("Creating car agent car: (" , X , ", " , Y , ")");
    spawn_car(1);
    .create_agent(car, "car_agent.asl");
    .wait(1000);
    !create_next_car(X, Y).

+!terminate_car(X, Y) : true <-
    .print("Terminating car agent car");
    kill_agent("car").
