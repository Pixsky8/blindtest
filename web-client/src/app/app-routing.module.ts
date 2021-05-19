import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from "./account/login/login.component";
import { QuestionComponent } from './main/question/question.component';
import { SignupComponent } from './account/signup/signup.component';

const routes: Routes = [
    { path: '', component: QuestionComponent, pathMatch: 'full' },
    { path: 'login', component: LoginComponent },
    { path: 'signup', component: SignupComponent },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
