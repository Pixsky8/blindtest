import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

import { LoginService } from '../../service/login/login.service';
import { LoginRequest } from '../../service/login/login';

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

    createAccountRedirect(): void {
        this.router.navigate(['/signup']);
    }

    snackMessage(message: string): void {
        this.snackBar.open(message, "Close", {
            duration: 5000,
        });
    }
}
