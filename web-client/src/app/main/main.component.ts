import { Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';
import { MatSnackBar } from "@angular/material/snack-bar";

import { QuestionService } from "../service/question/question.service"
import { ProfileService } from "../service/profile/profile.service";
import { WebsocketService } from "../service/websocket/websocket.service";

import { AppComponent } from "../app.component";
import { QuestionResponse } from "../service/question/question";

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    providers: [QuestionService, ProfileService, WebsocketService],
    styleUrls: []
})
export class MainComponent implements OnInit {
    question: QuestionResponse | null = null;
    message: string | null = null;

    constructor(private questionService: QuestionService,
                private profileService: ProfileService,
                private websocketService: WebsocketService,
                private router: Router,
                private snackBar: MatSnackBar) {}

    ngOnInit() {
        this.profileService.getProfile().subscribe(rsp => {
            console.log(rsp);
            if (rsp == null || ((rsp.status != "FAILURE" || rsp.login == null)
                && rsp.errorCode == "NOT_LOGGED")) {
                this.router.navigate(['/login']);
                return;
            }
            else {
                AppComponent.login = rsp.login;
            }
        });
        var self = this;
        console.log(this.websocketService.getSocket);
        this.websocketService.getSocket.onmessage = function (event) {
            console.log("New socket message");
            if (event.data == "UPDATE")
                self.fetchQuestion();
        }
    }

    fetchQuestion() {
        this.questionService.getCurrentQuestion().subscribe(rsp => {
            if (rsp == null) {
                this.snackMessage("Could not get a valid answer from server.");
                this.question = null;
                return;
            }
            this.question = rsp;
        });
    }

    get getQuestionId(): number {
        if (this.question == null || this.question.id == null)
            return -1;
        return this.question.id;
    }

    snackMessage(message: string) {
        this.snackBar.open(message, "Close", {
            duration: 5000,
        });
    }
}
