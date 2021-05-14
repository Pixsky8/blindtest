import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ProfileResponse } from './profile';

@Injectable()
export class ProfileService {
    profileUrl = '/api/profile'

    constructor(private http: HttpClient) { }

    httpOptions = {
        headers: new HttpHeaders({ 'Content-Type': 'application/json' })
    };

    getProfile(): Observable<ProfileResponse | null> {
        return this.http.get<ProfileResponse>(this.profileUrl)
            .pipe(
                catchError(this.handleError<ProfileResponse | null>('ProfileResponse', null))
            );
    }

    private handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {
            console.error(error);
            return of(result as T);
        }
    }
}
