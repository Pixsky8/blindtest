import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';

import { LoginService } from '../../interface/login/login.service';
import { LoginRequest } from '../../interface/login/login';
import { Router } from '@angular/router';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    providers: [LoginService],
    styleUrls: []
})
export class LoginComponent implements OnInit {
    constructor(private loginService: LoginService,
                private router: Router,
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

                if (rsp.status == "SUCCESS") {
                    this.snackMessage("Successfully Logged in.");
                    this.router.navigate(['/'])
                }
                else {
                    if (rsp.errorMessage == null)
                        this.snackMessage("Wrong login or password.");
                    else
                        this.snackMessage(rsp.errorMessage);
                }
            });
    }

    createAccountRedirect() {
        this.router.navigate(['/signup']);
    }

    snackMessage(message: string) {
        this.snackBar.open(message, "Close", {
            duration: 5000,
        });
    }
}
