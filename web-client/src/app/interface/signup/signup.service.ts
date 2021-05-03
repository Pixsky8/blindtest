import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { Observable, of } from "rxjs";
import { catchError } from "rxjs/operators";

import { SignupRequest } from "./signup";
import { Response } from "src/app/interface/response";

@Injectable()
export class SignupService {
    loginUrl = '/api/account'

    constructor(private http: HttpClient) { }

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    postLogin(signupInfo: SignupRequest): Observable<Response | null> {
        const signupInfoJson = JSON.stringify(signupInfo);
        return this.http.post<Response>(this.loginUrl,
            signupInfoJson,
            this.httpOptions).pipe(
                catchError(this.handleError<Response | null>('SignupResponse', null))
            );
    }

    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
            console.error(error);
            return of(result as T);
        }
    }
}
