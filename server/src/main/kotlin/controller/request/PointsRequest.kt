package controller.request

class PointsRequest {
    val username: String
    val points: Int

    constructor(username: String, points: Int) {
        this.username = username
        this.points = points
    }
}
