import { Component, OnInit } from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Router } from "@angular/router";

import { SignupRequest } from "src/app/interface/signup/signup";
import { SignupService } from "src/app/interface/signup/signup.service";

@Component({
    selector: 'app-sign-up',
    templateUrl: './signup.component.html',
    providers: [SignupService],
    styleUrls: []
})
export class SignupComponent implements OnInit {
    constructor(private signupService: SignupService,
                private router: Router,
                private snackBar: MatSnackBar) { }

    ngOnInit() { }

    signup(user: string, passwd: string): void {
        if (passwd.length < 3)
            this.snackMessage("Your password must be longer than 3 characters");

        var request: SignupRequest = {
            login: user,
            password: passwd,
        }

        this.signupService.postLogin(request)
        .subscribe(rsp => {
            if (rsp == null) {
                this.snackMessage("Could not get a valid response from server");
                return;
            }

            if (rsp.status == "SUCCESS") {
                this.snackMessage("Account successfully created. You can now log in.");
                this.router.navigate(['/login'])
            }
            else {
                if (rsp.errorMessage == null)
                    this.snackMessage("Server issue, try again later.");
                else
                    this.snackMessage(rsp.errorMessage);
            }
        });
    }

    snackMessage(message: string) {
        this.snackBar.open(message, "Close", {
            duration: 5000,
        });
    }
}