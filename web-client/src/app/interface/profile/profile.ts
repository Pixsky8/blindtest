import { Response } from '../response'

export interface ProfileResponse extends Response {
    login: string | null
}
