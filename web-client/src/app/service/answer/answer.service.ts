import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { AnswerRequest } from "./answer";
import { Response } from '../response';

@Injectable()
export class AnswerService {
    answerUrl = '/api/answer'

    constructor(private http: HttpClient) { }

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    postAnswer(answerInfo: AnswerRequest): Observable<Response | null> {
        const answerInfoJson = JSON.stringify(answerInfo);
        return this.http.post<Response>(this.answerUrl,
            answerInfoJson,
            this.httpOptions).pipe(
                catchError(this.handleError<Response | null>('AnswerResponse', null))
            );
    }

    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
            console.error(error);
            return of(result as T);
        }
    }
}
