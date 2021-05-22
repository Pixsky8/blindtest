import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";

import { QuestionComponent } from "./question/question.component";
import { ScoreboardComponent } from "./scoreboard/scoreboard.component";

@NgModule({
    imports: [CommonModule],
    declarations: [
        QuestionComponent,
        ScoreboardComponent
    ]
})
export class MainModule { }
