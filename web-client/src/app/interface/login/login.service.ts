import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { LoginRequest } from './login';
import { Response } from '../response';

@Injectable()
export class LoginService {
    loginUrl = '/api/login'

    constructor(private http: HttpClient) { }

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    postLogin(loginInfo: LoginRequest): Observable<Response | null> {
        const loginInfoJson = JSON.stringify(loginInfo);
        return this.http.post<Response>(this.loginUrl,
            loginInfoJson,
            this.httpOptions).pipe(
                catchError(this.handleError<Response | null>('LoginResponse', null))
            );
    }

    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
            console.error(error);
            return of(result as T);
        }
    }
}
