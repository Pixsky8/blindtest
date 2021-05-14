import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { QuestionResponse } from './question';

@Injectable()
export class QuestionService {
    profileUrl = '/api/question'

    constructor(private http: HttpClient) { }

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    getCurrentQuestion(): Observable<QuestionResponse | null> {
        return this.http.get<QuestionResponse>(this.profileUrl)
            .pipe(
                catchError(this.handleError<QuestionResponse | null>('QuestionResponse', null))
            );
    }

    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
            console.error(error);
            return of(result as T);
        }
    }
}
