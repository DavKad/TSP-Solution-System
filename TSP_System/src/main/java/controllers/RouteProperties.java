package controllers;

import jdk.nashorn.internal.objects.annotations.Getter;

class RouteProperties {
    private long time;
    private Double distance;
    private String NodeA;
    private String NodeB;

    RouteProperties(long time, Double distance, String nodeA, String nodeB) {
        this.time = time;
        this.distance = distance;
        this.NodeA = nodeA;
        this.NodeB = nodeB;
    }

    @Getter
    long getTime() {
        return time;
    }

    @Getter
    Double getDistance() {
        return distance;
    }

    @Getter
    String getNodeA() {
        return NodeA;
    }

    @Getter
    String getNodeB() {
        return NodeB;
    }
}
