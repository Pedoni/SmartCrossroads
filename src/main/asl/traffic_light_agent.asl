// Traffic Light Agent: Manages traffic light states and crossroad coordination

// Initialization with crossroad group and index
+start(GREEN, PosX, PosY, GroupID, Index) : true <-
    if (GREEN) { 
        COLOR = green;
        // First light in group starts the cycle
        +current_color(green);
        +group_id(GroupID);
        +my_index(Index);
        +total_lights(4);  // 4 lights per crossroad
    } else { 
        COLOR = red;
        +current_color(red);
        +group_id(GroupID);
        +my_index(Index);
        +total_lights(4);
    };
    .my_name(Me);
    .print("Started at (", PosX, ",", PosY, ") in group ", GroupID, " [Index ", Index, "]");
    spawn_traffic_light(GREEN, PosX, PosY, Me);
    if (GREEN) {
        !begin_cycle(green);  // Start the cycle if initial light is green
    }.

// Green phase - only one light in group can be green
+!begin_cycle(green) : 
    current_color(red) <-  // Only trigger if not already green
    .my_name(Me);
    update_traffic_light(green, Me);
    -current_color(_);
    +current_color(green);
    .print(Me, ": GREEN for group ", GroupID);
    .wait(5000);  // Green duration
    !transition_to(yellow).

// Yellow phase
+!transition_to(yellow) : 
    current_color(green) <-
    .my_name(Me);
    update_traffic_light(yellow, Me);
    -current_color(_);
    +current_color(yellow);
    .print(Me, ": YELLOW for group ", GroupID);
    .wait(1000);  // Yellow duration
    !transition_to(red).

// Red phase with coordination
+!transition_to(red) : 
    current_color(yellow) <-
    .my_name(Me);
    update_traffic_light(red, Me);
    -current_color(_);
    +current_color(red);
    .print(Me, ": RED for group ", GroupID);
    .wait(2000);  // All-red buffer period
    
    // Calculate next light in round-robin
    my_index(MyIndex);
    total_lights(Total);
    NextIndex = (MyIndex + 1) mod Total;
    
    // Broadcast to group (only group members will process)
    .broadcast(tell, next_light(GroupID, NextIndex));
    
    // Monitor for next cycle
    +waiting_for_next(NextIndex).

// Handle incoming coordination messages
+next_light(MyGroup, NextIndex) : 
    group_id(MyGroup) & 
    my_index(NextIndex) & 
    current_color(red) <-
    .print("Received coordination message - activating green");
    !begin_cycle(green).

// Cleanup when receiving messages for other indices
+next_light(MyGroup, _) : 
    group_id(MyGroup) <-
    true.  // Ignore if not the target index

// Maintenance rules to handle synchronization
+waiting_for_next(NextIndex) : 
    current_color(green) <-
    -waiting_for_next(_).

+waiting_for_next(NextIndex) : 
    .my_name(Me) &
    .time(T) &
    T > 10000 <-  // Timeout safety net
    .print("Timeout in coordination - forcing restart");
    -waiting_for_next(_);
    !begin_cycle(green).