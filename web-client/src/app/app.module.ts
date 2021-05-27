import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// Material
import { MatButtonModule } from '@angular/material/button';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatToolbarModule } from '@angular/material/toolbar';

// Components
import { LoginComponent } from './account/login/login.component';
import { MainModule } from './main/main.module';
import { ScoreboardComponent } from './main/scoreboard/scoreboard.component';
import { ThemeComponent } from './theme/theme.component';

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        ThemeComponent,
    ],
    imports: [
        AppRoutingModule,
        BrowserAnimationsModule,
        BrowserModule,
        HttpClientModule,
        MainModule,
        MatButtonModule,
        MatButtonToggleModule,
        MatGridListModule,
        MatIconModule,
        MatMenuModule,
        MatSidenavModule,
        MatSnackBarModule,
        MatToolbarModule,
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule { }
