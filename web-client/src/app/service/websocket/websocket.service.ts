import { Injectable } from "@angular/core";


@Injectable()
export class WebsocketService {
    webSocketUrl: string;
    socket: WebSocket;

    constructor() {
        var baseUrl = location.origin
        if (baseUrl.match("https://*"))
            this.webSocketUrl = "wss://" + location.origin.substring(8) + "/api/ws/question"
        else
            this.webSocketUrl = "ws://" + location.origin.substring(7) + "/api/ws/question"
        this.socket = new WebSocket(this.webSocketUrl);
        this.socket.onopen = function(event) {
            console.log("Successfully opened socket.", this);
            this.send("Hello");
        }
    }

    get getSocket(): WebSocket {
        return this.socket;
    }
}
