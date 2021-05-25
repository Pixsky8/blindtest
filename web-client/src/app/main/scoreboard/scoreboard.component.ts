import { Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';
import { MatSnackBar } from "@angular/material/snack-bar";

import { ScoreboardService } from "../../service/scoreboard/scoreboard.service"

import { ScoreboardResponse } from "../../service/scoreboard/scoreboard";

@Component({
    selector: 'app-scoreboard',
    templateUrl: './scoreboard.component.html',
    providers: [ScoreboardService],
    styleUrls: ['./scoreboard.component.css']
})
export class ScoreboardComponent implements OnInit {
    scoreboard: ScoreboardResponse | null = null;

    constructor(private scoreboardService: ScoreboardService,
                private router: Router,
                private snackBar: MatSnackBar) {}

    ngOnInit() {
        this.fetchScoreboard();
    }

    fetchScoreboard() {
        this.scoreboardService.getScoreboard().subscribe(rsp => {
            if (rsp == null || rsp.status == "FAILURE" || rsp.scoreboard == null) {
                this.snackMessage("Scoreboard: Could not get a valid answer from server.");
                return;
            }

            this.scoreboard = rsp;
        })
    }

    snackMessage(message: string) {
        this.snackBar.open(message, "Close", {
            duration: 5000,
        });
    }
}
