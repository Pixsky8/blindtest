import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

import { LoginService } from '../interface/login/login.service';
import { LoginRequest } from '../interface/login/login';
import { ProfileService } from '../interface/profile/profile.service'

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    providers: [
        LoginService,
        ProfileService,
    ],
    styleUrls: []
})
export class LoginComponent implements OnInit {
    constructor(private loginService: LoginService,
                private profileService: ProfileService,
                private snackBar: MatSnackBar) { }

    ngOnInit(): void { }

    login(user: string, passwd: string): void {
        var request: LoginRequest = {
            login: user,
            password: passwd,
        }

        this.loginService.postLogin(request)
            .subscribe(rsp => {
                if (rsp == null) {
                    this.snackMessage("Could not get a valid response from server");
                    return;
                }

                if (rsp.status == "SUCCESS")
                    this.snackMessage("Successfully Logged in.");
                else {
                    if (rsp.errorMessage == null)
                        this.snackMessage("Wrong login or password.");
                    else
                        this.snackMessage(rsp.errorMessage);
                }
            });
    }

    getUserName() {
        this.profileService.getProfile()
            .subscribe(rsp => {
                if (rsp == null)
                    this.snackMessage("Server Error.");
                else if (rsp.login == null)
                    this.snackMessage("You are not logged in")
                else
                    this.snackMessage("Logged as: " + rsp.login);
            })
    }

    snackMessage(message: string) {
        this.snackBar.open(message, "Close", {
            duration: 5000,
        });
    }
}
