package client.IntelligentAI;

class Score {
    final static Integer IN_ZONE = 50;
    final static Integer MOVE_COST = -4;
    final static Integer DISTANCE_COST = -5;
    final static Integer HEALTH_COST = -10;
    final static Integer KILL_COST = -1000;
    final static Integer KILL_DISTANCE_COST = 100;
    final static Integer CAN_HIT_COST = -10;
    final static Integer MY_HERO_CELL = -1000;
    final static Integer WALL_SCORE = -1000;

    // distance to zone = -4 * (manhatan distance)
}
