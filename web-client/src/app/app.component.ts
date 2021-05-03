import { Component } from '@angular/core';
import { ProfileService } from './interface/profile/profile.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    providers: [ProfileService],
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    title = 'Blindtest';
    static login: string | null = null;

    get getLogin(): string | null {
        return AppComponent.login;
    }
}
