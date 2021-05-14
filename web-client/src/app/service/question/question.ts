import { Response } from '../response';

export interface QuestionResponse extends Response {
    id: number | null
    theme: string | null
    audio: boolean | null
    question: string | null
}
