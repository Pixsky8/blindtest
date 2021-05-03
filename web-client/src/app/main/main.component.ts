import { Component, OnInit } from "@angular/core";
import { Router } from '@angular/router';

import { ProfileService } from "../interface/profile/profile.service";

import { AppComponent } from "../app.component";

@Component({
    selector: 'app-main',
    templateUrl: './main.component.html',
    providers: [ProfileService],
    styleUrls: []
})
export class MainComponent implements OnInit {
    constructor(private profileService: ProfileService,
                private router: Router) {}

    ngOnInit() {
        this.profileService.getProfile().subscribe(rsp => {
            console.log(rsp);
            if (rsp == null || ((rsp.status != "FAILURE" || rsp.login == null)
                && rsp.errorCode == "NOT_LOGGED")) {
                this.router.navigate(['/login']);
            }
            else {
                AppComponent.login = rsp.login;
            }
        });
    }
}