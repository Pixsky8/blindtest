import { Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';
import { MatSnackBar } from "@angular/material/snack-bar";

import { QuestionService } from "../service/question/question.service"
import { ProfileService } from "../service/profile/profile.service";
import { WebsocketService } from "../service/websocket/websocket.service";

import { AppComponent } from "../app.component";
import { Question, QuestionResponse } from "../service/question/question";

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    providers: [QuestionService, ProfileService, WebsocketService],
    styleUrls: []
})
export class MainComponent implements OnInit {
    question: Question | null | undefined = undefined;
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
        this.fetchQuestion();
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
                this.question = undefined;
            }
            else if (rsp.id == -1)
                this.question = null;
            else if (rsp.id == null
                || rsp.theme == null || rsp.audio == null
                || rsp.question == null)
                this.question = undefined;
            else {
                this.question = {
                    id: rsp.id,
                    theme: rsp.theme,
                    audio: rsp.audio,
                    question: rsp.question
                };
            }
        });
    }

    get getQuestion(): Question | null | undefined {
        return this.question;
    }

    snackMessage(message: string) {
        this.snackBar.open(message, "Close", {
            duration: 5000,
        });
    }
}
