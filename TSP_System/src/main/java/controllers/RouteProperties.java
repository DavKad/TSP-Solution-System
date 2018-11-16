package controllers;

class RouteProperties {
    private Double time;
    private Double distance;
    private String NodeA;
    private String NodeB;

    RouteProperties(Double time, Double distance, String nodeA, String nodeB) {
        this.time = time;
        this.distance = distance;
        this.NodeA = nodeA;
        this.NodeB = nodeB;
    }

    Double getTime() {
        return time;
    }

    Double getDistance() {
        return distance;
    }


    String getNodeA() {
        return NodeA;
    }

    String getNodeB() {
        return NodeB;
    }
}
