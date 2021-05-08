import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

import { LoginService } from './interface/login/login.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    providers: [LoginService],
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    title = 'Blindtest';
    static login: string | null = null;

    constructor(private loginService: LoginService,
                private router: Router,
                private snackBar: MatSnackBar) { }

    get getLogin(): string | null {
        return AppComponent.login;
    }

    logout(): void {
        this.loginService.deleteLogin()
            .subscribe(rsp => {
                if (rsp == null) {
                    this.snackMessage("Could not get a valid response from server");
                    return;
                }

                if (rsp.status == "SUCCESS") {
                    this.snackMessage("Successfully Logged out.");
                    AppComponent.login = null;
                    this.router.navigate(['/login'])
                }
                else {
                    if (rsp.errorMessage == null)
                        this.snackMessage("Could not logout.");
                    else
                        this.snackMessage(rsp.errorMessage);
                }
            });
    }

    snackMessage(message: string): void {
        this.snackBar.open(message, "Close", {
            duration: 5000,
        });
    }
}
