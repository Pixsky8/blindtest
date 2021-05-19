import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ScoreboardResponse } from './scoreboard';

@Injectable()
export class ScoreboardService {
    profileUrl = '/api/scoreboard';

    constructor(private http: HttpClient) { }

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    getScoreboard(): Observable<ScoreboardResponse | null> {
        return this.http.get<ScoreboardResponse>(this.profileUrl)
            .pipe(
                catchError(this.handleError<ScoreboardResponse | null>('ScoreboardResponse', null))
            );
    }

    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
            console.error(error);
            return of(result as T);
        }
    }
}
