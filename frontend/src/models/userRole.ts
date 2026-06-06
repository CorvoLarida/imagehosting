import { DTO } from "./dto";

export class UserRole extends DTO{
    id: string;
    name: string;
    constructor(id: string, name: string) {
        super();
        this.id = id;
        this.name = name;
    }

    static checkData(data: any): boolean {
        if (data.id === undefined) return false;
        if (data.name === undefined) return false;
        return true;
    }
}
