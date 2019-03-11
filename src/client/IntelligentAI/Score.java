package client.IntelligentAI;

class Score {
    final static Integer IN_ZONE = 100000; //50
    final static Integer MOVE_COST = -4;
    final static Integer DISTANCE_COST = -99; //5
    final static Integer HEALTH_COST = -5;
    final static Integer KILL_COST = -3000;
    final static Integer KILL_DISTANCE_COST = 5000;
    final static Integer CAN_HIT_COST = -10;
    final static Integer MY_HERO_CELL = -1000;
    final static Integer WALL_SCORE = -1000;

    final static Integer BEFORE_CELL_SCORE = -800000;

    final static Integer DISTANCE_TO_My_HEROES = -81000;
    final static Integer DISTANCE_TO_OPP_HEROES = -4000;
    final static Integer MY_OTHER_HERO_AROUND_NEGATIVE_COST = -(((2 * MOVE_COST) + 1));
    final static Integer MY_OTHER_HERO_AROUND_NEGATIVE_COST_IN_ZONE = (((2000 * MOVE_COST) + 1) + IN_ZONE);

    // distance to zone = -4 * (manhatan distance)
}
