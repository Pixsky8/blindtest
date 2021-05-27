import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { MatButtonModule } from "@angular/material/button";
import { MatGridListModule } from '@angular/material/grid-list';

import { QuestionComponent } from "./question/question.component";
import { ScoreboardComponent } from "./scoreboard/scoreboard.component";

@NgModule({
    imports: [
        CommonModule,
        MatButtonModule,
        MatGridListModule,
    ],
    declarations: [
        QuestionComponent,
        ScoreboardComponent
    ]
})
export class MainModule { }
