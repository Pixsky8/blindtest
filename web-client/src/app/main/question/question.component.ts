import { Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';
import { MatSnackBar } from "@angular/material/snack-bar";

import { QuestionService } from "../../service/question/question.service"
import { ProfileService } from "../../service/profile/profile.service";
import { WebsocketService } from "../../service/websocket/websocket.service";

import { AppComponent } from "../../app.component";
import { Question, QuestionResponse } from "../../service/question/question";
import { AnswerService } from "src/app/service/answer/answer.service";
import { AnswerRequest } from "src/app/service/answer/answer";

@Component({
    selector: 'app-question',
    templateUrl: './question.component.html',
    providers: [
        AnswerService,
        QuestionService,
        ProfileService,
        WebsocketService
    ],
    styleUrls: []
})
export class QuestionComponent implements OnInit {
    question: Question | null | undefined = undefined;
    message: string | null = null;

    constructor(private answerService: AnswerService,
                private questionService: QuestionService,
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

    get getQuestion(): Question {
        if (this.question == null || this.question == undefined) {
            return {
                id: this.question == null ? -1 : -2,
                theme: "Waiting...",
                audio: false,
                question: ""
            }
        }
        return this.question;
    }

    answerQuestion(answer: string) {
        if (this.question == null) {
            this.snackMessage("There is currently no question.");
            return;
        }

        var answerRequest: AnswerRequest = {
            questionId: this.question.id,
            answer: answer,
        }
        this.answerService.postAnswer(answerRequest).subscribe(rsp => {
            if (rsp == null)
                this.snackMessage("Could not get a valid response from server.");
            else if (rsp.status != "SUCCESS")
                this.snackMessage(rsp.errorMessage ? rsp.errorMessage : "Could not send answer.");
            else
                this.snackMessage("Answer registered.");
        })
    }

    snackMessage(message: string) {
        this.snackBar.open(message, "Close", {
            duration: 5000,
        });
    }
}
