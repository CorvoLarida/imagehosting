export class ApiError {
    detail: string; 
    instance: string;
    status: number;
    title: string;
    type: string;
    constructor(
        detail: string, 
        instance: string,
        status: number,
        title: string,
        type: string,
    ) {
        this.detail = detail; 
        this.instance = instance;
        this.status = status;
        this.title = title;
        this.type = type;
    }
}