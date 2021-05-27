import { Component, OnInit, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';

@Component({
    selector: 'app-theme',
    templateUrl: './theme.component.html',
    styleUrls: []
})
export class ThemeComponent implements OnInit {
    private static readonly light_name = 'light';
    private static readonly dark_path = 'dark-theme';
    private static readonly dark_name = 'dark';

    public theme: string;

    constructor(@Inject(DOCUMENT) private document: Document) {
        this.theme =
            this.document.documentElement.classList.contains(ThemeComponent.dark_path)
                ? ThemeComponent.dark_name
                : ThemeComponent.light_name;
    }

    ngOnInit(): void { }

    public selectDarkTheme(): void {
        this.document.documentElement.classList.add(ThemeComponent.dark_path);
        this.theme = ThemeComponent.dark_name;
    }

    public selectLightTheme(): void {
        this.document.documentElement.classList.remove(ThemeComponent.dark_path);
        this.theme = ThemeComponent.light_name;
    }
}
