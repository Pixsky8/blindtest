import { Response } from '../response'

export interface ScoreboardResponse extends Response {
    scoreboard: Map<string, number> | null;
}
