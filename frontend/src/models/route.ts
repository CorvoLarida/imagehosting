
export class Route {
    path: string;
    component;
    name: string | undefined;

    constructor(path: string, component: any, name?: string | undefined) {
        this.path = path;
        this.component = component;
        this.name = name;
    }
}